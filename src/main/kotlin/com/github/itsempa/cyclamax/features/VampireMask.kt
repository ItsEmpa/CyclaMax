package com.github.itsempa.cyclamax.features

import at.hannibal2.skyhanni.data.mob.Mob
import at.hannibal2.skyhanni.events.GuiRenderEvent
import at.hannibal2.skyhanni.events.LorenzTickEvent
import at.hannibal2.skyhanni.events.MobEvent
import at.hannibal2.skyhanni.utils.LorenzColor
import at.hannibal2.skyhanni.utils.LorenzUtils
import at.hannibal2.skyhanni.utils.LorenzVec
import at.hannibal2.skyhanni.utils.RenderUtils.renderString
import at.hannibal2.skyhanni.utils.TimeLimitedSet
import at.hannibal2.skyhanni.utils.getLorenzVec
import com.github.itsempa.cyclamax.CyclaMax
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.seconds

object VampireMask {
    private val config get() = CyclaMax.config.vampireMask

    val batDeathLocations = TimeLimitedSet<LorenzVec>(2.seconds)

    private var bats = mutableSetOf<Mob>()

    @SubscribeEvent
    fun onMobSpawn(event: MobEvent.Spawn.Projectile) {
        val mob = event.mob
        if (mob.name == "Vampire Mask Bat") {
            bats += mob
            if (config.highlight) mob.highlight(LorenzColor.RED.addOpacity(100))
        }
    }

    @SubscribeEvent
    fun onTick(event: LorenzTickEvent) {
        if (!isEnabled()) return
        bats = bats.filter {
            if (it.baseEntity.isDead || it.baseEntity.health == 0F) {
                val pos = it.baseEntity.getLorenzVec()
                batDeathLocations += pos
                KillsCounter.handleBatDeath(pos)
                false
            } else true
        }.toMutableSet()
    }

    @SubscribeEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        if (!isEnabled()) return
        config.position.renderString("§aBats: §b${bats.size}", posLabel = "Vampire Mask Bats")
    }

    private fun isEnabled() = LorenzUtils.inSkyBlock && config.enabled
}
