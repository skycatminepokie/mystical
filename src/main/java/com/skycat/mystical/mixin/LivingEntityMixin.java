package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.EnderTypeChangeConsequence;
import com.skycat.mystical.common.spell.consequence.SkeletonTypeChangeConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    // Scuffed? Maybe.
    @Inject(method = "applyDamage", at = @At("TAIL"))
    public void applyDamage(DamageSource damageSource, float damage, CallbackInfo ci) {
        if (damageSource.isOf(DamageTypes.OUT_OF_WORLD)) { // Require damage to be from normal source
            return;
        }
        
        LivingEntity dis = ((LivingEntity) (Object) this);
        var originalHealth = dis.getHealth();
        
        if (!(Mystical.isClientWorld() && Mystical.getHavenManager().isInHaven(dis))) { 
            if (dis instanceof AbstractSkeletonEntity skeleton) {
                if ((Mystical.isClientWorld() && Mystical.getSpellHandler().isConsequenceActive(SkeletonTypeChangeConsequence.class)) && // Spell is active
                        !dis.isDead() && // And we're not dead
                        Utils.percentChance(Mystical.CONFIG.skeletonTypeChange.chance())) { // Roll the dice
                    float totalDamage = dis.getHealth();
                    Utils.log(Utils.translateString("text.mystical.consequence.skeletonTypeChange.fired"), Mystical.CONFIG.skeletonTypeChange.logLevel());
                    // Convert
                    Entity newEntity = Utils.convertToRandomInTag(skeleton, Mystical.SKELETON_VARIANTS);
                    if (newEntity == null) return;
                    // Do the damage
                    newEntity.damage(dis.getWorld().getDamageSources().outOfWorld(), totalDamage);
                }
            } else {
                if (dis.getType().isIn(Mystical.ENDERMAN_VARIANTS) && dis instanceof MobEntity enderEntity) {
                    if ((Mystical.isClientWorld() && Mystical.getSpellHandler().isConsequenceActive(EnderTypeChangeConsequence.class)) && // Spell is active
                            !dis.isDead() && // And we're not dead
                            Utils.percentChance(Mystical.CONFIG.enderTypeChange.chance())) { // Roll the dice
                        float totalDamage = (dis.getMaxHealth() - originalHealth) + damage;
                        // Convert
                        MobEntity newEntity = Utils.convertToRandomInTag(enderEntity, Mystical.ENDERMAN_VARIANTS);
                        if (newEntity == null) {
                            return;
                        }
                        Utils.log(Utils.translateString("text.mystical.consequence.enderTypeChange.fired"), Mystical.CONFIG.enderTypeChange.logLevel());

                        newEntity.damage(dis.getWorld().getDamageSources().outOfWorld(), totalDamage); // Do the damage
                        }
                    }
                }
            }
    }
}
