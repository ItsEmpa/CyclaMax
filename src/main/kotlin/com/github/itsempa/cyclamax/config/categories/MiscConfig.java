package com.github.itsempa.cyclamax.config.categories;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class MiscConfig {

    @Expose
    @ConfigOption(name = "Spin", desc = "weeeeeeee")
    @ConfigEditorBoolean
    public boolean spin = false;

    @Expose
    @ConfigOption(name = "Spin Speed", desc = "How fast you spin")
    @ConfigEditorSlider(minValue = 1, minStep = 1, maxValue = 100)
    public int spinSpeed = 50;

}
