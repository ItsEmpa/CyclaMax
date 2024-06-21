package com.github.itsempa.cyclamax.commands

import at.hannibal2.skyhanni.utils.ChatUtils
import com.github.itsempa.cyclamax.CyclaMax
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

object SpinCommand : CommandBase() {
    override fun getCommandName(): String = "spin"

    override fun getCommandAliases(): List<String> = listOf("spin")

    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean = true

    override fun getCommandUsage(sender: ICommandSender): String = "/spin help"

    override fun processCommand(
        sender: ICommandSender,
        args: Array<out String>,
    ) {
        if (args.isEmpty()) {
            val newValue = !CyclaMax.config.misc.spin
            CyclaMax.config.misc.spin = newValue
            val text = if (newValue) "§aenabled" else "§cdisabled"
            ChatUtils.chat("§e[CyclaMax] §aSet spin to $text!", prefix = false)
            return
        } else {
            val number =
                args[0].runCatching { toInt() }.getOrNull()?.coerceIn(1..100)
                    ?: run {
                        ChatUtils.chat("§e[CyclaMax] §cInvalid number!", prefix = false)
                        return
                    }
            ChatUtils.chat("§e[CyclaMax] §aSet spin speed to $number!", prefix = false)
            CyclaMax.config.misc.spinSpeed = number
        }
    }
}
