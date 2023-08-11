package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.EnderTypeChangeConsequence;
import com.skycat.mystical.common.spell.consequence.SkeletonTypeChangeConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Util;
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
        
        if (!Mystical.getHavenManager().isInHaven(dis)) { 
            if (dis instanceof AbstractSkeletonEntity) {
                if (Mystical.getSpellHandler().isConsequenceActive(SkeletonTypeChangeConsequence.class) && // Spell is active
                        !dis.isDead() && // And we're not dead
                        Utils.percentChance(Mystical.CONFIG.skeletonTypeChange.chance())) { // Roll the dice
                    float totalDamage = dis.getHealth();
                    Utils.log(Utils.translateString("text.mystical.consequence.skeletonTypeChange.fired"), Mystical.CONFIG.skeletonTypeChange.logLevel());
                    // Convert
                    MobEntity skeletonEntity = ((AbstractSkeletonEntity) dis).convertTo(Util.getRandom(SkeletonTypeChangeConsequence.SKELETON_TYPES, Mystical.MC_RANDOM), true);
                    // Do the damage
                    if (skeletonEntity != null) {
                        skeletonEntity.damage(dis.getWorld().getDamageSources().outOfWorld(), totalDamage);
                    }
                }
            } else {
                if (dis instanceof EndermanEntity || dis instanceof EndermiteEntity) {
                    if (Mystical.getSpellHandler().isConsequenceActive(EnderTypeChangeConsequence.class) && // Spell is active
                            !dis.isDead() && // And we're not dead
                            Utils.percentChance(Mystical.CONFIG.enderTypeChange.chance())) { // Roll the dice
                        float totalDamage = (dis.getMaxHealth() - originalHealth) + damage;
                        Utils.log(Utils.translateString("text.mystical.consequence.enderTypeChange.fired"), Mystical.CONFIG.enderTypeChange.logLevel());
                        // Convert
                        EntityType<? extends MobEntity> convertToType = EntityType.ENDERMITE;
                        if (dis instanceof EndermiteEntity) { // If it's an endermite, turn it into an enderman instead.
                            convertToType = EntityType.ENDERMAN;
                        }
                        MobEntity mobEntity = ((MobEntity) dis).convertTo(convertToType, true);
                        if (mobEntity != null) {
                            if (convertToType.equals(EntityType.ENDERMAN)) { // Since endermites are so weak, we'll leave them be.
                                mobEntity.damage(dis.getWorld().getDamageSources().outOfWorld(), totalDamage); // Do the damage
                            }
                        } else {
                            Utils.log("mobEntity was null when converting. Not sure how.");
                        }
                    }
                }
            }
        }
    }
}
