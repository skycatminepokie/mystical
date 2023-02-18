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
import skycat.mystical.util.Utils;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {
    @Shadow
    private int explosionRadius;

    @Inject(method = "<init>", at = @At("TAIL"))
    void CreeperEntity(EntityType entityType, World world, CallbackInfo ci) {
        if (Mystical.SPELL_HANDLER.isBigCreeperExplosionActive() && (Mystical.RANDOM.nextDouble(0, 100) < Mystical.CONFIG.bigCreeperExplosionConsequence.chance())) {
            explosionRadius = (int) (explosionRadius * Mystical.CONFIG.bigCreeperExplosionConsequence.multiplier()); // WARN This method seems to lag out the game for some reason
            Utils.log(Utils.translateString("text.mystical.creeperEntityMixin.fired"), Mystical.CONFIG.bigCreeperExplosionConsequence.logLevel());
        }
    }

}
