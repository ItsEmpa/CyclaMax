package com.github.itsempa.cyclamax.config.categories

import at.hannibal2.skyhanni.deps.moulconfig.Config
import at.hannibal2.skyhanni.deps.moulconfig.annotations.Category
import com.github.itsempa.cyclamax.CyclaMax
import com.github.itsempa.cyclamax.CyclaMax.managedConfig
import com.google.gson.annotations.Expose

class Features : Config() {
    override fun getTitle(): String = "CyclaMax ${CyclaMax.VERSION} by §cEmpa"

    override fun saveNow() {
        managedConfig.saveToFile()
    }

    override fun shouldAutoFocusSearchbar(): Boolean = true

    @Expose
    @Category(name = "Cycla Box", desc = "")
    var cyclaBox: CyclaBoxConfig = CyclaBoxConfig()

    @Expose
    @Category(name = "Vampire Mask", desc = "")
    var vampireMask: VampireMaskConfig = VampireMaskConfig()

    @Expose
    @Category(name = "Kills Counter", desc = "")
    var killsCounter: KillsConfig = KillsConfig()

    @Expose
    @Category(name = "Misc", desc = "")
    var misc: MiscConfig = MiscConfig()
}
