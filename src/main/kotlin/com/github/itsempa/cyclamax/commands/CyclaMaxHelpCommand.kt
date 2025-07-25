package com.github.itsempa.cyclamax.commands

import at.hannibal2.skyhanni.config.commands.CommandBuilder
import at.hannibal2.skyhanni.utils.StringUtils.splitLines
import at.hannibal2.skyhanni.utils.chat.TextHelper
import at.hannibal2.skyhanni.utils.compat.hover
import at.hannibal2.skyhanni.utils.compat.suggest
import com.github.itsempa.cyclamax.CyclaMax
import net.minecraft.util.IChatComponent

object CyclaMaxHelpCommand {

    private const val COMMANDS_PER_PAGE = 15
    private val HELP_ID = CyclaMax.MOD_ID.hashCode()

    private fun createCommandEntry(command: CommandBuilder): IChatComponent {
        val category = command.category
        val color = category.color
        val description = command.description.splitLines(200).replace("§r", "§7")
        val categoryDescription = category.description.replace("SkyHanni", CyclaMax.MOD_NAME).splitLines(200).replace("§r", "§7")

        return TextHelper.text("§7 - $color${command.name}") {
            this.hover = TextHelper.multiline(
                "§e/${command.name}",
                if (description.isNotEmpty()) description.prependIndent("  ") else null,
                "",
                "$color§l${category.categoryName}",
                categoryDescription.prependIndent("  "),
            )
            this.suggest = "/${command.name}"
        }
    }

    private fun showPage(page: Int, search: String, commands: List<CommandBuilder>) {
        val filtered = commands.filter {
            it.name.contains(search, ignoreCase = true) || it.description.contains(search, ignoreCase = true)
        }

        val title = "${CyclaMax.MOD_NAME} Commands" + if (search.isNotBlank()) "Matching: \"$search\"" else ""

        TextHelper.displayPaginatedList(
            title,
            filtered,
            chatLineId = HELP_ID,
            emptyMessage = "No commands found.",
            currentPage = page,
            maxPerPage = COMMANDS_PER_PAGE,
        ) { createCommandEntry(it) }
    }

    fun onCommand(args: Array<String>) {
        val page: Int
        val search: String
        if (args.firstOrNull() == "-p") {
            page = args.getOrNull(1)?.toIntOrNull() ?: 1
            search = args.drop(2).joinToString(" ")
        } else {
            page = 1
            search = args.joinToString(" ")
        }
        showPage(page, search, CyclaMaxCommands.commandsList)
    }
}
