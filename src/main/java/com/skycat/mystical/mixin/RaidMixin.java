package com.skycat.mystical.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.IllusionersReplaceEvokersConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @WrapOperation(method = "spawnNextWave", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;"))
    public Entity illusion(EntityType instance, World world, Operation<RaiderEntity> original) { // WARN: Not affected by havens
        if (!Mystical.isClientWorld() &&
                instance == EntityType.EVOKER &&
                Mystical.getSpellHandler().isConsequenceActive(IllusionersReplaceEvokersConsequence.class)) {
            Utils.log(Utils.translateString(IllusionersReplaceEvokersConsequence.FACTORY.getDescriptionKey()), Mystical.CONFIG.illusionersReplaceEvokers.logLevel());
            return EntityType.ILLUSIONER.create(world);
        }
        return original.call(instance, world);
    }
}
