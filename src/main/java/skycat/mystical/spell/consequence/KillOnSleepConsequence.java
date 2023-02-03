package skycat.mystical.spell.consequence;

import lombok.NonNull;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import skycat.mystical.util.EventCallbackEnum;

import java.util.Random;

public class KillOnSleepConsequence extends SpellConsequence implements ConsequenceFactory<KillOnSleepConsequence>, EntitySleepEvents.StartSleeping {
    public KillOnSleepConsequence() {
        super(SpellConsequenceType.KILL_ON_SLEEP, EventCallbackEnum.SLEEP_START);
    }

    @Override
    public @NotNull KillOnSleepConsequence make(@NonNull Random random, double points) {
        return new KillOnSleepConsequence();
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        if (entity.isAlive()) {
            entity.kill();
        }
    }
}
