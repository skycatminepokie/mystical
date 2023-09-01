package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.OneStrikeWardensConsequence;
import net.minecraft.block.entity.SculkShriekerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SculkShriekerBlockEntity.class)
public abstract class SkulkShriekerBlockEntityMixin extends BlockEntityMixin {

    @ModifyConstant(method = "trySpawnWarden", constant = @Constant(intValue = 4))
    private int warningsBeforeSpawning(int constant) { // TODO: This currently tests if the shrieker is in a haven. Maybe this should be the player instead (set player strikes to 4 whenever they gain 1?)
        if (Mystical.getSpellHandler().isConsequenceActive(OneStrikeWardensConsequence.class) && !Mystical.getHavenManager().isInHaven(getPos())) {
            return 1;
        }
        return constant;
    }
}
