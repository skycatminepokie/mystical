package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.test.TestFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class AggressiveGolemsConsequence extends SpellConsequence { // TODO: Make work for snow golems
    public static final Factory FACTORY = new Factory();

    public AggressiveGolemsConsequence() {
        super(AggressiveGolemsConsequence.class, null, 100d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<AggressiveGolemsConsequence> {
        protected Factory() {
            super("aggressiveGolems",
                    "Aggressive golems",
                    "Iron + Pumpkin says \"Here, have pain!\"",
                    "Golem is aggressive",
                    AggressiveGolemsConsequence.class,
                    Codec.unit(AggressiveGolemsConsequence::new));
        }

        @Override
        public @NotNull AggressiveGolemsConsequence make(@NonNull Random random, double points) {
            return new AggressiveGolemsConsequence();
        }

        @Override
        public @Nullable TestFunction getTestFunction() {
            return null; // TODO
        }

        @Override
        public double getWeight()  {
            return (Mystical.CONFIG.aggressiveGolems.enabled() ? Mystical.CONFIG.aggressiveGolems.weight() : 0);
        }
    }
}
