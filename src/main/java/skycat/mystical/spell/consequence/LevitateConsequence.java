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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LevitateConsequence extends SpellConsequence implements EntitySleepEvents.StopSleeping, ServerEntityCombatEvents.AfterKilledOtherEntity, ServerPlayerEvents.AfterRespawn, PlayerBlockBreakEvents.After {
    private final int length;
    private final int level;
    private static final ArrayList<Class> supportedEvents = new ArrayList<>();
    public static final ConsequenceFactory<LevitateConsequence> FACTORY = new Factory();

    static {
        Collections.addAll(supportedEvents,
                EntitySleepEvents.StopSleeping.class,
                ServerEntityCombatEvents.AfterKilledOtherEntity.class,
                ServerPlayerEvents.AfterRespawn.class,
                PlayerBlockBreakEvents.After.class);
    }

    public LevitateConsequence(int length, int level, Class callbackType) { // TODO: maybe double-check that it's a valid callbackType
        super(LevitateConsequence.class, callbackType, "levitate", "Levitation", "Are you a balloon?");
        this.length = length;
        this.level = level;
    }

    private void levitate(LivingEntity entity) {
        Utils.giveStatusEffect(entity, StatusEffects.LEVITATION, length, level); // TODO: CONFIG
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
    public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos) {
        levitate(entity);
    }

    private static class Factory implements ConsequenceFactory<LevitateConsequence> {
        @Override
        public @NotNull LevitateConsequence make(@NonNull Random random, double points) {
            // TODO: Take points into account
            return new LevitateConsequence(5, 5, Utils.chooseRandom(random, supportedEvents));
            // return new LevitateConfig(5, 5, EntitySleepEvents.StartSleeping.class); // WARN Debug
        }

        @Override
        public double getWeight() {
            // return (Mystical.CONFIG.levitate.enabled()?Mystical.CONFIG.levitate.weight():0);
            return 100; // WARN debug
        }
    }
}
