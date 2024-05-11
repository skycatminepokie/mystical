package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.MysticalTags;
import com.skycat.mystical.spell.consequence.EnderTypeChangeConsequence;
import com.skycat.mystical.spell.consequence.SkeletonTypeChangeConsequence;
import com.skycat.mystical.util.Utils;
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
    public void mystical_onApplyDamage(DamageSource damageSource, float damage, CallbackInfo ci) {
        if (damageSource.isOf(DamageTypes.OUT_OF_WORLD) || Mystical.isClientWorld()) { // Require damage to be from normal source and in server world
            return;
        }

        LivingEntity dis = ((LivingEntity) (Object) this);

        if (Mystical.getHavenManager().isInHaven(dis) || dis.isDead()) { // Dead or in a haven, don't bother it.
            return;
        }

        var originalHealth = dis.getHealth();

        if (!Mystical.getHavenManager().isInHaven(dis)) {
            if (dis instanceof AbstractSkeletonEntity skeleton) {
                if (Mystical.getSpellHandler().isConsequenceActive(SkeletonTypeChangeConsequence.class) && // Spell is active
                        Utils.percentChance(Mystical.CONFIG.skeletonTypeChange.chance())) { // Roll the dice
                    float totalDamage = dis.getHealth();
                    Utils.log(Utils.translateString("text.mystical.consequence.skeletonTypeChange.fired"), Mystical.CONFIG.skeletonTypeChange.logLevel());
                    // Convert
                    Entity newEntity = Utils.convertToRandomInTag(skeleton, MysticalTags.SKELETON_VARIANTS);
                    if (newEntity == null) {
                        Utils.log("Whoops, something failed while converting skeleton.");
                        return; // Something failed, just ignore it
                    }
                    // Do the damage
                    newEntity.damage(dis.getDamageSources().outOfWorld(), totalDamage);
                }
            } else {
                if (dis.getType().isIn(MysticalTags.ENDERMAN_VARIANTS) && dis instanceof MobEntity enderEntity) {
                    if (Mystical.getSpellHandler().isConsequenceActive(EnderTypeChangeConsequence.class) && // Spell is active
                            Utils.percentChance(Mystical.CONFIG.enderTypeChange.chance())) { // Roll the dice
                        float totalDamage = (dis.getMaxHealth() - originalHealth) + damage;
                        // Convert
                        MobEntity newEntity = Utils.convertToRandomInTag(enderEntity, MysticalTags.ENDERMAN_VARIANTS);
                        if (newEntity == null) {
                            return;
                        }
                        Utils.log(Utils.translateString("text.mystical.consequence.enderTypeChange.fired"), Mystical.CONFIG.enderTypeChange.logLevel());

                        newEntity.damage(dis.getDamageSources().outOfWorld(), totalDamage); // Do the damage
                        }
                    }
                }
            }
    }
}
