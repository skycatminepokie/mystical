package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.AggressiveGolemsConsequence;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends MobEntityMixin { // TODO: Credit MattiDragon#8944 on discord for extension info
    private static boolean targetPredicate(LivingEntity entity) {
        return Mystical.getSpellHandler().isConsequenceActive(AggressiveGolemsConsequence.class) &&
                !Mystical.getHavenManager().isInHaven(entity); // Don't attack things that are in a haven
    }

    @Inject(method = "canTarget", at = @At("RETURN"), cancellable = true)
    private void swapCanTarget(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        if (Mystical.getSpellHandler().isConsequenceActive(AggressiveGolemsConsequence.class)) {
            cir.setReturnValue(!type.equals(EntityType.CAT)); // Protect the poor kitties. TODO: Config
        }
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void addTargetGoal(CallbackInfo ci) {
        targetSelector.add(999, new ActiveTargetGoal<>(((IronGolemEntity) (Object) this), LivingEntity.class, false, IronGolemEntityMixin::targetPredicate));
    }
}
