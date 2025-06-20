package com.github.itsempa.cyclamax.config.categories

import at.hannibal2.skyhanni.deps.moulconfig.ChromaColour
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigEditorBoolean
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigEditorColour
import at.hannibal2.skyhanni.deps.moulconfig.annotations.ConfigOption
import at.hannibal2.skyhanni.utils.LorenzColor
import com.google.gson.annotations.Expose

class CyclaBoxConfig {
    @Expose
    @ConfigOption(name = "Cycla Highlight", desc = "Highlight funny desert cows with a funny sphere")
    @ConfigEditorBoolean
    var enabled: Boolean = true

    @Expose
    @ConfigOption(name = "Line to nearest", desc = "shows a line to the nearest visible funny cow")
    @ConfigEditorBoolean
    var lineToNearest: Boolean = false

    @Expose
    @ConfigOption(name = "Sphere color", desc = "coloUr of sphere")
    @ConfigEditorColour
    var color = LorenzColor.LIGHT_PURPLE.toChromaColor()
}
