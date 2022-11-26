package skycat.mystical.curses;

import net.minecraft.stat.StatType;

public class TypedRemovalCondition<T> extends CurseRemovalCondition {
    StatType<T> statType; // ex MINED is a StatType<Block>
    T statValue; // ex Blocks.COBBLESTONE

    public TypedRemovalCondition(StatType<T> statType, T statValue, double amountRequired) {
        super(amountRequired, 0);
        this.statType = statType;
        this.statValue = statValue;
    }
}
