package com.github.itsempa.cyclamax.features.cyclabox

import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.data.mob.Mob
import at.hannibal2.skyhanni.events.LorenzRenderWorldEvent
import at.hannibal2.skyhanni.events.LorenzWorldChangeEvent
import at.hannibal2.skyhanni.events.MobEvent
import at.hannibal2.skyhanni.utils.ColorUtils.toChromaColor
import at.hannibal2.skyhanni.utils.ColorUtils.withAlpha
import at.hannibal2.skyhanni.utils.LorenzColor
import at.hannibal2.skyhanni.utils.LorenzUtils.isInIsland
import at.hannibal2.skyhanni.utils.RenderUtils.drawColor
import at.hannibal2.skyhanni.utils.RenderUtils.drawSphereInWorld
import at.hannibal2.skyhanni.utils.RenderUtils.exactLocation
import at.hannibal2.skyhanni.utils.TimeLimitedSet
import at.hannibal2.skyhanni.utils.toLorenzVec
import com.github.itsempa.cyclamax.CyclaMax
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class CyclaBox {

    private val config get() = CyclaMax.config.cyclaBox

    private val mobSet = mutableSetOf<Mob>()

    @SubscribeEvent
    fun onMobSpawn(event: MobEvent.Spawn.SkyblockMob) {
        if (!isEnabled()) return
        if (event.mob.name != "Farm Mooshroom") return
        event.mob.highlight(LorenzColor.AQUA.toColor())
        mobSet += event.mob
    }

    @SubscribeEvent
    fun onMobDeSpawn(event: MobEvent.DeSpawn.SkyblockMob) {
        mobSet -= event.mob
    }

    @SubscribeEvent
    fun onWorldChange(event: LorenzWorldChangeEvent) {
        mobSet.clear()
    }

    @SubscribeEvent
    fun onRenderWorld(event: LorenzRenderWorldEvent) {
        if (!isEnabled()) return
        mobSet.filter { !it.baseEntity.isDead }.forEach {
            val pos = event.exactLocation(it.baseEntity).add(y = 0.75)
            event.drawSphereInWorld(
                config.color.toChromaColor(),
                pos,
                1.5f
            )
        }
    }

    private fun isEnabled() = IslandType.THE_FARMING_ISLANDS.isInIsland() && config.enabled

}
