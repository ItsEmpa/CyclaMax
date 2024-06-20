package com.github.itsempa.cyclamax.features

import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.LorenzRenderWorldEvent
import at.hannibal2.skyhanni.events.LorenzWorldChangeEvent
import at.hannibal2.skyhanni.events.SecondPassedEvent
import at.hannibal2.skyhanni.utils.ColorUtils.toChromaColor
import at.hannibal2.skyhanni.utils.LorenzUtils.isInIsland
import at.hannibal2.skyhanni.utils.RenderUtils.drawSphereInWorld
import at.hannibal2.skyhanni.utils.RenderUtils.exactLocation
import com.github.itsempa.cyclamax.CyclaMax
import net.minecraft.entity.passive.EntityMooshroom
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class CyclaBox {
    private val config get() = CyclaMax.config.cyclaBox

    private val entities = mutableSetOf<EntityMooshroom>()

    @SubscribeEvent
    fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
        val entity = event.entity as? EntityMooshroom ?: return
        entities += entity
    }

    @SubscribeEvent
    fun onSecondPassed(event: SecondPassedEvent) {
        if (!isEnabled()) return
        entities.removeIf { it.isDead }
    }

    @SubscribeEvent
    fun onWorldChange(event: LorenzWorldChangeEvent) {
        entities.clear()
    }

    @SubscribeEvent
    fun onRenderWorld(event: LorenzRenderWorldEvent) {
        if (!isEnabled()) return
        entities.filter { !it.isDead }.forEach {
            val pos = event.exactLocation(it).add(y = 0.75)
            event.drawSphereInWorld(
                config.color.toChromaColor(),
                pos,
                1.5f,
            )
        }
    }

    private fun isEnabled() = IslandType.THE_FARMING_ISLANDS.isInIsland() && config.enabled

}
