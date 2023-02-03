package skycat.mystical.spell.consequence;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

public class KillOnSleepConsequence extends SpellConsequence implements EntitySleepEvents.StartSleeping {
    public KillOnSleepConsequence() {
        super(KillOnSleepConsequence.class);
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        if (entity.isAlive()) {
            entity.kill();
        }
    }
}
