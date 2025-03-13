package com.github.itsempa.cyclamax.mixins.hooks

import com.github.itsempa.cyclamax.CyclaMax
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack

object ArmorGlintHook {
    private val config get() = CyclaMax.feature.misc

    @JvmStatic
    fun shouldHideGlint(item: ItemStack?): Boolean = config.removeArmorGlint && item?.item is ItemArmor
}
