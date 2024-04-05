package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.AggressiveGolemsConsequence;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends MobEntityMixin {
    @Unique
    private static boolean mystical_targetPredicate(LivingEntity entity) {
        return (!Mystical.isClientWorld() &&
                Mystical.getSpellHandler().isConsequenceActive(AggressiveGolemsConsequence.class)) &&
                !Mystical.getHavenManager().isInHaven(entity); // Don't attack things that are in a haven
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void mystical_addGoal(CallbackInfo ci) {
        targetSelector.add(5, new ActiveTargetGoal<>(((IronGolemEntity) (Object) this), LivingEntity.class, false, IronGolemEntityMixin::mystical_targetPredicate));
    }
}
