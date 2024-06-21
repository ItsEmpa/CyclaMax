package com.github.itsempa.cyclamax

import at.hannibal2.skyhanni.events.SecondPassedEvent
import com.github.itsempa.cyclamax.commands.ConfigCommand
import com.github.itsempa.cyclamax.commands.SpinCommand
import com.github.itsempa.cyclamax.config.categories.Features
import com.github.itsempa.cyclamax.features.CyclaBox
import com.github.itsempa.cyclamax.features.VampireMask
import io.github.notenoughupdates.moulconfig.managed.ManagedConfig
import net.minecraft.command.ICommand
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.File

@Mod(
    modid = CyclaMax.MOD_ID,
    clientSideOnly = true,
    useMetadata = true,
    version = "0.0.3",
)
class CyclaMax {

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        loadModule(CyclaBox())
        loadModule(VampireMask())

        loadCommand(SpinCommand)
        loadCommand(ConfigCommand)
    }

    @SubscribeEvent
    fun onSecondPassed(event: SecondPassedEvent) {
        if (event.repeatSeconds(60)) {
            managedConfig.saveToFile()
        }
    }

    private fun loadModule(obj: Any) {
        MinecraftForge.EVENT_BUS.register(obj)
    }

    private fun loadCommand(obj: ICommand) {
        ClientCommandHandler.instance.registerCommand(obj)
    }

    companion object {
        const val MOD_ID = "cyclamax"

        @JvmStatic
        val version: String
            get() = Loader.instance().indexedModList[MOD_ID]!!.version

        val managedConfig by lazy { ManagedConfig.create(File("config/cyclamax/config.json"), Features::class.java) }
        val config get() = managedConfig.instance
    }
}
