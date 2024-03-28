package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SheepColorChangeConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();

    @Override
    public @NotNull ConsequenceFactory<SheepColorChangeConsequence> getFactory() {
        return FACTORY;
    }

    public SheepColorChangeConsequence() {
        super(RandomTreeTypeConsequence.class, null, 5d); // TODO: Scaling
    }

    public static class Factory extends ConsequenceFactory<SheepColorChangeConsequence> {
        public Factory() {
            super("sheepColorChange",
                    "Sheep Color Change",
                    "The fluffy marshmallows got jealous of _jeb.",
                    "Sheep color changed",
                    SheepColorChangeConsequence.class,
                    Codec.unit(SheepColorChangeConsequence::new));
        }

        @Override
        public @NotNull SheepColorChangeConsequence make(@NonNull Random random, double points) {
            return new SheepColorChangeConsequence();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.sheepColorChange.enabled() ? Mystical.CONFIG.sheepColorChange.weight() : 0);
        }
    }
}
