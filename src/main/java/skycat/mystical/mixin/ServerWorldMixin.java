package skycat.mystical.mixin;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skycat.mystical.Mystical;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "wakeSleepingPlayers", at = @At("TAIL"))
    private void onWakeSleepingPlayers(CallbackInfo ci) {
        Mystical.getEVENT_HANDLER().doNighttimeEvents(); // TODO: This is a roundabout fix, but you know...
    }
}
