package skycat.mystical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MysticalServer implements DedicatedServerModInitializer {
    @Getter public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setVersion(1.0).create();
    @Getter public static final Logger LOGGER = LoggerFactory.getLogger("mystical");
    @Getter public static Save SAVE = Save.load();
    @Getter public static final MysticalEventHandler EVENT_HANDLER = new MysticalEventHandler();

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(EVENT_HANDLER);

    }
}
