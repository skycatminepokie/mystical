package com.skycat.mystical.common.spell.consequence;

import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ZombieTypeChangeConsequence extends SpellConsequence {
    public static final ArrayList<EntityType<? extends MobEntity>> ZOMBIE_TYPES = new ArrayList<>(); // TODO: Config
    public static final Factory FACTORY = new Factory();

    @Override
    public @NotNull ConsequenceFactory<ZombieTypeChangeConsequence> getFactory() {
        return FACTORY;
    }

    static {
        Collections.addAll(ZOMBIE_TYPES,
                EntityType.ZOMBIE,
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.DROWNED,
                EntityType.HUSK
        );
    }

    public ZombieTypeChangeConsequence() {
        super(ZombieTypeChangeConsequence.class, null);
    }

    public static class Factory extends ConsequenceFactory<ZombieTypeChangeConsequence> {

        public Factory() {
            super("zombieTypeChange", "Zombie Type Change", "Zombies are having a wardrobe crisis", "Zombie type changed.", ZombieTypeChangeConsequence.class);
        }

        @Override
        public @NotNull ZombieTypeChangeConsequence make(@NonNull Random random, double points) {
            return new ZombieTypeChangeConsequence();
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.zombieTypeChange.enabled() ? Mystical.CONFIG.zombieTypeChange.weight() : 0);
        }
    }
}
