package com.skycat.mystical.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.IllusionersReplaceEvokersConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @SuppressWarnings("rawtypes")
    @WrapOperation(method = "spawnNextWave", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"))
    public Entity mystical_swapEvokerIllusioner(EntityType instance, World world, Operation<RaiderEntity> original, @Local(ordinal = 0) BlockPos pos) {
        if (!Mystical.isClientWorld() &&
                instance == EntityType.EVOKER &&
                Mystical.getSpellHandler().isConsequenceActive(IllusionersReplaceEvokersConsequence.class) &&
                !Mystical.getHavenManager().isInHaven(pos)) {
            Utils.log(Utils.translateString(IllusionersReplaceEvokersConsequence.FACTORY.getDescriptionKey()), Mystical.CONFIG.illusionersReplaceEvokers.logLevel());
            return EntityType.ILLUSIONER.create(world);
        }
        return original.call(instance, world);
    }
}
