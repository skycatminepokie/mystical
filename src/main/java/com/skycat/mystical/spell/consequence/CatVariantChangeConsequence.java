package com.skycat.mystical.spell.consequence;

import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CatVariantChangeConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    public CatVariantChangeConsequence() { // TODO: Config
        super(CatVariantChangeConsequence.class, CatVariantChangeConsequence.class, "catVariantChange", "Cat Variant Change", "We change coats, so why can't cats do the same?", "Changed cat variant");
    }

    public static class Factory implements ConsequenceFactory<CatVariantChangeConsequence> {
        @Override
        public @NotNull CatVariantChangeConsequence make(@NonNull Random random, double points) {
            return new CatVariantChangeConsequence();
        }
        @Override
        public double getWeight() {
            return (Mystical.CONFIG.catVariantChange.enabled()? Mystical.CONFIG.catVariantChange.weight():0);
        }
    }
}
