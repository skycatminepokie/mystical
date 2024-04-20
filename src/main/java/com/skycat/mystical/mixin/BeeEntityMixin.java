package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.EvilBeesConsequence;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends MobEntityMixin {
    @Inject(method = "initGoals", at = @At("TAIL"))
    public void addTargetSelector(CallbackInfo ci) {
        targetSelector.add(5, new ActiveTargetGoal<LivingEntity>((BeeEntity) (Object) this, LivingEntity.class, true, this::mystical_targetPredicate));
    }

    @Unique
    public boolean mystical_targetPredicate(LivingEntity entity) { // STOPSHIP This fires way too much, I need to either cache it or mess with the goals live.
        if (Mystical.getSpellHandler().isConsequenceActive(EvilBeesConsequence.class)
            && !Mystical.getHavenManager().isInHaven((BeeEntity)(Object) this)) {
            return !entity.getType().equals(EntityType.BEE);
        }
        return false;
    }
}
