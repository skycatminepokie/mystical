package skycat.mystical.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import skycat.mystical.Mystical;
import skycat.mystical.spell.consequence.ZombieTypeChangeConsequence;
import skycat.mystical.util.Utils;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {

    @Inject(method = "damage", at = @At("RETURN"))
    public void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        // If the spell is not active, the damage didn't go through, or we roll too low, don't do anything
        if (!Mystical.SPELL_HANDLER.isConsequenceActive(ZombieTypeChangeConsequence.class) || !cir.getReturnValue() || !Utils.percentChance(Mystical.CONFIG.zombieTypeChange.chance())) {
            return;
        }
        ZombieEntity dis = (ZombieEntity) (Object) this;
        float totalDamage = dis.getMaxHealth() - dis.getHealth();
        if (!source.isOutOfWorld() && !dis.isDead()) {
                Entity newEntity = dis.convertTo(Util.getRandom(ZombieTypeChangeConsequence.ZOMBIE_TYPES, Mystical.MC_RANDOM), true);
                Utils.log(Utils.translateString("text.mystical.zombieTypeChange.fired"), Mystical.CONFIG.zombieTypeChange.logLevel()); // TODO: Translate
                if (newEntity != null) {
                    newEntity.damage(DamageSource.OUT_OF_WORLD, totalDamage);
                }
        }
    }
}
