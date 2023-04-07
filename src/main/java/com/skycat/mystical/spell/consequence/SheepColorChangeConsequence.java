package com.skycat.mystical.spell.consequence;

import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SheepColorChangeConsequence extends SpellConsequence { // TODO: Config
    public static final Factory FACTORY = new Factory();
    public SheepColorChangeConsequence() {
        super(SheepColorChangeConsequence.class, SheepColorChangeConsequence.class, "sheepColorChange", "Sheep Color Change", "The fluffy marshmallows got jealous of _jeb.");
    }

    public static class Factory implements ConsequenceFactory<SheepColorChangeConsequence> {
        @Override
        public @NotNull SheepColorChangeConsequence make(@NonNull Random random, double points) {
            return new SheepColorChangeConsequence();
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.sheepColorChange.enabled()?Mystical.CONFIG.sheepColorChange.weight():0);
        }
    }
}
