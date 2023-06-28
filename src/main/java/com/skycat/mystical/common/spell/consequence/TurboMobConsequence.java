package com.skycat.mystical.common.spell.consequence;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.LogLevel;
import com.skycat.mystical.common.util.Utils;
import lombok.NonNull;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.text.MutableText;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TurboMobConsequence extends SpellConsequence { // TODO: Maybe make this work with wardens?
    public static final Factory FACTORY = new Factory();
    public EntityType<?> entityType;

    public TurboMobConsequence(EntityType<?> entityType) {
        super(TurboMobConsequence.class, null, 50d);
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
            var entry = Registry.ENTITY_TYPE.getRandom(Mystical.MC_RANDOM);
            if (entry.isPresent()) {
                EntityType<?> type = entry.get().value();
                SpawnGroup spawnGroup = type.getSpawnGroup();
                if (spawnGroup != null && spawnGroup != SpawnGroup.MISC && type != EntityType.GIANT) { // TODO: Use EntityTypePredicate, TODO: config
                    return new TurboMobConsequence(type);
                } else {
                    Utils.log("Making TurboMobConsequence: skipping unspawnable type: " + type.getName().getString(), LogLevel.INFO);
                    return make(random, points); // try again
                }
            }
            // TODO: Logging - couldn't get random EntityType
            return new TurboMobConsequence(EntityType.ZOMBIE);
        }

        @Override
        public double getWeight() {
            return 0; // TODO
        }

        @Override
        public MutableText getDescriptionText(SpellConsequence consequence) throws IllegalArgumentException {
            if (!(consequence instanceof TurboMobConsequence turboMobConsequence)) {
                throw new IllegalArgumentException("Consequence is not a TurboMobConsequence: " + consequence); // TODO: Logging instead of crashing
            }

            return Utils.translatable(getDescription(), turboMobConsequence.entityType.getName().getString());
        }
    }
}
