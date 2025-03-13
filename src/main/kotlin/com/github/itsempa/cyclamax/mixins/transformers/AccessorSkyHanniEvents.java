package com.github.itsempa.cyclamax.mixins.transformers;

import at.hannibal2.skyhanni.api.event.SkyHanniEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.lang.reflect.Method;

@Mixin(SkyHanniEvents.class)
public interface AccessorSkyHanniEvents {

    @SuppressWarnings("unused")
    @Invoker("registerMethod")
    void cyclamax$registerMethod(Method method, Object instance);

}
