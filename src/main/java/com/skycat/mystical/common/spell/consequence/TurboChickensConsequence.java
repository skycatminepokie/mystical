package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TurboChickensConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    public TurboChickensConsequence() {
        super(TurboChickensConsequence.class, null, 1d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<TurboChickensConsequence> {
        protected Factory() {
            super("turboChickens",
                    "Chickens lay eggs faster",
                    "Turbo chickens",
                    "Turbo chicken activated",
                    TurboChickensConsequence.class,
                    Codec.unit(TurboChickensConsequence::new));
        }

        @Override
        public @NotNull TurboChickensConsequence make(@NonNull Random random, double points) {
            return new TurboChickensConsequence();
        }

        @Override
        public void test(TestContext context) {
            // TODO
        }


        @Override
        public double getWeight() {
            return Mystical.CONFIG.turboChickens.enabled() ? Mystical.CONFIG.turboChickens.weight() : 0;
        }
    }
}
