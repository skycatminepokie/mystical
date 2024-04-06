package com.skycat.mystical.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.skycat.mystical.SpellStatusEffect;
import com.skycat.mystical.HudManager;
import com.skycat.mystical.MysticalClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @ModifyVariable(method = "renderStatusEffectOverlay", at = @At(value = "STORE", ordinal = 0))
    public Collection<StatusEffectInstance> mystical_addFakeStatusEffect(Collection<StatusEffectInstance> original) {
        ArrayList<StatusEffectInstance> list = new ArrayList<>(List.of(original.toArray(new StatusEffectInstance[0])));
        list.addAll(MysticalClient.HUD_MANAGER.getFakeStatusEffects());
        return list;
    }

    @ModifyArg(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    public Identifier mystical_useSpellOutline(Identifier original, @Local StatusEffectInstance statusEffectInstance) {
        if (statusEffectInstance.getEffectType() instanceof SpellStatusEffect) {
            return HudManager.SPELL_OUTLINE;
        }
        return original;
    }
}
