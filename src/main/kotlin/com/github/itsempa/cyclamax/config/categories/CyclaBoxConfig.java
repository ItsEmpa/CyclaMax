package com.github.itsempa.cyclamax.config.categories;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class CyclaBoxConfig {

    @Expose
    @ConfigOption(name = "Cycla Highlight", desc = "Highlight funny desert cows with a funny sphere")
    @ConfigEditorBoolean
    public boolean enabled = true;

    @Expose
    @ConfigOption(name = "Sphere color", desc = "coloUr of sphere")
    @ConfigEditorColour
    public String color = "0:100:255:85:255";

}
