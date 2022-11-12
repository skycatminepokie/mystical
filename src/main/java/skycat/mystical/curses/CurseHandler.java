package skycat.mystical.curses;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.stat.Stat;
import skycat.mystical.MysticalServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CurseHandler {
    public final ArrayList<Curse> curses = new ArrayList<>();
    public HashMap<UUID, HashMap<Stat<?>, Double>> lastStats = new HashMap<>();

    public CurseHandler() {
        // Initialize curses
        Curse curse = new Curse<>(EntitySleepEvents.START_SLEEPING, (entity, sleepingPos) -> entity.kill(), 1.0);
        curse.register();
        curses.add(curse);

    }

    public void doNighttimeEvents() {
        updateCurseFulfillment();
    }

    /**
     * Update curses and their removal conditions. Will not remove the effects of curses.
     */
    public void updateCurseFulfillment() {
        // For each player
        // For each curse in use by a stat
        // Find diff
        // Save
        for (UUID playerUUID : lastStats.keySet()) {
            if (MysticalServer.getEVENT_HANDLER().getServer().getPlayerManager().getPlayer(playerUUID) != null) {

            }
        }
    }

    public void removeFulfilledCurses() {

    }

    // TODO Needs to update amount fulfilled of curses each night and when players disconnect

    // Get random curse
    // Get random curse, but weight chances based on difficulty
    // Get random curse, but weight chances to get close to a difficulty
}
