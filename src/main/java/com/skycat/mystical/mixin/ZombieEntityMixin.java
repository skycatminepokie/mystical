package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.MysticalTags;
import com.skycat.mystical.spell.consequence.ZombieTypeChangeConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin { // TODO: move to LivingEntityMixin
    @Inject(method = "damage", at = @At("RETURN"))
    public void mystical_onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ZombieEntity dis = (ZombieEntity) (Object) this;
        // If the spell is not active, the damage didn't go through, or we roll too low, don't do anything
        if (Mystical.isClientWorld() ||
                !cir.getReturnValue() || // Damage was avoided anyway
                Mystical.getHavenManager().isInHaven(dis) ||
                !Mystical.getSpellHandler().isConsequenceActive(ZombieTypeChangeConsequence.class) ||
                !Utils.percentChance(Mystical.CONFIG.zombieTypeChange.chance())) {
            return;
        }
        float totalDamage = dis.getMaxHealth() - dis.getHealth();
        if (!source.isOf(DamageTypes.OUT_OF_WORLD) && !dis.isDead()) {
            Entity newEntity = Utils.convertToRandomInTag(dis, MysticalTags.ZOMBIE_VARIANTS);
            if (newEntity == null) return;
            Utils.log(Utils.translateString("text.mystical.consequence.zombieTypeChange.fired"), Mystical.CONFIG.zombieTypeChange.logLevel());
            newEntity.damage(dis.getDamageSources().outOfWorld(), totalDamage);
        }
    }
}
