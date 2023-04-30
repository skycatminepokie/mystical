package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.SheepColorChangeConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin implements EntityLike {
    @Shadow public abstract void setColor(DyeColor color);

    @Inject(method = "onEatingGrass", at = @At("TAIL"))
    public void afterEatGrass(CallbackInfo ci) {
        // https://www.reddit.com/r/fabricmc/comments/kfrsq7/comment/ggactl1/
        if (Mystical.HAVEN_MANAGER.isInHaven(getBlockPos()) &&
                Mystical.SPELL_HANDLER.isConsequenceActive(SheepColorChangeConsequence.class) && Utils.percentChance(Mystical.CONFIG.sheepColorChange.chance())) {
            setColor(Util.getRandom(DyeColor.values(), Mystical.MC_RANDOM));
            Utils.log(Utils.translateString("text.mystical.consequence.sheepColorChange.fired"), Mystical.CONFIG.sheepColorChange.logLevel());
        }
    }
}
