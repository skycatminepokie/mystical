package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.MobSpawnSwapConsequence;
import net.minecraft.entity.EntityType;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {
    @ModifyArg(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnHelper;createMob(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/EntityType;)Lnet/minecraft/entity/mob/MobEntity;"), index = 1)
    private static EntityType swapMobs(EntityType entityType) {
        if (!Mystical.SPELL_HANDLER.isConsequenceActive(MobSpawnSwapConsequence.class)) return entityType;
        if (entityType == EntityType.SKELETON) return EntityType.WITHER_SKELETON; // Skeleton <-> Wither skeleton
        if (entityType == EntityType.WITHER_SKELETON) return EntityType.SKELETON;
        if (entityType == EntityType.ZOMBIE) return EntityType.ZOMBIFIED_PIGLIN; // Zombie <-> Zombified piglin
        if (entityType == EntityType.ZOMBIFIED_PIGLIN) return EntityType.ZOMBIE;
        if (entityType == EntityType.SLIME) return EntityType.MAGMA_CUBE; // Slime <-> Magma cube
        if (entityType == EntityType.MAGMA_CUBE) return EntityType.SLIME;
        if (entityType == EntityType.CREEPER) return EntityType.BLAZE; // Creeper <-> Blaze
        if (entityType == EntityType.BLAZE) return EntityType.CREEPER;

        return entityType;
    }
}
