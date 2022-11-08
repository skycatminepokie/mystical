package skycat.mystical.curses;

import net.minecraft.stat.StatType;

/**
 * Suggestion from a member of the community: call it magic crap
 */
public class CurseRemovalCondition <T> {
    public double amountRequired;
    public double amountFulfilled;
    StatType<T> statType;
    T stat;

    public CurseRemovalCondition(StatType<T> statType, T stat, double amountRequired, double amountFulfilled) {
        this.statType = statType;
        this.stat = stat;
        this.amountRequired = amountRequired;
        this.amountFulfilled = amountFulfilled;
    }

    /**
     * Fulfill the condition, in part or in whole
     * @param amountToFulfill How many actions have been recorded/fulfilled
     * @return true if the condition has been satisfied
     */
    public boolean fulfill(double amountToFulfill) {
        amountFulfilled += amountToFulfill;
        return amountFulfilled >= amountRequired;
    }

}
