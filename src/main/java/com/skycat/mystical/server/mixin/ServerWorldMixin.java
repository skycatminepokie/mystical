package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(method = "wakeSleepingPlayers", at = @At("TAIL"))
    private void onWakeSleepingPlayers(CallbackInfo ci) {
        Mystical.getEVENT_HANDLER().doNighttimeEvents();
    }
}
