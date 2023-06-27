package com.skycat.mystical.common.spell.consequence;

import lombok.NonNull;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TurboMobConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    public EntityType<?> entityType;

    public TurboMobConsequence(EntityType<?> entityType) {
        super(TurboMobConsequence.class, null, 50d); // TODO: Difficulty
        this.entityType = entityType;
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<TurboMobConsequence> {

        protected Factory() {
            super("turboMob", "Turbo Mob", "%s go zoom", "Gave a mob extra speed.", TurboMobConsequence.class);
        }

        @Override
        public @NotNull TurboMobConsequence make(@NonNull Random random, double points) {
            return new TurboMobConsequence(EntityType.ZOMBIE); // TODO: Other entity types
        }

        @Override
        public double getWeight() {
            return 0; // TODO
        }
    }
}
