package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.RandomTreeTypeConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin {
    @Shadow @Final public static IntProperty STAGE;

    @Inject(method = "generate", at = @At("HEAD"), cancellable = true)
    public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random, CallbackInfo ci) {
        if (state.get(STAGE) != 0 &&
                Mystical.HAVEN_MANAGER.isInHaven(pos) &&
                Mystical.SPELL_HANDLER.isConsequenceActive(RandomTreeTypeConsequence.class) &&
                Utils.percentChance(Mystical.CONFIG.randomTreeType.chance())) {
            Util.getRandom(RandomTreeTypeConsequence.SAPLING_GENERATORS, random).generate(world, world.getChunkManager().getChunkGenerator(), pos, state, random);
            Utils.log("text.mystical.consequence.randomTreeType.fired", Mystical.CONFIG.randomTreeType.logLevel());
            ci.cancel();
        }
    }
}
