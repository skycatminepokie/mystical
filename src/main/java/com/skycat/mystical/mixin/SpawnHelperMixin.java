package com.skycat.mystical.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.spell.consequence.MobSpawnSwapConsequence;
import com.skycat.mystical.spell.consequence.TurboMobsConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperMixin {
    @Inject(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V", shift = At.Shift.AFTER))
    // Tried using ModifyVariable, but locations weren't set up yet. // TY llamalad7, benonardo
    private static void mystical_giveSpeedBoost(CallbackInfo ci, @Local(ordinal = 0) MobEntity mobEntity) {
        // Most of the parameters are unused :(
        if (Mystical.isClientWorld()) return; // Probably won't happen, but I'm leaving it just cuz.
        for (Spell spell : Mystical.getSpellHandler().spellsOfConsequenceType(TurboMobsConsequence.class)) { // Inside this, there must be an active TurboMobsConsequence spell
            if (!Mystical.getHavenManager().isInHaven(mobEntity) &&
                    ((TurboMobsConsequence) spell.getConsequence()).entityType.equals(mobEntity.getType())) { // Make sure it applies to this type // TODO: Chance
                EntityAttributeInstance attributeInstance = mobEntity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if (attributeInstance != null) {
                    attributeInstance.addPersistentModifier(new EntityAttributeModifier("Mystical speed boost", 0.5, EntityAttributeModifier.Operation.MULTIPLY_BASE)); // TODO: Config
                    Utils.log(Utils.translateString(TurboMobsConsequence.FACTORY.getDescriptionKey()), Mystical.CONFIG.turboMobs.logLevel());
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @ModifyArg(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnHelper;createMob(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/EntityType;)Lnet/minecraft/entity/mob/MobEntity;"), index = 1)
    private static EntityType mystical_swapMobs(EntityType entityType) {
        if (Mystical.isClientWorld() || !Mystical.getSpellHandler().isConsequenceActive(MobSpawnSwapConsequence.class)) {
            return entityType;
        }
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
