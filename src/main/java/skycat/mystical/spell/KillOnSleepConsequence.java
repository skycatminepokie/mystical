package skycat.mystical.spell;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import skycat.mystical.util.SpellConsequenceType;

public class KillOnSleepConsequence extends SpellConsequence implements EntitySleepEvents.StartSleeping {
    public KillOnSleepConsequence() {
        super(SpellConsequenceType.KILL_ON_SLEEP);
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        if (entity.isAlive()) {
            entity.kill();
        }
    }
}
