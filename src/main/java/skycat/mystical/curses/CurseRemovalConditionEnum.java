package skycat.mystical.curses;

import net.minecraft.block.Blocks;
import net.minecraft.stat.Stats;

import java.util.HashMap;

public enum CurseRemovalConditionEnum {
    MINE_COBBLESTONE("MINE_COBBLESTONE");
    public final String removalCondition;

    CurseRemovalConditionEnum(String removalConditionEnum) {
        this.removalCondition = removalConditionEnum;
    }
    private static final HashMap<CurseRemovalConditionEnum, CurseRemovalCondition> lookupMap = new HashMap<>();
    static {
        lookupMap.put(MINE_COBBLESTONE, new TypedRemovalCondition<>(Stats.MINED, Blocks.COBBLESTONE, 10));
    }

    public static CurseRemovalCondition lookup(CurseRemovalConditionEnum removalConditionEnum) {
        return lookupMap.get(removalConditionEnum);
    }
}
