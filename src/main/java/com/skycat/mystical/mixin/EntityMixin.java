package com.skycat.mystical.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.FireImmunitySwapConsequence;
import com.skycat.mystical.spell.consequence.NoPortalsConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract int getBlockX();

    @Shadow public abstract int getBlockZ();

    @ModifyReturnValue(method = "isFireImmune", at = @At("RETURN"))
    private boolean mystical_flipFireImmunity(boolean original) {
        if (Mystical.getSpellHandler().isConsequenceActive(FireImmunitySwapConsequence.class) &&
            !Mystical.getHavenManager().isInHaven(getBlockX(), getBlockZ())) {
            Utils.log(Utils.translateString("text.mystical.consequence.fireImmunitySwap.fired"), Mystical.CONFIG.fireImmunitySwap.logLevel());
            return !original;
        }
        return original;
    }

    @ModifyReturnValue(method = "canUsePortals", at = @At("RETURN"))
    private boolean mystical_preventPortalUsage(boolean original) {
        if (Mystical.getSpellHandler().isConsequenceActive(NoPortalsConsequence.class) &&
                !Mystical.getHavenManager().isInHaven(getBlockX(), getBlockZ())) {
            Utils.log(Utils.translateString("text.mystical.consequence.noPortals.fired"), Mystical.CONFIG.noPortals.logLevel());
            return false;
        }
        return original;
    }
}
