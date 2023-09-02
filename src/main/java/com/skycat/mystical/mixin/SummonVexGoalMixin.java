package com.skycat.mystical.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.LogLevel;
import com.skycat.mystical.common.spell.consequence.RandomEvokerSummonsConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.entity.mob.EvokerEntity$SummonVexGoal")
public abstract class SummonVexGoalMixin {
    @Unique BlockPos spawningPos;

    @ModifyVariable(method = "castSpell", at = @At("STORE"), ordinal = 0)
    public BlockPos grabSpawnLocation(BlockPos pos) {
        spawningPos = pos;
        return pos;
    }

    @WrapOperation(
            method = "castSpell",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;")
    )
    public Entity overrideVexSpawning(EntityType instance, World world, Operation<VexEntity> original) { // TODO: Make things not spawn in walls
        if (Mystical.getHavenManager().isInHaven(spawningPos) ||
                world.isClient() ||
                spawningPos == null ||
                !Mystical.getSpellHandler().isConsequenceActive(RandomEvokerSummonsConsequence.class) ||
                !Utils.percentChance(Mystical.CONFIG.randomEvokerSummons.chance())) {
            return original.call(instance, world);
        }
        ServerWorld serverWorld = (ServerWorld) world;
        EntityType<?> mobType = Utils.getRandomEntryFromTag(Registries.ENTITY_TYPE, Mystical.EVOKER_SUMMONABLE);
        if (mobType == null) {
            Utils.log("Warning: Could not choose random mobType in SummonVexGoalMixin. Consider checking mystical:evoker_summonable tag.", LogLevel.WARN);
            return original.call(instance, world); // Let it run normally
        }
        Entity summonedMob = mobType.create(world);
        if (summonedMob == null) {
            Utils.log("Could not summon chosen mob, " + mobType.getName() + " in SummonVexGoalMixin.", LogLevel.WARN);
            return original.call(instance, world);
        }
        // From EvokerEntity.SummonVexGoal#castSpell
        summonedMob.refreshPositionAndAngles(spawningPos, 0.0f, 0.0f);
        serverWorld.spawnEntityAndPassengers(summonedMob);
        Utils.log(RandomEvokerSummonsConsequence.FACTORY.getDescriptionKey(), Mystical.CONFIG.randomEvokerSummons.logLevel());
        return null; // And tell it to stop doing stuff. NOTE: As of writing, this will just make it continue trying to spawn its three vexes.
    }
}
