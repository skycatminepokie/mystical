package skycat.mystical.curses;

public class Curse {
    private final CurseConsequenceEnum consequenceEnum;
    private final CurseRemovalConditionEnum removalConditionEnum;

    public Curse(CurseConsequenceEnum consequenceEnum, CurseRemovalConditionEnum removalConditionEnum) {
        this.consequenceEnum = consequenceEnum;
        this.removalConditionEnum = removalConditionEnum;
    }


    @SuppressWarnings("rawtypes")
    public CurseConsequence getConsequence() {
        return CurseConsequenceEnum.lookup(consequenceEnum);
    }

    public CurseConsequenceEnum getConsequenceEnum() {
        return consequenceEnum;
    }

    public CurseRemovalCondition getRemovalCondition() {
        return CurseRemovalConditionEnum.lookup(removalConditionEnum);
    }

    public CurseRemovalConditionEnum getRemovalConditionEnum() {
        return removalConditionEnum;
    }

}
