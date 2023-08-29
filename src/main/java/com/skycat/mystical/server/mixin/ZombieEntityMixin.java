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
import net.minecraft.registry.Registries;
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
            // TODO: Testing
            EntityType<?> randomType = Utils.getRandomEntryFromTag(Registries.ENTITY_TYPE, Mystical.ZOMBIE_VARIANTS);
            if (randomType == null) {
                Utils.log("Failed to get randomType to convert zombie to. We'll skip it for now.", LogLevel.WARN);
                return;
            }
            Entity newEntity = null;
            for (int i = 0; i < 10; i++) {
                try { // Checking the cast here
                    //noinspection unchecked
                    newEntity = dis.convertTo((EntityType<MobEntity>) randomType, true);
                    break;
                } catch (ClassCastException e) {
                    Utils.log("EntityType<?>" + randomType.getName().getString() + " in mystical:zombie_variants - ? doesn't extend MobEntity - Attempt #" + i + " :(", LogLevel.WARN);
                }
            }
            if (newEntity == null) {
                Utils.log("Failed to convert zombie - see previous logging and check tags. For now, we'll skip it.", LogLevel.ERROR);
                // TODO: Maybe warn admins?
                return;
            }
            Utils.log(Utils.translateString("text.mystical.consequence.zombieTypeChange.fired"), Mystical.CONFIG.zombieTypeChange.logLevel());
            newEntity.damage(dis.getWorld().getDamageSources().outOfWorld(), totalDamage);
        }
    }
}
