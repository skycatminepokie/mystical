package skycat.mystical.curses;

import net.fabricmc.fabric.api.event.Event;

public class Curse<T> {
    public final Event<T> event;
    public final T callback;

    public Curse(Event<T> event, T callback, double difficultyMultiplier) {
        this.event = event;
        this.callback = callback;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    double difficultyMultiplier;

    public void register() {
        event.register(callback);
    }
}
