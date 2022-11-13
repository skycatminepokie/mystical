package skycat.mystical.curses;

import net.minecraft.stat.StatType;

/**
 * Suggestion from a member of the community: call it magic crap
 */
public class CurseRemovalCondition <T> {
    public double amountRequired;
    public double amountFulfilled;
    StatType<T> statType; // MINED, BROKEN, etc.
    /**
     * The object that the stat is interested in. For example, if
     * {@code T} is a {@link net.minecraft.block.Block}, this could be {@link net.minecraft.block.Blocks#STONE}
     */
    T statObject;

    public CurseRemovalCondition(StatType<T> statType, T statObject, double amountRequired, double amountFulfilled) {
        this.statType = statType;
        this.statObject = statObject;
        this.amountRequired = amountRequired;
        this.amountFulfilled = amountFulfilled;
    }

    public CurseRemovalCondition(StatType<T> statType, T statObject, double amountRequired) {
        this.statType = statType;
        this.statObject = statObject;
        this.amountRequired = amountRequired;
        this.amountFulfilled = 0;
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

    public boolean isFulfilled() {
        return amountFulfilled >= amountRequired;
    }
}
