package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.LogLevel;
import com.skycat.mystical.common.spell.consequence.ZombieTypeChangeConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {

    @Inject(method = "damage", at = @At("RETURN"))
    public void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ZombieEntity dis = (ZombieEntity) (Object) this;
        // If the spell is not active, the damage didn't go through, or we roll too low, don't do anything
        if (Mystical.getHavenManager().isInHaven(dis) ||
                !Mystical.getSpellHandler().isConsequenceActive(ZombieTypeChangeConsequence.class) ||
                !cir.getReturnValue() ||
                !Utils.percentChance(Mystical.CONFIG.zombieTypeChange.chance())) {
            return;
        }
        float totalDamage = dis.getMaxHealth() - dis.getHealth();
        if (!source.isOf(DamageTypes.OUT_OF_WORLD) && !dis.isDead()) {

            EntityType<?> randomType = EntityType.ARMOR_STAND;
            // EntityType<?> randomType = EntityType.ZOMBIE;
            EntityType<MobEntity> mobEntityType = null;

            // Claims i < 10 == true, I guess because ClassCastException won't be thrown. If I could make it throw that when ? does not extend MobEntity, then wonderful!
            for (int i = 0; i < 10; i++) {
                try {
                    // Unchecked cast
                    mobEntityType = (EntityType<MobEntity>) randomType;
                    break;
                } catch (ClassCastException e) {
                    Utils.log("EntityType<?>" + randomType.getName().getString() + " in mystical:zombie_variants - ? doesn't extend MobEntity - Attempt #" + i + " :(", LogLevel.WARN);
                }
            }
            if (mobEntityType == null) {
                Utils.log("Failed to get a random type");
                return;
            }

            Entity newEntity = dis.convertTo(mobEntityType, true);
            Utils.log(Utils.translateString("text.mystical.consequence.zombieTypeChange.fired"), Mystical.CONFIG.zombieTypeChange.logLevel());
            if (newEntity != null) {
                newEntity.damage(dis.getWorld().getDamageSources().outOfWorld(), totalDamage);
            }
        }
    }

    /* @Inject(method = "burnsInDaylight", at = @At("RETURN"), cancellable = true)
    private void burnsInDaylight(CallbackInfoReturnable<Boolean> cir) {
        if (Mystical.SPELL_HANDLER.isConsequenceActive(DisableDaylightBurningConsequence.class)) {
            cir.setReturnValue(false);
        }
    }
     */
}
