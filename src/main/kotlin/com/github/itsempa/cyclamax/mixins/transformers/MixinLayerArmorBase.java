package com.github.itsempa.cyclamax.mixins.transformers;

import com.github.itsempa.cyclamax.CyclaMax;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public class MixinLayerArmorBase<T extends ModelBase>{
    @Inject(method = "renderGlint", at = @At("HEAD"), cancellable = true)
    private void onRenderGlint(
            EntityLivingBase e,
            T m,
            float p_177183_3_,
            float p_177183_4_,
            float partialTicks,
            float p_177183_6_,
            float p_177183_7_,
            float p_177183_8_,
            float scale,
            CallbackInfo ci
    ) {
        if (CyclaMax.getConfig().misc.removeArmorGlint) ci.cancel();
    }
}
