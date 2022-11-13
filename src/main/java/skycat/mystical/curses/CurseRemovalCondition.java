package skycat.mystical.curses;

/**
 * Suggestion from a member of the community: call it magic crap
 */
public abstract class CurseRemovalCondition {
    public double amountRequired;
    public double amountFulfilled;

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
