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
    @ConfigEditorSlider(minValue = -100, minStep = 1, maxValue = 100)
    public int spinSpeed = 50;

    @Expose
    @ConfigOption(name = "Stop Sword Blocking", desc = "makes it so that you dont get the block sword animation")
    @ConfigEditorBoolean
    public boolean stopSwordBlocking = false;

    @Expose
    @ConfigOption(name = "Remove Armor Enchant Glint", desc = "removes enchant glint only from armor (worn or in item form)")
    @ConfigEditorBoolean
    public boolean removeArmorGlint = false;
}
