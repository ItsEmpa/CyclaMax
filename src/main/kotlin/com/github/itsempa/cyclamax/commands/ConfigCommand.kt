package com.github.itsempa.cyclamax.commands

import com.github.itsempa.cyclamax.CyclaMax
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

object ConfigCommand : CommandBase() {
    override fun getCommandName(): String = "cyclamax"
    override fun getCommandAliases(): List<String> = listOf("cm")
    override fun canCommandSenderUseCommand(sender: ICommandSender?): Boolean = true
    override fun getCommandUsage(sender: ICommandSender): String = "/cm"

    override fun processCommand(sender: ICommandSender, args: Array<out String>) {
        CyclaMax.managedConfig.openConfigGui()
    }

}
