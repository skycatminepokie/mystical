package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.ExplosionsInfestConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.InfestedBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.ListIterator;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow public abstract List<BlockPos> getAffectedBlocks();

    @Shadow @Final private World world;

    @Inject(method = "affectWorld", at = @At("HEAD"))
    private void mystical_infestBlocks(boolean particles, CallbackInfo ci) {
        if (!Mystical.isClientWorld() &&
                Mystical.getSpellHandler().isConsequenceActive(ExplosionsInfestConsequence.class)) {
            ListIterator<BlockPos> it = getAffectedBlocks().listIterator();
            while (it.hasNext()) {
                BlockPos blockPos = it.next();
                if (Mystical.getHavenManager().isInHaven(blockPos)) continue;
                BlockState blockState = world.getBlockState(blockPos);
                if (Utils.percentChance(Mystical.CONFIG.explosionsInfest.chance()) && InfestedBlock.isInfestable(blockState)) {
                    world.setBlockState(blockPos, InfestedBlock.fromRegularState(blockState));
                    it.remove();
                }
            }
        }
    }
}
