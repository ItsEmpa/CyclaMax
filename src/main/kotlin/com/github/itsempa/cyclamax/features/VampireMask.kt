package com.github.itsempa.cyclamax.features

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.data.mob.Mob
import at.hannibal2.skyhanni.events.GuiRenderEvent
import at.hannibal2.skyhanni.events.MobEvent
import at.hannibal2.skyhanni.events.minecraft.SkyHanniTickEvent
import at.hannibal2.skyhanni.utils.LorenzColor
import at.hannibal2.skyhanni.utils.LorenzUtils
import at.hannibal2.skyhanni.utils.LorenzVec
import at.hannibal2.skyhanni.utils.RenderUtils.renderString
import at.hannibal2.skyhanni.utils.TimeLimitedSet
import at.hannibal2.skyhanni.utils.getLorenzVec
import com.github.itsempa.cyclamax.CyclaMax
import com.github.itsempa.cyclamax.modules.Module
import kotlin.time.Duration.Companion.seconds

@Module
object VampireMask {
    private val config get() = CyclaMax.feature.vampireMask

    val batDeathLocations = TimeLimitedSet<LorenzVec>(2.seconds)

    private var bats = mutableSetOf<Mob>()

    @HandleEvent
    fun onMobSpawn(event: MobEvent.Spawn.Projectile) {
        val mob = event.mob
        if (mob.name == "Vampire Mask Bat") {
            bats += mob
            if (config.highlight) mob.highlight(LorenzColor.RED.addOpacity(100))
        }
    }

    @HandleEvent
    fun onTick(event: SkyHanniTickEvent) {
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

    @HandleEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        if (!isEnabled()) return
        config.position.renderString("§aBats: §b${bats.size}", posLabel = "Vampire Mask Bats")
    }

    private fun isEnabled() = LorenzUtils.inSkyBlock && config.enabled
}
