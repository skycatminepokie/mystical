package skycat.mystical.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skycat.mystical.Mystical;
import skycat.mystical.spell.consequence.SkeletonTypeChangeConsequence;

@Mixin(DamageTracker.class)
public abstract class DamageTrackerMixin {

    @Shadow @Final private LivingEntity entity;

    @Inject(method = "onDamage", at = @At("HEAD"))
    public void onDamage(DamageSource damageSource, float originalHealth, float damage, CallbackInfo ci) {
        if (entity instanceof AbstractSkeletonEntity) {
            if (Mystical.SPELL_HANDLER.shouldChangeSkeletonType() && // Spell is active
                    !damageSource.isOutOfWorld() && // Damage from normal source
                    Mystical.RANDOM.nextFloat(0, 100) >= Mystical.CONFIG.skeletonTypeChangeConsequence.chance()) { // Roll the dice
                float totalDamage = (entity.getMaxHealth() - originalHealth) + damage;
                Mystical.LOGGER.info("total: " + totalDamage + " max: " + entity.getMaxHealth() + " original: " + originalHealth + " damage: " + damage);
                // Convert
                ((AbstractSkeletonEntity) entity).convertTo(Util.getRandom(SkeletonTypeChangeConsequence.SKELETON_TYPES, Mystical.MC_RANDOM), true)
                        .damage(DamageSource.OUT_OF_WORLD, totalDamage); // Do the damage TODO check for null (shouldn't happen though)
            }
        }
    }
}
