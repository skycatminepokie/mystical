package com.skycat.mystical.spell.consequence;

import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SkeletonTypeChangeConsequence extends SpellConsequence {
    public static final ArrayList<EntityType<? extends MobEntity>> SKELETON_TYPES = new ArrayList<>(); // TODO: Config
    public static final Factory FACTORY = new Factory();

    static {
        Collections.addAll(SKELETON_TYPES,
                EntityType.SKELETON,
                EntityType.WITHER_SKELETON,
                EntityType.STRAY
        );
    }

    protected SkeletonTypeChangeConsequence() {
        super(SkeletonTypeChangeConsequence.class, SkeletonTypeChangeConsequence.class, "skeletonTypeChange", "Skeleton Type Change", "Skeletons are having a wardrobe crisis too!", "Skeleton type changed");
    }

    public static class Factory extends ConsequenceFactory<SkeletonTypeChangeConsequence> {
        public Factory() {
            super("skeletonTypeChange", "Skeleton Type Change", "Skeletons are having a wardrobe crisis too!", "Skeleton type changed", SkeletonTypeChangeConsequence.class);
        }

        @Override
        public @NotNull SkeletonTypeChangeConsequence make(@NonNull Random random, double points) {
            return new SkeletonTypeChangeConsequence();
        }

        @Override
        public double getWeight() {
            return (Mystical.CONFIG.skeletonTypeChange.enabled() ? Mystical.CONFIG.skeletonTypeChange.weight() : 0);
        }
    }
}
