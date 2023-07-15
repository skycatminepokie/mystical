package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CatVariantChangeConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    @Override
    public @NotNull ConsequenceFactory<CatVariantChangeConsequence> getFactory() {
        return FACTORY;
    }

    public CatVariantChangeConsequence() { // TODO: Config on variants
        super(CatVariantChangeConsequence.class, null, 1d);  // TODO: Scaling
    }

    public static class Factory extends ConsequenceFactory<CatVariantChangeConsequence> {
        public Factory() {
            super("catVariantChange",
                    "Cat Variant Change",
                    "We change coats, so why can't cats do the same?",
                    "Changed cat variant",
                    CatVariantChangeConsequence.class,
                    Codec.unit(CatVariantChangeConsequence::new));
        }

        @Override
        public @NotNull CatVariantChangeConsequence make(@NonNull Random random, double points) {
            return new CatVariantChangeConsequence();
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.catVariantChange.enabled() ? Mystical.CONFIG.catVariantChange.weight() : 0);
        }
    }
}
