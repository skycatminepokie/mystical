package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.DisableDaylightBurningConsequence;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
    @Inject(method = "isAffectedByDaylight", at = @At("HEAD"), cancellable = true)
    private void cancelDaylightEffects(CallbackInfoReturnable<Boolean> cir) {
        if (Mystical.SPELL_HANDLER.isConsequenceActive(DisableDaylightBurningConsequence.class)) { // TODO allow for havening
            cir.setReturnValue(false);
        }
    }
}
