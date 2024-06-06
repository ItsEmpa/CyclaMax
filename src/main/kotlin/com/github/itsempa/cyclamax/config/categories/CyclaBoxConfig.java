package com.github.itsempa.cyclamax.config.categories;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class CyclaBoxConfig {

    @Expose
    @ConfigOption(name = "Cycla Highlight", desc = "cycla mobs thing highlight idk")
    @ConfigEditorBoolean
    public boolean enabled = true;

    @Expose
    @ConfigOption(name = "Sphere color", desc = "idk color of sphere")
    @ConfigEditorColour
    public String color = "0:100:255:85:255";

}
