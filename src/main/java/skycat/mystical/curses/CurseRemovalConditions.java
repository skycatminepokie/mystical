package skycat.mystical.curses;

import net.minecraft.block.Blocks;
import net.minecraft.stat.Stats;

public enum CurseRemovalConditions {
    MINE_COBBLESTONE(new TypedRemovalCondition<>(Stats.MINED, Blocks.COBBLESTONE, 10));

    public final CurseRemovalCondition removalCondition;

    CurseRemovalConditions(CurseRemovalCondition removalCondition) {
        this.removalCondition = removalCondition;
    }
}
