package com.skycat.mystical.common.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.MinecraftServerTimerAccess;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

// CREDIT: Daomephsta#0044 for help on fabric discord (and fabric bot too)
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerTimer implements MinecraftServerTimerAccess {
    @Unique
    private long ticksUntilNight;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (--this.ticksUntilNight <= 0L) {
            Mystical.EVENT_HANDLER.doNighttimeEvents();
        }
    }

    @Override
    public void mystical_setTimer(long ticksUntilNight) {
        this.ticksUntilNight = ticksUntilNight;
    }

}
