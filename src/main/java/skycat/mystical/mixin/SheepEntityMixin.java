package skycat.mystical.mixin;

import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skycat.mystical.Mystical;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin {
    @Shadow public abstract void setColor(DyeColor color);

    @Inject(method = "onEatingGrass", at = @At("TAIL"))
    public void afterEatGrass(CallbackInfo ci) {
        if (Mystical.SPELL_HANDLER.shouldSheepChangeColor()) { // TODO: Logging
            setColor(Util.getRandom(DyeColor.values(), Mystical.MC_RANDOM));
        }
    }
}
