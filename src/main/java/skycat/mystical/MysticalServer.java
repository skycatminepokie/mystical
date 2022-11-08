package skycat.mystical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skycat.mystical.curses.CurseHandler;


public class MysticalServer implements DedicatedServerModInitializer {
    @Getter public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setVersion(1.0).create();
    @Getter public static final Logger LOGGER = LoggerFactory.getLogger("mystical");
    @Getter public static Save SAVE = Save.load();
    @Getter public static final MysticalEventHandler EVENT_HANDLER = new MysticalEventHandler();
    @Getter public static final CurseHandler CURSE_HANDLER = new CurseHandler();

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(EVENT_HANDLER);
        ServerLifecycleEvents.SERVER_STOPPING.register(EVENT_HANDLER);
    }
}
