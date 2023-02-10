package skycat.mystical.mixin;

import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import skycat.mystical.server.DefaultFertilizable;

@Mixin(Block.class)
public class BlockFertilizableMixin implements DefaultFertilizable {
}
