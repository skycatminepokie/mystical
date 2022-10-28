package skycat.mystical.curses;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

public class StartSleepingCurse extends Curse implements EntitySleepEvents.StartSleeping {
    private final EntitySleepEvents.StartSleeping callback;

    public StartSleepingCurse(EntitySleepEvents.StartSleeping callback, double difficultyMultiplier) {
        this.callback = callback;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        callback.onStartSleeping(entity, sleepingPos);
    }

    @Override
    public void register() {
        EntitySleepEvents.START_SLEEPING.register(this);
    }

}
