package com.github.itsempa.cyclamax.config.categories

import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigEditorBoolean
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigEditorSlider
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigOption
import com.google.gson.annotations.Expose

class MiscConfig {
    @Expose
    @ConfigOption(name = "Spin", desc = "weeeeeeee")
    @ConfigEditorBoolean
    var spin: Boolean = false

    @Expose
    @ConfigOption(name = "Spin Speed", desc = "How fast you spin")
    @ConfigEditorSlider(minValue = -100f, minStep = 1f, maxValue = 100f)
    var spinSpeed: Int = 50

    @Expose
    @ConfigOption(name = "Stop Sword Blocking", desc = "makes it so that you dont get the block sword animation")
    @ConfigEditorBoolean
    var stopSwordBlocking: Boolean = false

    @Expose
    @ConfigOption(
        name = "Remove Armor Enchant Glint",
        desc = "removes enchant glint only from armor (worn or in item form)"
    )
    @ConfigEditorBoolean
    var removeArmorGlint: Boolean = false
}
