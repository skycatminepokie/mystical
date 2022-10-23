package skycat.mystical;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class MysticalEventHandler implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        // CREDIT: Daomephsta#0044 for help on fabric discord
        ((MinecraftServerTimerAccess) server).mystical_setTimer(18000 - (server.getOverworld().getTimeOfDay() % 24000)); // This formula is completely wrong
        Utils.log("time of day is " + server.getOverworld().getTimeOfDay());
    }

    public void doNighttimeEvents() {
        Utils.log("wow it's night"); // STOPSHIP
    }
}
