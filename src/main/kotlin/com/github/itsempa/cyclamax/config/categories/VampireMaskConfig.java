package com.github.itsempa.cyclamax.config.categories;

import at.hannibal2.skyhanni.config.core.config.Position;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigLink;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

public class VampireMaskConfig {

    @Expose
    @ConfigOption(
            name = "Vampire Mask",
            desc = "Show how many bats are currently spawned\n" +
                    "§c(may be inaccurate if multiple players are using vampire/witch mask)"
    )
    @ConfigEditorBoolean
    public boolean enabled = true;

    @Expose
    @ConfigLink(owner = VampireMaskConfig.class, field = "enabled")
    public Position position = new Position(260, -15);


}
