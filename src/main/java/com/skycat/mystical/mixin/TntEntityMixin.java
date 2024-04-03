package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.NoFuseConsequence;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin implements EntityLike {
    @ModifyArg(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/TntEntity;setFuse(I)V"))
    private int mystical_modifyFuse(int fuse) {
        if (!Mystical.isClientWorld() &&
                !Mystical.getHavenManager().isInHaven(getBlockPos()) &&
                Mystical.getSpellHandler().isConsequenceActive(NoFuseConsequence.class)) {
            return 1;
        }
        return fuse;
    }
}
