package skycat.mystical;

import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.util.math.random.CheckedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skycat.mystical.curses.CurseHandler;

import java.util.Random;


public class MysticalServer implements DedicatedServerModInitializer {
    @Getter public static final Logger LOGGER = LoggerFactory.getLogger("mystical");

    @Getter public static final MysticalEventHandler EVENT_HANDLER = new MysticalEventHandler();
    @Getter public static final Random RANDOM = new Random();
    @Getter public static final net.minecraft.util.math.random.Random MC_RANDOM = new CheckedRandom(RANDOM.nextLong()); // WARN: Probably not a great way to do this
    public static final skycat.mystical.MysticalConfig CONFIG = skycat.mystical.MysticalConfig.createAndLoad();
    public static final CurseHandler CURSE_HANDLER = new CurseHandler();

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(EVENT_HANDLER);
        ServerLifecycleEvents.SERVER_STOPPING.register(EVENT_HANDLER);
        EntitySleepEvents.START_SLEEPING.register(CURSE_HANDLER);
        PlayerBlockBreakEvents.BEFORE.register(CURSE_HANDLER);
        ServerEntityEvents.EQUIPMENT_CHANGE.register(CURSE_HANDLER);
    }
}
