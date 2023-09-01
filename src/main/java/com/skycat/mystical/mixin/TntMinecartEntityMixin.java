package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.NoFuseConsequence;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TntMinecartEntity.class)
public abstract class TntMinecartEntityMixin implements EntityLike {
    @ModifyConstant(method = "prime", constant = @Constant(intValue = 80))
    private int modifyFuse(int fuse) {
        if (!Mystical.getHavenManager().isInHaven(getBlockPos()) && Mystical.getSpellHandler().isConsequenceActive(NoFuseConsequence.class)) {
            return 1;
        }
        return fuse;
    }
}
