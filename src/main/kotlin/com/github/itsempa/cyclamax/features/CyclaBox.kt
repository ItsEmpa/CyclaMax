package com.github.itsempa.cyclamax.features

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.entity.EntityEnterWorldEvent
import at.hannibal2.skyhanni.events.minecraft.SkyHanniRenderWorldEvent
import at.hannibal2.skyhanni.events.minecraft.SkyHanniTickEvent
import at.hannibal2.skyhanni.events.minecraft.WorldChangeEvent
import at.hannibal2.skyhanni.utils.ColorUtils.addAlpha
import at.hannibal2.skyhanni.utils.ColorUtils.toChromaColor
import at.hannibal2.skyhanni.utils.EntityUtils.canBeSeen
import at.hannibal2.skyhanni.utils.LocationUtils.distanceToPlayer
import at.hannibal2.skyhanni.utils.LorenzUtils.isInIsland
import at.hannibal2.skyhanni.utils.RenderUtils.draw3DLine
import at.hannibal2.skyhanni.utils.RenderUtils.drawSphereInWorld
import at.hannibal2.skyhanni.utils.RenderUtils.exactLocation
import at.hannibal2.skyhanni.utils.RenderUtils.exactPlayerEyeLocation
import com.github.itsempa.cyclamax.CyclaMax
import com.github.itsempa.cyclamax.modules.Module
import net.minecraft.entity.passive.EntityMooshroom

@Module
object CyclaBox {
    private val config get() = CyclaMax.feature.cyclaBox

    private val entities = mutableSetOf<EntityMooshroom>()

    @HandleEvent
    fun onEntityJoinWorld(event: EntityEnterWorldEvent<EntityMooshroom>) {
        entities += event.entity
    }

    @HandleEvent
    fun onWorldChange(event: WorldChangeEvent) {
        entities.clear()
    }

    @HandleEvent
    fun onTick(event: SkyHanniTickEvent) {
        if (!isEnabled()) return
        entities.removeIf {
            if (it.isDead || it.health == 0F) {
                // TODO: make an event
                KillsCounter.killMushroom(it)
                false
            } else true
        }
    }

    @HandleEvent
    fun onRenderWorld(event: SkyHanniRenderWorldEvent) {
        if (!isEnabled()) return
        val color = config.color.toChromaColor()
        entities
            .filter { !it.isDead }
            .sortedBy { if (it.canBeSeen(50.0)) it.distanceToPlayer() else Double.MAX_VALUE }
            .forEachIndexed { index, entity ->
                val pos = event.exactLocation(entity).add(y = 0.75)
                if (index == 0 && config.lineToNearest && entity.canBeSeen(50.0)) {
                    event.draw3DLine(
                        event.exactPlayerEyeLocation(),
                        pos,
                        color.addAlpha(255),
                        3,
                        true,
                    )
                }
                event.drawSphereInWorld(
                    color,
                    pos,
                    1.5f,
                )
            }
    }

    private fun isEnabled() = IslandType.THE_FARMING_ISLANDS.isInIsland() && config.enabled

}
