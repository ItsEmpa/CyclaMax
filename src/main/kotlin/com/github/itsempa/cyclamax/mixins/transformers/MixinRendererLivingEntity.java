package com.github.itsempa.cyclamax.mixins.transformers;

import com.github.itsempa.cyclamax.mixins.hooks.SpinnyHook;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RendererLivingEntity.class)
public class MixinRendererLivingEntity<T extends EntityLivingBase> {

    @Inject(method = "rotateCorpse", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumChatFormatting;getTextWithoutFormattingCodes(Ljava/lang/String;)Ljava/lang/String;", shift = At.Shift.AFTER))
    private void rotateThePlayer(T bat, float p_77043_2_, float p_77043_3_, float partialTicks, CallbackInfo ci) {
        if (bat instanceof EntityPlayer) SpinnyHook.rotatePlayer((EntityPlayer) bat, partialTicks);
    }

}
