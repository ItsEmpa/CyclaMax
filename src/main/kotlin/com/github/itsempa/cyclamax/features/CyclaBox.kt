package com.github.itsempa.cyclamax.features

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.entity.EntityEnterWorldEvent
import at.hannibal2.skyhanni.events.entity.EntityLeaveWorldEvent
import at.hannibal2.skyhanni.events.minecraft.SkyHanniRenderWorldEvent
import at.hannibal2.skyhanni.events.minecraft.SkyHanniTickEvent
import at.hannibal2.skyhanni.events.minecraft.WorldChangeEvent
import at.hannibal2.skyhanni.utils.ColorUtils.addAlpha
import at.hannibal2.skyhanni.utils.EntityUtils.canBeSeen
import at.hannibal2.skyhanni.utils.LocationUtils.distanceToPlayer
import at.hannibal2.skyhanni.utils.RenderUtils.drawLineToEye
import at.hannibal2.skyhanni.utils.RenderUtils.drawSphereInWorld
import at.hannibal2.skyhanni.utils.RenderUtils.exactLocation
import at.hannibal2.skyhanni.utils.SpecialColor.toSpecialColor
import com.github.itsempa.cyclamax.CyclaMax
import com.github.itsempa.cyclamax.modules.Module
import com.github.itsempa.cyclamax.utils.CyclaMaxUtils.minByNullableOrNull
import net.minecraft.entity.passive.EntityMooshroom

@Module
object CyclaBox {
    private val config get() = CyclaMax.feature.cyclaBox

    private val entities = mutableSetOf<EntityMooshroom>()
    private var closest: EntityMooshroom? = null

    @HandleEvent(onlyOnIsland = IslandType.THE_FARMING_ISLANDS)
    fun onEntityJoinWorld(event: EntityEnterWorldEvent<EntityMooshroom>) = entities.add(event.entity)

    @HandleEvent(onlyOnIsland = IslandType.THE_FARMING_ISLANDS)
    fun onEntityLeaveWorld(event: EntityLeaveWorldEvent<EntityMooshroom>) {
        val entity = event.entity
        // TODO: make an event
        KillsCounter.killMushroom(entity)
        entities.remove(entity)
        if (entity == closest) calculateClosest()
    }

    @HandleEvent
    fun onWorldChange(event: WorldChangeEvent) = entities.clear()

    private fun calculateClosest(): EntityMooshroom? {
        closest = entities.minByNullableOrNull { if (it.canBeSeen(50.0)) it.distanceToPlayer() else null }
        return closest
    }

    @HandleEvent(onlyOnIsland = IslandType.THE_FARMING_ISLANDS)
    fun onTick(event: SkyHanniTickEvent) {
        if (!isEnabled()) return
        entities.removeIf {
            if (it.isDead || it.health == 0F) {
                // TODO: make an event
                KillsCounter.killMushroom(it)
                false
            } else true
        }
        calculateClosest()
    }

    @HandleEvent(onlyOnIsland = IslandType.THE_FARMING_ISLANDS)
    fun onRenderWorld(event: SkyHanniRenderWorldEvent) {
        if (!isEnabled()) return
        val color = config.color.toSpecialColor()

        if (entities.isEmpty()) return
        val first = closest?.takeIf { !it.isDead } ?: calculateClosest() ?: return

        if (config.lineToNearest && first.canBeSeen(50.0)) {
            event.drawLineToEye(
                event.exactLocation(first).add(y = 0.75),
                color.addAlpha(255),
                3,
                true,
            )
        }

        for (entity in entities) {
            if (!entity.canBeSeen(50.0)) continue
            val pos = event.exactLocation(entity).up(0.75)
            event.drawSphereInWorld(
                color,
                pos,
                1.5f,
            )
        }
    }

    private fun isEnabled() = config.enabled

}
