package skycat.mystical.curses;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;

import java.util.ArrayList;

public class CurseHandler {
    public final ArrayList<Curse> curses = new ArrayList<>();

    public CurseHandler() {
        // Initialize curses
        Curse curse = new Curse<>(EntitySleepEvents.START_SLEEPING, (entity, sleepingPos) -> entity.kill(), 1.0);
        curse.register();
        curses.add(curse);

    }

    // TODO Needs to update amount fulfilled of curses each night

    // Get random curse
    // Get random curse, but weight chances based on difficulty
    // Get random curse, but weight chances to get close to a difficulty
}
