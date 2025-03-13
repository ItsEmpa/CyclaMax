package com.github.itsempa.cyclamax.mixins.transformers;

import com.github.itsempa.cyclamax.mixins.hooks.ArmorGlintHook;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class MixinRenderItem {

    @Unique
    private ItemStack cyclamax$lastRenderedItem = null;

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At("HEAD")
    )
    private void onRenderItem(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        cyclamax$lastRenderedItem = stack;
    }

    @Inject(method = "renderEffect", at = @At("HEAD"), cancellable = true)
    private void onRenderEffect(IBakedModel model, CallbackInfo ci) {
        if (ArmorGlintHook.shouldHideGlint(cyclamax$lastRenderedItem)) ci.cancel();
    }
}
