package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.SheepColorChangeConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin {
    @Shadow public abstract void setColor(DyeColor color);

    @Inject(method = "onEatingGrass", at = @At("TAIL"))
    public void afterEatGrass(CallbackInfo ci) {
        if (Mystical.SPELL_HANDLER.isConsequenceActive(SheepColorChangeConsequence.class) && Utils.percentChance(Mystical.CONFIG.sheepColorChange.chance())) {
            setColor(Util.getRandom(DyeColor.values(), Mystical.MC_RANDOM));
            Utils.log(Utils.translateString("text.mystical.consequence.sheepColorChange.fired"), Mystical.CONFIG.sheepColorChange.logLevel()); // TODO: Translate
        }
    }
}