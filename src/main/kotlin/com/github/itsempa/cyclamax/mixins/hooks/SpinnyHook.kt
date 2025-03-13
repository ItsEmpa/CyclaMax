package com.github.itsempa.cyclamax.mixins.hooks

import at.hannibal2.skyhanni.utils.LorenzUtils
import com.github.itsempa.cyclamax.CyclaMax
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer

object SpinnyHook {
    private val config get() = CyclaMax.feature.misc

    @JvmStatic
    fun rotatePlayer(
        player: EntityPlayer,
        partialTicks: Float) {
        if (!config.spin) return
        val name = player.name ?: return
        if (name != LorenzUtils.getPlayerName()) return
        val spinsPerMinute = config.spinSpeed
        val spinsPerSecond = spinsPerMinute / 60.0
        val degreesPerSecond = spinsPerSecond * 360.0
        val degreesPerTick = degreesPerSecond / 20.0

        val ticksExisted = player.ticksExisted + partialTicks

        val rotation = (ticksExisted * degreesPerTick).toFloat() % 360 + 180
        GlStateManager.rotate(rotation, 0f, 1f, 0f)
    }
}
