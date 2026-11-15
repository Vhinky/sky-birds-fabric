package com.vhinky.skybirds.mixin;

import com.vhinky.skybirds.client.Startle;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Каждый проигрываемый звук проверяем: не испугает ли он птиц (взрыв, гром, рог…). */
@Mixin(SoundManager.class)
public abstract class SoundManagerMixin {
    @Inject(method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V", at = @At("HEAD"))
    private void skyBirds$onPlay(SoundInstance sound, CallbackInfo ci) {
        Startle.onSound(sound);
    }
}
