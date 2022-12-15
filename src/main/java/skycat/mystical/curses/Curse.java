package skycat.mystical.curses;

public class Curse {
    private final CurseConsequences consequenceEnum;
    public CurseRemovalCondition removalCondition;
    boolean enabled;

    public Curse(CurseConsequences consequenceEnum, CurseRemovalCondition removalCondition) {
        this.consequenceEnum = consequenceEnum;
        this.removalCondition = removalCondition;
    }

    @SuppressWarnings("rawtypes")
    public CurseConsequence getConsequenceEnum() {
        return consequenceEnum.consequence;
    }

    public void disable() {
        enabled = false;
    }

    public void enable(){
        enabled = true;
    }

}
