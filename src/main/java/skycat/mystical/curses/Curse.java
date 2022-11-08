package skycat.mystical.curses;

import net.fabricmc.fabric.api.event.Event;

public class Curse<T, S> {
    public final Event<T> event;
    public final T callback;
    public CurseRemovalCondition<S> removalCondition;
    double difficultyMultiplier;

    public Curse(Event<T> event, T callback, double difficultyMultiplier) {
        this.event = event;
        this.callback = callback;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public Curse(Event<T> event, T callback, CurseRemovalCondition<S> removalCondition, double difficultyMultiplier) {
        this.event = event;
        this.callback = callback;
        this.removalCondition = removalCondition;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public void register() {
        event.register(callback);
    }
}
