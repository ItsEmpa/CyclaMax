package com.github.itsempa.cyclamax.config.categories;

import at.hannibal2.skyhanni.config.core.config.Position;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigLink;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class KillsConfig {

    @Expose
    @ConfigOption(name = "Kills counter", desc = "Show the amount of mooshroom cow kills you have")
    @ConfigEditorBoolean
    public boolean enabled = true;

    @Expose
    public long kills = 0;

    @Expose
    @ConfigLink(owner = KillsConfig.class, field = "enabled")
    public Position position = new Position(260, -15);
}
