package skycat.mystical.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skycat.mystical.Mystical;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {
    @Shadow
    private int explosionRadius;

    @Inject(method = "<init>", at = @At("TAIL"))
    void CreeperEntity(EntityType entityType, World world, CallbackInfo ci) {
        if (Mystical.SPELL_HANDLER.shouldIncreaseCreeperExplosion()) {
            explosionRadius = 6; // TODO: Make configurable
        }
    }

}
