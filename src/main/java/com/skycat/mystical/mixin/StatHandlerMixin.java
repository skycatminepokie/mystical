package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatHandler.class)
public abstract class StatHandlerMixin {
    @Inject(method = "increaseStat", at = @At("HEAD"))
    public void mystical_statIncreased(PlayerEntity player, Stat<?> stat, int value, CallbackInfo ci) {
        if (!Mystical.isClientWorld()) {
            Mystical.getSpellHandler().onStatIncreased(player, stat, value);
        }
    }
}
