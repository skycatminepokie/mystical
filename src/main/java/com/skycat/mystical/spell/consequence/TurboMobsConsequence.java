package com.skycat.mystical.spell.consequence;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.util.LogLevel;
import com.skycat.mystical.util.Utils;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TurboMobsConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();
    @Getter public EntityType<?> entityType;

    public TurboMobsConsequence(EntityType<?> entityType) {
        super(TurboMobsConsequence.class, null, 50d);
        this.entityType = entityType;
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<TurboMobsConsequence> {

        protected Factory() {
            super("turboMobs",
                    "Turbo Mobs",
                    "%s go zoom",
                    "Gave a mob extra speed.",
                    TurboMobsConsequence.class,
                    Registries.ENTITY_TYPE.getCodec().xmap(TurboMobsConsequence::new, TurboMobsConsequence::getEntityType));
        }

        @Override
        public @NotNull TurboMobsConsequence make(@NonNull Random random, double points) {
            var entry = Registries.ENTITY_TYPE.getRandom(Mystical.MC_RANDOM);
            if (entry.isPresent()) {
                EntityType<?> type = entry.get().value();
                SpawnGroup spawnGroup = type.getSpawnGroup();
                if (spawnGroup != null && spawnGroup != SpawnGroup.MISC && type != EntityType.GIANT) {
                    return new TurboMobsConsequence(type);
                } else {
                    Utils.log("Making TurboMobsConsequence: skipping unspawnable type: " + type.getName().getString(), LogLevel.INFO);
                    return make(random, points); // try again
                }
            }
            Utils.log(Utils.translateString("text.mystical.consequence.turboMobs.failedGetRandomEntityType"), LogLevel.ERROR);
            return new TurboMobsConsequence(EntityType.ZOMBIE);
        }


        @Override
        public double getWeight() {
            return Mystical.CONFIG.turboMobs.enabled() ? Mystical.CONFIG.turboMobs.weight() : 0;
        }


        @Override
        public MutableText getDescriptionText(SpellConsequence consequence) throws IllegalArgumentException {
            if (!(consequence instanceof TurboMobsConsequence turboMobsConsequence)) {
                throw new IllegalArgumentException("Consequence is not a TurboMobsConsequence: " + consequence);
            }

            return Utils.translatable(getDescription(), turboMobsConsequence.entityType.getName().getString());
        }
    }
}
