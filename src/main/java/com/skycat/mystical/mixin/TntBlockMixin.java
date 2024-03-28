package com.skycat.mystical.mixin;

import net.minecraft.block.TntBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin {
    /**
     * Used to fix an error with noFuse consequence (stops the argument of nextInt from being <= 0)
     * @param bound The previously chosen bound.
     * @return 1 or bound, whichever is greater.
     */
    @ModifyArg(method = "onDestroyedByExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"), index = 0) // TODO ModifyReturnValue instead
    private int mystical_minRandomOne(int bound) {
        return Math.max(1, bound);
    }
}
