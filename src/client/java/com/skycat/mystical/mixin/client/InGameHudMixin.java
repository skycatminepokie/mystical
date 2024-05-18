package com.skycat.mystical.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.skycat.mystical.MysticalClient;
import com.skycat.mystical.polymer.ConsequenceStatusEffect;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @ModifyArg(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    private Identifier mystical_useSpellOutline(Identifier original, @Local StatusEffectInstance statusEffectInstance) {
        if (statusEffectInstance.getEffectType().value() instanceof ConsequenceStatusEffect) { // TODO: This doesn't work. Need to figure out why.
            return MysticalClient.SPELL_OUTLINE;
        }
        return original;
    }
}
