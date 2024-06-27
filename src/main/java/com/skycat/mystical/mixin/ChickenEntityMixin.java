package com.skycat.mystical.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.TurboChickensConsequence;
import net.minecraft.entity.passive.ChickenEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin {
    @WrapOperation(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/ChickenEntity;eggLayTime:I", opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void mystical_shortenEggLayTime(ChickenEntity instance, int value, Operation<Void> original) {
        if (!Mystical.isClientWorld() &&
                Mystical.getSpellHandler().isConsequenceActive(TurboChickensConsequence.class) &&
                Mystical.getHavenManager().isInHaven(instance)) {
            original.call(instance, (int) (value / Mystical.CONFIG.turboChickens.speed()));
        } else {
            original.call(instance, value);
        }
    }
}
