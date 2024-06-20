package com.github.itsempa.cyclamax.features

import at.hannibal2.skyhanni.data.mob.Mob
import at.hannibal2.skyhanni.events.GuiRenderEvent
import at.hannibal2.skyhanni.events.MobEvent
import at.hannibal2.skyhanni.utils.LorenzUtils
import at.hannibal2.skyhanni.utils.RenderUtils.renderString
import com.github.itsempa.cyclamax.CyclaMax
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class VampireMask {
    private val config get() = CyclaMax.config.vampireMask

    private val bats = mutableSetOf<Mob>()

    @SubscribeEvent
    fun onMobSpawn(event: MobEvent.Spawn.Projectile) {
        if (event.mob.name == "Vampire Mask Bat") bats += event.mob
    }

    @SubscribeEvent
    fun onMobDeSpawn(event: MobEvent.DeSpawn.Projectile) {
        bats -= event.mob
    }

    @SubscribeEvent
    fun onRenderOverlay(event: GuiRenderEvent.GuiOverlayRenderEvent) {
        if (!isEnabled()) return
        config.position.renderString("§aBats: §b${bats.size}", posLabel = "Vampire Mask Bats")
    }

    private fun isEnabled() = LorenzUtils.inSkyBlock && config.enabled
}
