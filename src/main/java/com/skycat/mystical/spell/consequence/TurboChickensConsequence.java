package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Random;

public class TurboChickensConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();
    public TurboChickensConsequence() {
        super(TurboChickensConsequence.class, null, 1d);
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<TurboChickensConsequence> {
        protected Factory() {
            super("turboChickens",
                    "Chickens lay eggs faster",
                    "Turbo chickens",
                    "Turbo chicken activated",
                    TurboChickensConsequence.class,
                    MapCodec.unit(TurboChickensConsequence::new));
        }

        @Override
        public @NotNull TurboChickensConsequence make(@NotNull Random random, double points) {
            return new TurboChickensConsequence();
        }


        @Override
        public double getWeight() {
            return Mystical.CONFIG.turboChickens.enabled() ? Mystical.CONFIG.turboChickens.weight() : 0;
        }
    }

    @Override
    public void onActivated(MinecraftServer server) {
        LinkedList<ChickenEntity> chickens = new LinkedList<>();
        for (ServerWorld serverWorld : server.getWorlds()) {
            serverWorld.collectEntitiesByType(EntityType.CHICKEN, (a) -> true, chickens);
            for (ChickenEntity chicken : chickens) {
                chicken.eggLayTime = (int) ((Mystical.MC_RANDOM.nextInt(6000) + 6000) / Mystical.CONFIG.turboChickens.speed()); // Magic 6000s are from default egg lay time
            }
            chickens.clear();
        }
    }
}
