package com.github.itsempa.cyclamax.mixins.hooks

import at.hannibal2.skyhanni.utils.LorenzUtils
import com.github.itsempa.cyclamax.CyclaMax
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer

object SpinnyHook {
    private val config get() = CyclaMax.config.misc

    @JvmStatic
    fun rotatePlayer(player: EntityPlayer) {
        if (!config.spin) return
        val name = player.name ?: return
        if (name != LorenzUtils.getPlayerName()) return
        val speed = 20 * (config.spinSpeed / 100.0)
        val rotation = ((player.ticksExisted % 90) * speed).toFloat()
        GlStateManager.rotate(rotation, 0f, 1f, 0f)
    }
}
