package skycat.mystical;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class MysticalEventHandler implements ServerLifecycleEvents.ServerStarted {
    MinecraftServer server;
    MinecraftServerTimerAccess timerAccess;

    @Override
    public void onServerStarted(MinecraftServer server) {
        this.server = server;
        timerAccess = ((MinecraftServerTimerAccess) server);
        setNightTimer();
        Utils.log("Time of day is " + server.getOverworld().getTimeOfDay(), Settings.LoggingSettings.getTimeOfDayAtStartup());
    }

    public void doNighttimeEvents() {
        try {
            setNightTimer();
        } catch (NullPointerException e) {
            Utils.log("Couldn't set timer for night. Reason: " + e.getMessage(), Settings.LoggingSettings.getNightTimerSetFailed());
            // TODO: Try again later
        }
    }

    /**
     * Sets the nighttime timer to the next night
     * @return Number of ticks until night, or -1 on failure
     */
    public long setNightTimer() throws NullPointerException {
        // CREDIT: Daomephsta#0044 for help on fabric discord
        if (server == null) {
            throw new NullPointerException("server was null in setNightTimer.");
        }
        if (timerAccess == null) {
            throw new NullPointerException("timerAccess was null in setNightTimer.");
        }
        long timerLength = 18000 - (server.getOverworld().getTimeOfDay() % 24000);
        if (timerLength == 0) { // So that we don't get repeating events if this is fired twice in a tick.
            timerLength = 18000;
        }
        timerAccess.mystical_setTimer(timerLength);
        return timerLength;
    }
}
