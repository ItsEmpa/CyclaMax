package com.github.itsempa.cyclamax.events

import at.hannibal2.skyhanni.api.event.SkyHanniEvent
import at.hannibal2.skyhanni.config.commands.CommandBuilder
import com.github.itsempa.cyclamax.commands.CyclaMaxCommands
import net.minecraftforge.client.ClientCommandHandler

object CyclaMaxCommandRegistrationEvent : SkyHanniEvent() {
    fun register(name: String, block: CommandBuilder.() -> Unit) {
        val info = CommandBuilder(name).apply(block)
        if (CyclaMaxCommands.commandsList.any { it.name == name }) {
            error("The command '$name is already registered!'")
        }
        ClientCommandHandler.instance.registerCommand(info.toSimpleCommand())
        CyclaMaxCommands.commandsList.add(info)
    }
}
