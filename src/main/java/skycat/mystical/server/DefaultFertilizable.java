package skycat.mystical.server;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.UndergroundConfiguredFeatures;

public interface DefaultFertilizable extends Fertilizable { // This is from MossBlock.java
    @Override
    default boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    default boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    default void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        UndergroundConfiguredFeatures.MOSS_PATCH_BONEMEAL.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
        // PileConfiguredFeatures.PILE_HAY.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
        // NetherConfiguredFeatures.BASALT_BLOBS.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
        // MiscConfiguredFeatures.DESERT_WELL.value().generate(world, world.getChunkManager().getChunkGenerator(), random, pos.up());
    }
}
