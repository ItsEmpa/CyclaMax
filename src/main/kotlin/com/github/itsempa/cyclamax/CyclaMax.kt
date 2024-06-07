package com.github.itsempa.cyclamax

import com.github.itsempa.cyclamax.commands.ConfigCommand
import com.github.itsempa.cyclamax.config.categories.Features
import com.github.itsempa.cyclamax.features.cyclabox.CyclaBox
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import java.io.File

@Mod(
    modid = CyclaMax.MOD_ID,
    clientSideOnly = true,
    useMetadata = true,
    version = "0.0.1-indev",
)
class CyclaMax {

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        loadModule(CyclaBox())
        ClientCommandHandler.instance.registerCommand(ConfigCommand)
    }

    private fun loadModule(obj: Any) {
        MinecraftForge.EVENT_BUS.register(obj)
    }

    companion object {
        const val MOD_ID = "cyclamax"

        @JvmStatic
        val version: String
            get() = Loader.instance().indexedModList[MOD_ID]!!.version

        val managedConfig by lazy { ManagedConfig.create(File("config/cyclamax/config.json"), Features::class.java) }
        val config = managedConfig.instance
    }
}
