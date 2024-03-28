package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.TurboChickensConsequence;
import net.minecraft.entity.passive.ChickenEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin {
    @Redirect(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/ChickenEntity;eggLayTime:I", opcode = Opcodes.PUTFIELD, ordinal = 1)) // TODO: Don't redirect
    private void mystical_shortenEggLayTime(ChickenEntity instance, int value) {
        if (!Mystical.isClientWorld() &&
                Mystical.getSpellHandler().isConsequenceActive(TurboChickensConsequence.class) &&
                Mystical.getHavenManager().isInHaven(instance)) {
            instance.eggLayTime = (int) (value / Mystical.CONFIG.turboChickens.speed());
        } else {
            instance.eggLayTime = value;
        }
    }
}
