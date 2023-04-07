package com.skycat.mystical.mixin;

import com.skycat.mystical.server.DefaultFertilizable;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public abstract class BlockFertilizableMixin implements DefaultFertilizable {
}
