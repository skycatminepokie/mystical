package skycat.mystical.curses;

public class Curse {
    public CurseConsequence consequence;
    public CurseRemovalCondition removalCondition;
    boolean enabled;

    public Curse(@SuppressWarnings("rawtypes") CurseConsequence consequence, CurseRemovalCondition removalCondition) {
        this.consequence = consequence;
        this.removalCondition = removalCondition;
    }

    public void disable() {
        enabled = false;
    }

    public void enable(){
        enabled = true;
    }
}
