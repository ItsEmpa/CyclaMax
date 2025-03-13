package com.github.itsempa.cyclamax.commands

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.config.commands.CommandBuilder
import at.hannibal2.skyhanni.config.commands.CommandCategory
import at.hannibal2.skyhanni.data.GuiEditManager
import at.hannibal2.skyhanni.deps.moulconfig.gui.GuiScreenElementWrapper
import at.hannibal2.skyhanni.utils.ChatUtils
import com.github.itsempa.cyclamax.events.CyclaMaxCommandRegistrationEvent
import com.github.itsempa.cyclamax.CyclaMax
import com.github.itsempa.cyclamax.modules.Module

@Module
object CyclaMaxCommands {

    private val spinConfig get() = CyclaMax.feature.misc

    private fun getOpenMainMenu(args: Array<String>) {
        if (args.isNotEmpty()) {
            if (args[0].lowercase() == "gui") {
                GuiEditManager.openGuiPositionEditor(hotkeyReminder = true)
            } else openConfigGui(args.joinToString(" "))
        } else openConfigGui()
    }

    val commandsList = mutableListOf<CommandBuilder>()

    @HandleEvent
    fun onCommandRegistration(event: CyclaMaxCommandRegistrationEvent) {
        event.register("cyclamax") {
            this.aliases = listOf("cm")
            this.category = CommandCategory.MAIN
            this.description = "Opens the main ${CyclaMax.MOD_NAME} config"
            callback(::getOpenMainMenu)
        }
        event.register("cmcommands") {
            this.aliases = listOf("cmhelp")
            this.description = "Shows this list"
            this.category = CommandCategory.MAIN
            callback(CyclaMaxHelpCommand::onCommand)
        }
        event.register("cmsaveconfig") {
            this.description = "Saves the config"
            this.category = CommandCategory.DEVELOPER_TEST
            callback { CyclaMax.managedConfig.saveToFile() }
        }
        event.register("spin") {
            this.description = "Spin the player"
            this.category = CommandCategory.DEVELOPER_TEST
            callback { args ->
                if (args.isEmpty()) {
                    val newValue = !spinConfig.spin
                    spinConfig.spin = newValue
                    val text = if (newValue) "§aenabled" else "§cdisabled"
                    ChatUtils.chat("§e[CyclaMax] §aSet spin to $text!", prefix = false)
                    return@callback
                } else {
                    val number =
                        args[0].runCatching { toInt() }.getOrNull()?.coerceIn(-500..500)
                            ?: run {
                                ChatUtils.chat("§e[CyclaMax] §cInvalid number!", prefix = false)
                                return@callback
                            }
                    ChatUtils.chat("§e[CyclaMax] §aSet spin speed to $number!", prefix = false)
                    if (!spinConfig.spin) spinConfig.spin = true
                    spinConfig.spinSpeed = number
                }
            }
        }
    }

    private fun openConfigGui(search: String? = null) {
        val editor = CyclaMax.managedConfig.getEditor()

        search?.let { editor.search(search) }
        SkyHanniMod.screenToOpen = GuiScreenElementWrapper(editor)
    }
}
