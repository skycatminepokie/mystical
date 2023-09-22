package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.DisableDaylightBurningConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements EntityLike {
    @Shadow @Final protected GoalSelector targetSelector;

    @Inject(method = "isAffectedByDaylight", at = @At("HEAD"), cancellable = true)
    private void cancelDaylightEffects(CallbackInfoReturnable<Boolean> cir) {
        if (!(Mystical.isClientWorld() && Mystical.getHavenManager().isInHaven(getBlockPos())) &&
                (Mystical.isClientWorld() && (Mystical.isClientWorld() && Mystical.getSpellHandler().isConsequenceActive(DisableDaylightBurningConsequence.class)))) {
            Utils.log(Utils.translateString(DisableDaylightBurningConsequence.FACTORY.getDescriptionKey()), Mystical.CONFIG.disableDaylightBurning.logLevel());
            cir.setReturnValue(false);
        }
    }
}