package com.github.itsempa.cyclamax.features

import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.LorenzRenderWorldEvent
import at.hannibal2.skyhanni.events.LorenzTickEvent
import at.hannibal2.skyhanni.events.LorenzWorldChangeEvent
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
import net.minecraft.entity.passive.EntityMooshroom
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object CyclaBox {
    private val config get() = CyclaMax.config.cyclaBox

    private var entities = mutableSetOf<EntityMooshroom>()

    @SubscribeEvent
    fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
        val entity = event.entity as? EntityMooshroom ?: return
        entities += entity
    }

    @SubscribeEvent
    fun onWorldChange(event: LorenzWorldChangeEvent) {
        entities.clear()
    }

    @SubscribeEvent
    fun onTick(event: LorenzTickEvent) {
        if (!isEnabled()) return
        entities = entities.filter {
            if (it.isDead || it.health == 0F) {
                // TODO: make an event
                KillsCounter.killMushroom(it)
                false
            } else true
        }.toMutableSet()
    }

    @SubscribeEvent
    fun onRenderWorld(event: LorenzRenderWorldEvent) {
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
