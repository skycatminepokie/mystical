package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow @Final private MinecraftServer server;

    @Inject(method = "wakeSleepingPlayers", at = @At("TAIL"))
    private void onWakeSleepingPlayers(CallbackInfo ci) {
        Mystical.getEVENT_HANDLER().doNighttimeEvents(server);
    }
}
