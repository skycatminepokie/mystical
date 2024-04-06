package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.accessor.MinecraftServerTimerAccess;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerTimer implements MinecraftServerTimerAccess {
    @Unique
    private long mystical_ticksUntilNight;

    @Inject(method = "tick", at = @At("TAIL"))
    private void mystical_onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (--this.mystical_ticksUntilNight <= 0L) {
            Mystical.EVENT_HANDLER.doNighttimeEvents((MinecraftServer) (Object) this);
        }
    }

    @Unique
    @Override
    public void mystical_setTimer(long ticksUntilNight) {
        this.mystical_ticksUntilNight = ticksUntilNight;
    }

}
