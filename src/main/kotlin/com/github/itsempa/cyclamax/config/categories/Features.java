package com.github.itsempa.cyclamax.config.categories;

import com.github.itsempa.cyclamax.CyclaMax;
import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.Config;
import io.github.notenoughupdates.moulconfig.annotations.Category;

public class Features extends Config {

    @Override
    public String getTitle() {
        return "CyclaMax " + CyclaMax.getVersion() + " by §cEmpa";
    }

    @Override
    public void saveNow() {
        CyclaMax.Companion.getManagedConfig().saveToFile();
    }

    @Expose
    @Category(name = "Cycla Box", desc = "")
    public CyclaBoxConfig cyclaBox = new CyclaBoxConfig();

    @Expose
    @Category(name = "Vampire Mask", desc = "")
    public VampireMaskConfig vampireMask = new VampireMaskConfig();

    @Expose
    @Category(name = "Kills Counter", desc = "")
    public KillsConfig killsCounter = new KillsConfig();

    @Expose
    @Category(name = "Misc", desc = "")
    public MiscConfig misc = new MiscConfig();
}
