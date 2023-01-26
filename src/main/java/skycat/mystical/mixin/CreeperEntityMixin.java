package skycat.mystical.mixin;

import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin {
    /*
    @Shadow private int explosionRadius;

    @Inject(method = "<init>", at = @At("TAIL"))
    void CreeperEntity(EntityType entityType, World world, CallbackInfo ci) {
        int newRadius = Mystical.getCURSE_HANDLER().getCreeperExplosionRadius();
        if (newRadius != -1) {
            explosionRadius = newRadius;
        }
    }
    */
}
