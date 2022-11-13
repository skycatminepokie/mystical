package skycat.mystical.curses;

import net.fabricmc.fabric.api.event.Event;

public class Curse<T> {
    public final Event<T> event;
    public final T callback;
    public CurseRemovalCondition<?> removalCondition;
    double difficultyMultiplier;
    boolean enabled = true;

    public Curse(Event<T> event, T callback, double difficultyMultiplier) {
        this.event = event;
        this.callback = callback;
        this.difficultyMultiplier = difficultyMultiplier;
        this.removalCondition = null; // WARN This will probably cause problems
    }

    public Curse(Event<T> event, T callback, CurseRemovalCondition<?> removalCondition, double difficultyMultiplier) {
        this.event = event;
        this.callback = callback;
        this.removalCondition = removalCondition;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public void disable() {
        enabled = false;
    }

    public void enable(){
        enabled = true;
    }

    /**
     * @deprecated in favor of registering only one of each listener in CurseHandler
     */
    public void register() {
        event.register(callback);
    }
}
