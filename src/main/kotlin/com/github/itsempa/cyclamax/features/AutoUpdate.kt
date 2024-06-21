package com.github.itsempa.cyclamax.features

import at.hannibal2.skyhanni.events.LorenzTickEvent
import at.hannibal2.skyhanni.utils.ChatUtils
import com.github.itsempa.cyclamax.CyclaMax
import io.github.moulberry.notenoughupdates.util.ApiUtil
import io.github.moulberry.notenoughupdates.util.MinecraftExecutor
import moe.nea.libautoupdate.*
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.CompletableFuture
import javax.net.ssl.HttpsURLConnection

class AutoUpdate {
    private val updater =
        UpdateContext(
            UpdateSource.githubUpdateSource("ItsEmpa", "CyclaMax"),
            UpdateTarget.deleteAndSaveInTheSameFolder(AutoUpdate::class.java),
            CurrentVersion.ofTag(CyclaMax.version),
            CyclaMax.MOD_ID,
        )

    init {
        updater.cleanup()
        UpdateUtils.patchConnection {
            if (it is HttpsURLConnection) {
                ApiUtil.patchHttpsRequest(it)
            }
        }
    }

    private var potentialUpdate = updater.checkUpdate("upstream")
    private var shouldNotify = CyclaMax.config.misc.autoUpdates

    @SubscribeEvent
    fun onTick(event: LorenzTickEvent) {
        if (!shouldNotify) return
        Minecraft.getMinecraft().thePlayer ?: return

        val update = potentialUpdate.getNow(null) ?: return
        MinecraftForge.EVENT_BUS.unregister(this)
        if (!update.isUpdateAvailable) return

        ChatUtils.clickableChat(
            "§e[CyclaMax] §aUpdate available §6(${update.update.versionName})§a! Click here to download.",
            prefix = false,
            onClick = {
                ChatUtils.chat("§e[CyclaMax] §aDownloading update...", prefix = false)

                CompletableFuture.supplyAsync { update.prepareUpdate() }.thenAcceptAsync(
                    {
                        update.executePreparedUpdate()
                        ChatUtils.chat(
                            "§e[CyclaMax] §aUpdate downloaded! Restart your game to apply changes.",
                            prefix = false,
                        )
                    },
                    MinecraftExecutor.OnThread,
                )
            },
        )
    }
}
