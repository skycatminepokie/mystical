package skycat.mystical.spell.consequence;

import lombok.NonNull;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class KillOnSleepConsequence extends SpellConsequence implements EntitySleepEvents.StartSleeping {
    public static final ConsequenceFactory<KillOnSleepConsequence> FACTORY = new Factory();

    protected KillOnSleepConsequence() {
        super(KillOnSleepConsequence.class, EntitySleepEvents.StartSleeping.class);
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        if (entity.isAlive()) {
            entity.kill();
        }
    }

    private static class Factory implements ConsequenceFactory<KillOnSleepConsequence> {
        @Override
        public @NotNull KillOnSleepConsequence make(@NonNull Random random, double points) {
            // TODO: Take points into account
            return new KillOnSleepConsequence();
        }

        @Override
        public double getWeight() {
            return 0; // WARN debug
        }
    }
}
