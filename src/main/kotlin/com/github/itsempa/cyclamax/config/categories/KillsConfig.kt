package com.github.itsempa.cyclamax.config.categories

import at.hannibal2.skyhanni.config.core.config.Position
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigEditorBoolean
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigLink
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigOption
import com.google.gson.annotations.Expose

class KillsConfig {
    @Expose
    @ConfigOption(name = "Kills counter", desc = "Show the amount of mooshroom cow kills you have")
    @ConfigEditorBoolean
    var enabled: Boolean = true

    @Expose
    var kills: Long = 0

    @Expose
    @ConfigLink(owner = KillsConfig::class, field = "enabled")
    var position: Position = Position(260, -15)
}
