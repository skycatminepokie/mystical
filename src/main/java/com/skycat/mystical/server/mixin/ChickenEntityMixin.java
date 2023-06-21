package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.TurboChickensConsequence;
import net.minecraft.entity.passive.ChickenEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin {
    @Redirect(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/passive/ChickenEntity;eggLayTime:I", opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void shortenEggLayTime(ChickenEntity instance, int value) {
        if (Mystical.SPELL_HANDLER.isConsequenceActive(TurboChickensConsequence.class) &&
            !Mystical.HAVEN_MANAGER.isInHaven(instance)) {
            instance.eggLayTime = (int) (value / Mystical.CONFIG.turboChickens.speed());
        } else {
            instance.eggLayTime = value;
        }
    }
}
