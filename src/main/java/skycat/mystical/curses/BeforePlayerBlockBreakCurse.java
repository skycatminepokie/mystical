package skycat.mystical.curses;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BeforePlayerBlockBreakCurse extends Curse implements PlayerBlockBreakEvents.Before {
    private final PlayerBlockBreakEvents.Before callback;

    public BeforePlayerBlockBreakCurse(PlayerBlockBreakEvents.Before callback, double difficultyMultiplier) {
        this.callback = callback;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        return callback.beforeBlockBreak(world, player, pos, state, blockEntity);
    }

    @Override
    void register() {
         PlayerBlockBreakEvents.BEFORE.register(callback);
    }
}
