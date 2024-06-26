package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.MapCodec;
import com.skycat.mystical.Mystical;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ZombieTypeChangeConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();

    @Override
    public @NotNull ConsequenceFactory<ZombieTypeChangeConsequence> getFactory() {
        return FACTORY;
    }

    public ZombieTypeChangeConsequence() {
        super(ZombieTypeChangeConsequence.class, null, 50d);
    }

    public static class Factory extends ConsequenceFactory<ZombieTypeChangeConsequence> {

        public Factory() {
            super("zombieTypeChange",
                    "Zombie Type Change",
                    "Zombies are having a wardrobe crisis",
                    "Zombie type changed.",
                    ZombieTypeChangeConsequence.class,
                    MapCodec.unit(ZombieTypeChangeConsequence::new));
        }

        @Override
        public @NotNull ZombieTypeChangeConsequence make(@NotNull Random random, double points) {
            return new ZombieTypeChangeConsequence();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.zombieTypeChange.enabled() ? Mystical.CONFIG.zombieTypeChange.weight() : 0);
        }
    }
}
