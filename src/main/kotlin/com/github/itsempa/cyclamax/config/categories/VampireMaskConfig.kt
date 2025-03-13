package com.github.itsempa.cyclamax.config.categories

import at.hannibal2.skyhanni.config.core.config.Position
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigEditorBoolean
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigLink
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigOption
import com.google.gson.annotations.Expose

class VampireMaskConfig {
    @Expose
    @ConfigOption(
        name = "Vampire Mask", desc = "Show how many bats are currently spawned\n" +
                "§c(may be inaccurate if multiple players are using vampire/witch mask)"
    )
    @ConfigEditorBoolean
    var enabled: Boolean = true

    @Expose
    @ConfigOption(name = "Highlight Bats", desc = "yeah idk, it just highlights bats from vamp mask")
    @ConfigEditorBoolean
    var highlight: Boolean = false

    @Expose
    @ConfigLink(owner = VampireMaskConfig::class, field = "enabled")
    var position: Position = Position(260, -15)
}
