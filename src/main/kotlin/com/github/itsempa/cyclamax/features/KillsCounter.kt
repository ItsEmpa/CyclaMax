package com.github.itsempa.cyclamax.features

import at.hannibal2.skyhanni.data.ClickType
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.GuiRenderEvent
import at.hannibal2.skyhanni.events.InventoryFullyOpenedEvent
import at.hannibal2.skyhanni.events.ItemClickEvent
import at.hannibal2.skyhanni.events.LorenzRenderWorldEvent
import at.hannibal2.skyhanni.events.LorenzTickEvent
import at.hannibal2.skyhanni.utils.ChatUtils
import at.hannibal2.skyhanni.utils.EntityUtils.getEntities
import at.hannibal2.skyhanni.utils.InventoryUtils
import at.hannibal2.skyhanni.utils.ItemUtils.getInternalName
import at.hannibal2.skyhanni.utils.ItemUtils.getLore
import at.hannibal2.skyhanni.utils.ItemUtils.getSkullTexture
import at.hannibal2.skyhanni.utils.ItemUtils.name
import at.hannibal2.skyhanni.utils.LocationUtils
import at.hannibal2.skyhanni.utils.LocationUtils.distanceTo
import at.hannibal2.skyhanni.utils.LocationUtils.rayIntersects
import at.hannibal2.skyhanni.utils.LorenzUtils.isInIsland
import at.hannibal2.skyhanni.utils.LorenzVec
import at.hannibal2.skyhanni.utils.NEUInternalName
import at.hannibal2.skyhanni.utils.NEUInternalName.Companion.asInternalName
import at.hannibal2.skyhanni.utils.NumberUtil.addSeparators
import at.hannibal2.skyhanni.utils.NumberUtil.formatLong
import at.hannibal2.skyhanni.utils.RecalculatingValue
import at.hannibal2.skyhanni.utils.RegexUtils.firstMatcher
import at.hannibal2.skyhanni.utils.RenderUtils.exactPlayerEyeLocation
import at.hannibal2.skyhanni.utils.RenderUtils.renderRenderable
import at.hannibal2.skyhanni.utils.SimpleTimeMark
import at.hannibal2.skyhanni.utils.StringUtils.removeColor
import at.hannibal2.skyhanni.utils.TimeLimitedSet
import at.hannibal2.skyhanni.utils.getLorenzVec
import at.hannibal2.skyhanni.utils.renderables.Renderable
import at.hannibal2.skyhanni.utils.toLorenzVec
import com.github.itsempa.cyclamax.CyclaMax
import net.minecraft.client.Minecraft
import net.minecraft.entity.passive.EntityMooshroom
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.seconds

object KillsCounter {
    private val config get() = CyclaMax.config.killsCounter

    private val PRECURSOR_EYE by lazy { "PRECURSOR_EYE".asInternalName() }
    private val witherBlades by lazy {
        listOf(
            "NECRON_BLADE",
            "VALKYRIE",
            "SCYLLA",
            "ASTRAEA",
            "HYPERION",
        ).map { it.asInternalName() }.toSet()
    }

    private val bestiaryKillsPattern = "§7Kills: §a(?<kills>[\\d,.]+)".toPattern()

    private const val BESTIARY_SKULL =
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmI1Mjg0MWYyZmQ1ODllMGJjODRjYmFiZjllMWMyN2NiNzBjYWM5OGY4ZDZiM2RkMDY1ZTU1YTRkY2I3MGQ3NyJ9fX0="

    private fun NEUInternalName.isWitherBlade() = this in witherBlades

    private val wearingPrecursorEye by RecalculatingValue(5.seconds) {
        InventoryUtils.getHelmet()?.getInternalName() == PRECURSOR_EYE
    }

    private val recentPositions = TimeLimitedSet<LorenzVec>(2.seconds)

    private val playerPositions get() = recentPositions + LocationUtils.playerLocation()

    private val recentlyLookedMobs = TimeLimitedSet<EntityMooshroom>(2.seconds)

    private val recentlyDeadNotOwn = TimeLimitedSet<EntityMooshroom>(2.seconds)

    private var lastWitherBladeUse = SimpleTimeMark.farPast()
    private var lastSneak = SimpleTimeMark.farPast()
    private var lastLeftClick = SimpleTimeMark.farPast()

    private var display: Renderable? = null

    private var kills: Long
        get() = config.kills
        set(value) {
            config.kills = value
        }

    @SubscribeEvent
    fun onRenderOverlay(event: GuiRenderEvent) {
        if (!isEnabled()) return
        val renderable = display ?: return
        config.position.renderRenderable(renderable, "Kills Counter")
    }

    @SubscribeEvent
    fun onRenderWorld(event: LorenzRenderWorldEvent) {
        if (!IslandType.THE_FARMING_ISLANDS.isInIsland()) return
        val cow = event.rayTraceMooshroom() ?: return
        recentlyLookedMobs += cow
    }

    @SubscribeEvent
    fun onTick(event: LorenzTickEvent) {
        if (!IslandType.THE_FARMING_ISLANDS.isInIsland()) return
        val player = Minecraft.getMinecraft().thePlayer
        recentPositions += player.getLorenzVec()
        if (player.isSneaking) {
            lastSneak = SimpleTimeMark.now()
        }
    }

    @SubscribeEvent
    fun onItemUse(event: ItemClickEvent) {
        if (!IslandType.THE_FARMING_ISLANDS.isInIsland()) return
        val item = event.itemInHand ?: return
        when (event.clickType) {
            ClickType.RIGHT_CLICK -> {
                if (item.getInternalName().isWitherBlade()) {
                    lastWitherBladeUse = SimpleTimeMark.now()
                }
            }

            ClickType.LEFT_CLICK -> {
                lastLeftClick = SimpleTimeMark.now()
            }
        }
    }

    @SubscribeEvent
    fun onInventoryOpen(event: InventoryFullyOpenedEvent) {
        val inventoryName = event.inventoryName
        val items = event.inventoryItems
        val stack = items[4] ?: return
        if (!isBestiaryGui(stack, inventoryName)) return
        items.values.firstOrNull { item ->
            item.getSkullTexture() == BESTIARY_SKULL && item.name.startsWith("§aMushroom Cow ")
        }?.let { item ->
            bestiaryKillsPattern.firstMatcher(item.getLore()) {
                kills = group("kills").formatLong()
                update()
            }
        }
    }

    private fun addKill(): Boolean {
        kills++
        update()
        return true
    }

    private fun update() {
        val stringRenderable = Renderable.string("§aKills: §b${kills.addSeparators()}")
        display = Renderable.clickAndHover(
            Renderable.hoverable(
                Renderable.underlined(stringRenderable),
                stringRenderable,
            ),
            tips = listOf("§bClick to update!"),
            onClick = {
                @Suppress("DEPRECATION")
                ChatUtils.sendCommandToServer("be mushroom cow")
            },
        )
    }

    fun killMushroom(entity: EntityMooshroom) {
        val pos = entity.getLorenzVec()
        if (handleWitchMask(pos)) return
        if (handlePrecursorEye(entity)) return
        if (handleMelee(entity, pos)) return
        if (handleWitherBlade(pos)) return
        recentlyDeadNotOwn += entity
    }

    private fun handleWitchMask(pos: LorenzVec): Boolean {
        val locations = runCatching { VampireMask.batDeathLocations.toSet() }.getOrNull() ?: return false
        if (locations.isEmpty()) return false
        if (locations.none { it.distance(pos) < 5.0 }) return false
        return addKill()
    }

    private fun handlePrecursorEye(mob: EntityMooshroom): Boolean {
        if (mob !in recentlyLookedMobs) return false
        if (!wearingPrecursorEye || lastSneak.passedSince() > 2.seconds) return false
        return addKill()
    }

    private fun handleMelee(mob: EntityMooshroom, pos: LorenzVec): Boolean {
        if (mob !in recentlyLookedMobs) return false
        if (playerPositions.none { it.distance(pos) < 7 }) return false
        if (lastLeftClick.passedSince() > 2.seconds) return false
        return addKill()
    }

    private fun handleWitherBlade(pos: LorenzVec): Boolean {
        if (lastWitherBladeUse.passedSince() > 2.seconds) return false
        if (playerPositions.none { it.distance(pos) < 15 }) return false
        return addKill()
    }

    private val bestiaryTitlePattern = "^(?:\\(\\d+/\\d+\\) )?(Bestiary|.+) ➜ (.+)\$".toPattern()

    /**
     * Taken and modified from SkyHanni
     */
    private fun isBestiaryGui(stack: ItemStack, name: String): Boolean {
        val bestiaryGuiTitleMatcher = bestiaryTitlePattern.matcher(name)
        when {
            bestiaryGuiTitleMatcher.matches() -> return !("Bestiary" != bestiaryGuiTitleMatcher.group(1) &&
                stack.getLore().none { it.removeColor().startsWith("Families Found") })

            name == "Search Results" -> {
                val loreList = stack.getLore()
                if (loreList.size >= 2 && loreList[0].startsWith("§7Query: §a")
                    && loreList[1].startsWith("§7Results: §a")
                ) {
                    return true
                }
            }

            name.startsWith("The Farming Islands ➜ Mushroom") -> return true
        }
        return false
    }

    private fun LorenzRenderWorldEvent.rayTraceMooshroom(): EntityMooshroom? {
        val pos = exactPlayerEyeLocation()
        val look = Minecraft.getMinecraft().thePlayer.getLook(partialTicks).toLorenzVec().normalize()
        val possibleEntities = getEntities<EntityMooshroom>().filter {
            it.entityBoundingBox.rayIntersects(
                pos, look,
            )
        }
        return possibleEntities.minByOrNull { it.distanceTo(pos) }
    }

    fun handleBatDeath(pos: LorenzVec) {
        val locations = runCatching { recentlyDeadNotOwn.toSet() }.getOrNull() ?: return
        val cow = locations.minByOrNull { it.distanceTo(pos) } ?: return
        if (cow.distanceTo(pos) > 5) return
        recentlyDeadNotOwn -= cow
        addKill()
    }

    private fun isEnabled() = IslandType.THE_FARMING_ISLANDS.isInIsland() && config.enabled

}
