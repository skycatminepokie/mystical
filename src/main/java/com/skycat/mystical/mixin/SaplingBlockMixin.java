package com.skycat.mystical.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.RandomTreeTypeConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin {
    @ModifyReceiver(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/sapling/SaplingGenerator;generate(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/random/Random;)Z"))
    public SaplingGenerator mystical_newSaplingType(SaplingGenerator instance, ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random) {
        if (!Mystical.isClientWorld() &&
                !Mystical.getHavenManager().isInHaven(pos) &&
                Mystical.getSpellHandler().isConsequenceActive(RandomTreeTypeConsequence.class) &&
                Utils.percentChance(Mystical.CONFIG.randomTreeType.chance())) {
            Utils.log(Utils.translateString("text.mystical.consequence.randomTreeType.fired"), Mystical.CONFIG.randomTreeType.logLevel());
            return Util.getRandom(RandomTreeTypeConsequence.SAPLING_GENERATORS, random);
        }
        return instance;
    }
}
