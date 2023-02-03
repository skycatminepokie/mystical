package skycat.mystical.spell.consequence;


import lombok.NonNull;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import skycat.mystical.util.Utils;

import java.util.Random;

public class LevitateConsequence extends SpellConsequence implements EntitySleepEvents.StartSleeping, EntitySleepEvents.StopSleeping, ServerEntityCombatEvents.AfterKilledOtherEntity, ServerPlayerEvents.AfterRespawn, PlayerBlockBreakEvents.After {
    private final int length;
    private final int level;
    public static final ConsequenceFactory<LevitateConsequence> FACTORY = new Factory();

    public LevitateConsequence(int length, int level, Class callbackType) { // TODO: maybe double-check that it's a valid callbackType
        super(LevitateConsequence.class, callbackType);
        this.length = length;
        this.level = level;
    }

    private void levitate(LivingEntity entity) {
        Utils.giveStatusEffect(entity, StatusEffects.LEVITATION, length, level);
    }

    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        levitate(player);
    }

    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        if (entity.isLiving()) {
            levitate((LivingEntity) entity);
        }
    }

    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        levitate(newPlayer);
    }


    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        levitate(entity);
    }

    @Override
    public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos) {
        levitate(entity);
    }

    private static class Factory implements ConsequenceFactory<LevitateConsequence> {
        @Override
        public @NotNull LevitateConsequence make(@NonNull Random random, double points) {
            // TODO: Take points into account
            // STOPSHIP Fix
            return null;
        }
    }
}
