package com.skycat.mystical.common.spell.consequence;

import com.skycat.mystical.Mystical;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NoFuseConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    public NoFuseConsequence() {
        super(NoFuseConsequence.class, NoFuseConsequence.class, 50d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<NoFuseConsequence> {
        protected Factory() {
            super("noFuse", "No Fuses", "Creepers don't hesitate anymore.", "Destroyed a fuse", NoFuseConsequence.class);
        }

        @Override
        public @NotNull NoFuseConsequence make(@NonNull Random random, double points) {
            return new NoFuseConsequence();
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.noFuse.enabled() ? Mystical.CONFIG.noFuse.weight() : 0);
        }
    }
}
