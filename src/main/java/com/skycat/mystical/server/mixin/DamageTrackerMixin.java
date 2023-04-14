package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.EnderTypeChangeConsequence;
import com.skycat.mystical.common.spell.consequence.SkeletonTypeChangeConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageTracker.class)
public abstract class DamageTrackerMixin {

    @Shadow @Final private LivingEntity entity;

    @Inject(method = "onDamage", at = @At("HEAD"))
    public void onDamage(DamageSource damageSource, float originalHealth, float damage, CallbackInfo ci) {
        if (entity instanceof AbstractSkeletonEntity) {
            if (Mystical.SPELL_HANDLER.isConsequenceActive(SkeletonTypeChangeConsequence.class) && // Spell is active
                    !damageSource.isOutOfWorld() && // Damage from normal source
                    !entity.isDead() && // And we're not dead
                    Utils.percentChance(Mystical.CONFIG.skeletonTypeChange.chance())) { // Roll the dice
                float totalDamage = (entity.getMaxHealth() - originalHealth) + damage;
                Utils.log(Utils.translateString("text.mystical.consequence.skeletonTypeChange.fired"), Mystical.CONFIG.skeletonTypeChange.logLevel());
                // Convert
                MobEntity skeletonEntity = ((AbstractSkeletonEntity) entity).convertTo(Util.getRandom(SkeletonTypeChangeConsequence.SKELETON_TYPES, Mystical.MC_RANDOM), true);
                // Do the damage
                if (skeletonEntity != null) {
                    skeletonEntity.damage(DamageSource.OUT_OF_WORLD, totalDamage);
                }
            }
        } else {
            if (entity instanceof EndermanEntity || entity instanceof EndermiteEntity) {
                if (Mystical.SPELL_HANDLER.isConsequenceActive(EnderTypeChangeConsequence.class) && // Spell is active
                        !damageSource.isOutOfWorld() && // Damage from normal source
                        !entity.isDead() && // And we're not dead
                        Utils.percentChance(Mystical.CONFIG.enderTypeChange.chance())) { // Roll the dice
                    float totalDamage = (entity.getMaxHealth() - originalHealth) + damage;
                    Utils.log(Utils.translateString("text.mystical.consequence.enderTypeChange.fired"), Mystical.CONFIG.enderTypeChange.logLevel());
                    // Convert
                    EntityType<? extends MobEntity> convertToType = EntityType.ENDERMITE;
                    if (entity instanceof EndermiteEntity) { // If it's an endermite, turn it into an enderman instead.
                        convertToType = EntityType.ENDERMAN;
                    }
                    MobEntity mobEntity = ((MobEntity) entity).convertTo(convertToType, true);
                    if (mobEntity != null) {
                        if (convertToType.equals(EntityType.ENDERMAN)) { // Since endermites are so weak, we'll leave them be.
                            mobEntity.damage(DamageSource.OUT_OF_WORLD, totalDamage); // Do the damage
                        }
                    } else {
                        Utils.log("mobEntity was null when converting. Not sure how.");
                    }
                }
            }
        }
    }
}
