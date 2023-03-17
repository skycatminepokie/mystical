package skycat.mystical.spell.consequence;

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
        super(ZombieTypeChangeConsequence.class, ZombieTypeChangeConsequence.class, "zombieTypeChange", "Zombie Type Change", "Zombies are having a wardrobe crisis");
    }

    public static class Factory implements ConsequenceFactory<ZombieTypeChangeConsequence> {

        @Override
        public @NotNull ZombieTypeChangeConsequence make(@NonNull Random random, double points) {
            return new ZombieTypeChangeConsequence();
        }
    }
}
