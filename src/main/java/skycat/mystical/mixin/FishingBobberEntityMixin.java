package skycat.mystical.mixin;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import skycat.mystical.Mystical;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {
    @ModifyArg(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), index = 0)
    public Vec3d onSetVelocity(Vec3d velocity) {
        if (Mystical.SPELL_HANDLER.shouldDoFishingRodLaunch()) {
            return velocity.multiply(20.0); // TODO: config
        }
        return velocity;
    }


}
