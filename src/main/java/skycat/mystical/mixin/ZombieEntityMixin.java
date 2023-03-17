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

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {

    @Inject(method = "damage", at = @At("RETURN"))
    public void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() || Mystical.RANDOM.nextFloat(0, 100) >= Mystical.CONFIG.zombieTypeChangeConsequence.chance()) { // If nothing happened or we didn't roll high enough // TODO: config
            return;
        }
        ZombieEntity dis = (ZombieEntity) (Object) this;
        float totalDamage = dis.getMaxHealth() - dis.getHealth();
        Mystical.LOGGER.info("totalDamage: " + totalDamage);
        if (!source.isOutOfWorld() && !dis.isDead()) {
                Entity newEntity = dis.convertTo(Util.getRandom(ZombieTypeChangeConsequence.ZOMBIE_TYPES, Mystical.MC_RANDOM), true);
                if (newEntity != null) {
                    newEntity.damage(DamageSource.OUT_OF_WORLD, totalDamage);
                }
        }
    }
}
