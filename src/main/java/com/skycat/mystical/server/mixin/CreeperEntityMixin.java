package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.BigCreeperExplosionConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {
    @Shadow
    private int explosionRadius;

    @Inject(method = "<init>", at = @At("TAIL"))
    void CreeperEntity(EntityType entityType, World world, CallbackInfo ci) { // TODO: allow for working with havens
        if (Mystical.SPELL_HANDLER.isConsequenceActive(BigCreeperExplosionConsequence.class) && Utils.percentChance(Mystical.CONFIG.bigCreeperExplosion.chance())) {
            explosionRadius = (int) (explosionRadius * Mystical.CONFIG.bigCreeperExplosion.multiplier()); // WARN This method seems to lag out the game for some reason
            Utils.log(Utils.translateString("text.mystical.consequence.bigCreeperExplosion.fired"), Mystical.CONFIG.bigCreeperExplosion.logLevel());
        }
    }

}
