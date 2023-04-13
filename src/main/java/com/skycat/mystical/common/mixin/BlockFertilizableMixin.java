package com.skycat.mystical.common.mixin;

import com.skycat.mystical.common.DefaultFertilizable;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public abstract class BlockFertilizableMixin implements DefaultFertilizable {
}
