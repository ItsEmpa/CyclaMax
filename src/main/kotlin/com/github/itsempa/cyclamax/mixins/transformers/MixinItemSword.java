package com.github.itsempa.cyclamax.mixins.transformers;

import com.github.itsempa.cyclamax.CyclaMax;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemSword.class)
public class MixinItemSword {

    @Inject(method = "getItemUseAction", at = @At("HEAD"), cancellable = true)
    private void onGetItemUseAction(ItemStack stack, CallbackInfoReturnable<EnumAction> cir) {
        if (CyclaMax.getConfig().misc.stopSwordBlocking) cir.setReturnValue(EnumAction.NONE);
    }

    @Inject(method = "getMaxItemUseDuration", at = @At("HEAD"), cancellable = true)
    private void onGetMaxItemUseDuration(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (CyclaMax.getConfig().misc.stopSwordBlocking) cir.setReturnValue(0);
    }

    @Inject(method = "onItemRightClick", at = @At("HEAD"), cancellable = true)
    private void onOnItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, CallbackInfoReturnable<ItemStack> cir) {
        if (CyclaMax.getConfig().misc.stopSwordBlocking) cir.setReturnValue(itemStackIn);
    }
}
