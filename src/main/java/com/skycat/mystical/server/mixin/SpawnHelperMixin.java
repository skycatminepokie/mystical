package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.MobSpawnSwapConsequence;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {
    @ModifyArg(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnHelper;createMob(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/EntityType;)Lnet/minecraft/entity/mob/MobEntity;"), index = 1)
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

    /*@ModifyVariable(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V", at = @At("STORE"), ordinal = 0)
    private static MobEntity giveSpeedBoost(MobEntity mobEntity) { // TY llamalad7, benonardo // TODO: This doesn't work - movement speed is not set at this point.
        if (Mystical.SPELL_HANDLER.isConsequenceActive(TurboMobConsequence.class) &&
                !Mystical.HAVEN_MANAGER.isInHaven(mobEntity)) { // TODO: chance
            mobEntity.setMovementSpeed(mobEntity.speed * 2);
        }
        return mobEntity;
    }*/

    @Inject(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT) // Tried using ModifyVariable, but speeds weren't set up then (at least for zombies)
    private static void foo(SpawnGroup group, ServerWorld world, Chunk chunk, BlockPos pos, SpawnHelper.Checker checker, SpawnHelper.Runner runner, CallbackInfo ci, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, int i, BlockState blockState, BlockPos.Mutable mutable, int j, int k, int l, int m, int n, SpawnSettings.SpawnEntry spawnEntry, EntityData entityData, int o, int p, int q, double d, double e, PlayerEntity playerEntity, double f, MobEntity mobEntity) {
        mobEntity.setMovementSpeed(mobEntity.speed * 3);
    }
}
