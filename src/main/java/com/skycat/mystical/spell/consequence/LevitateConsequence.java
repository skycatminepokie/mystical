package com.skycat.mystical.spell.consequence;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.util.Utils;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class LevitateConsequence extends SpellConsequence implements EntitySleepEvents.StopSleeping, ServerEntityCombatEvents.AfterKilledOtherEntity, ServerPlayerEvents.AfterRespawn, PlayerBlockBreakEvents.After {  // TODO: Tests
    private final int length;
    private final int level;
    private static final ArrayList<Class> supportedEvents = new ArrayList<>();
    public static final ConsequenceFactory<LevitateConsequence> FACTORY = new Factory();

    public int getLength() {
        return length;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public @NotNull ConsequenceFactory<LevitateConsequence> getFactory() {
        return FACTORY;
    }

    static {
        Collections.addAll(supportedEvents,
                ServerEntityCombatEvents.AfterKilledOtherEntity.class,
                PlayerBlockBreakEvents.After.class);
    }

    public LevitateConsequence(int length, int level, Class callbackType) {
        super(LevitateConsequence.class, callbackType, 30d);
        this.length = length;
        this.level = level;
    }

    private void levitate(LivingEntity entity) {
        if (Utils.percentChance(Mystical.CONFIG.levitate.chance()) && !(Mystical.isClientWorld() && Mystical.getHavenManager().isInHaven(entity))) {
            Utils.giveStatusEffect(entity, StatusEffects.LEVITATION, length, level); // TODO: CONFIG
            Utils.log(Utils.translateString("text.mystical.consequence.levitate.fired"), Mystical.CONFIG.levitate.logLevel());
        }
    }

    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (!Mystical.getHavenManager().isInHaven(player)) {
            levitate(player);
        }
    }

    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        if (entity.isLiving() && Mystical.getHavenManager().isInHaven(entity)) {
            levitate((LivingEntity) entity);
        }
    }

    @Deprecated
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        if (!Mystical.getHavenManager().isInHaven(newPlayer)) {
            levitate(newPlayer);
        }
    }

    @Override
    @Deprecated
    public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos) {
        levitate(entity);
    }

    private static class Factory extends ConsequenceFactory<LevitateConsequence> {
        private Factory() {
            super("levitate",
                    "Levitation",
                    "Are you a balloon?",
                    "Levitating entity",
                    LevitateConsequence.class,
                    RecordCodecBuilder.create(instance -> instance.group(
                            Codec.INT.fieldOf("length").forGetter(LevitateConsequence::getLength),
                            Codec.INT.fieldOf("level").forGetter(LevitateConsequence::getLevel),
                            Utils.CLASS_CODEC.fieldOf("callbackType").forGetter(LevitateConsequence::getCallbackType)
                    ).apply(instance, LevitateConsequence::new)));
        }

        @Override
        public @NotNull LevitateConsequence make(@NonNull Random random, double points) {
            return new LevitateConsequence(20, 5, Utils.chooseRandom(random, supportedEvents));
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.levitate.enabled() ? Mystical.CONFIG.levitate.weight() : 0);
        }
    }
}
