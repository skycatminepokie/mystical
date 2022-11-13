package skycat.mystical.curses;

import net.minecraft.stat.StatType;

public class TypedRemovalCondition<T> extends CurseRemovalCondition {
    StatType<T> statType; // ex MINED
    T statValue; // ex Blocks.COBBLESTONE

}
