package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.server.MysticalEventHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow @Final private ServerWorldProperties worldProperties;
    @Shadow public abstract @NotNull MinecraftServer getServer();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V", shift = At.Shift.AFTER))
    private void beforeSkippingNight(CallbackInfo ci) {
        if (worldProperties.getTimeOfDay() % 24000 <= MysticalEventHandler.NIGHT_TIME) { // If the time to do night things hasn't passed, do them now
            Mystical.EVENT_HANDLER.doNighttimeEvents(getServer());
        }
    }
}
