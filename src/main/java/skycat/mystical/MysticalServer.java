package skycat.mystical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.util.math.random.CheckedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class MysticalServer implements DedicatedServerModInitializer {
    @Getter public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setVersion(1.0).create();
    @Getter public static final Logger LOGGER = LoggerFactory.getLogger("mystical");
    @Getter public static Save SAVE = Save.load();
    @Getter public static final MysticalEventHandler EVENT_HANDLER = new MysticalEventHandler();
    @Getter public static final Random RANDOM = new Random();
    @Getter public static final net.minecraft.util.math.random.Random MC_RANDOM = new CheckedRandom(RANDOM.nextLong()); // WARN: Probably a horrible way to do this

    public static final skycat.mystical.MysticalConfig CONFIG = skycat.mystical.MysticalConfig.createAndLoad();

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(EVENT_HANDLER);
        ServerLifecycleEvents.SERVER_STOPPING.register(EVENT_HANDLER);
        EntitySleepEvents.START_SLEEPING.register(SAVE.getCurseHandler());
        PlayerBlockBreakEvents.BEFORE.register(SAVE.getCurseHandler());
    }
}
