package skycat.mystical.curses;

public class Curse {
    CurseConsequence consequence;
    CurseRemovalCondition removalCondition;
    boolean enabled;

    public Curse(CurseConsequence consequence, CurseRemovalCondition removalCondition) {
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
