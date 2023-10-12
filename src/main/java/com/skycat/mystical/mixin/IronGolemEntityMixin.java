package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.AggressiveGolemsConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemEntityMixin extends MobEntityMixin { // TODO: Credit MattiDragon#8944 on discord for extension info
    @Unique
    private static boolean targetPredicate(LivingEntity entity) {
        return (Mystical.isClientWorld() &&
                Mystical.getSpellHandler().isConsequenceActive(AggressiveGolemsConsequence.class)) &&
                !Mystical.getHavenManager().isInHaven(entity); // Don't attack things that are in a haven
    }

    @Inject(method = "canTarget", at = @At("RETURN"), cancellable = true)
    private void swapCanTarget(EntityType<?> type, CallbackInfoReturnable<Boolean> cir) {
        if (!Mystical.isClientWorld() &&
                Mystical.getSpellHandler().isConsequenceActive(AggressiveGolemsConsequence.class)) {
            cir.setReturnValue(!type.equals(EntityType.CAT)); // Protect the poor kitties. TODO: Config
            Utils.log(Utils.translateString(AggressiveGolemsConsequence.FACTORY.getDescriptionKey()), Mystical.CONFIG.aggressiveGolems.logLevel());
        }
    }
}
