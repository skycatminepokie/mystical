package skycat.mystical.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skycat.mystical.server.MinecraftServerTimerAccess;

import java.util.function.BooleanSupplier;

import static skycat.mystical.Mystical.EVENT_HANDLER;

// CREDIT: Daomephsta#0044 for help on fabric discord (and fabric bot too)
@Mixin(MinecraftServer.class)
public class MinecraftServerTimer implements MinecraftServerTimerAccess {
    @Unique
    private long ticksUntilNight;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (--this.ticksUntilNight <= 0L) {
            // TODO: If night...
            EVENT_HANDLER.doNighttimeEvents();
        }
    }

    @Override
    public void mystical_setTimer(long ticksUntilNight) {
        this.ticksUntilNight = ticksUntilNight;
    }

}
