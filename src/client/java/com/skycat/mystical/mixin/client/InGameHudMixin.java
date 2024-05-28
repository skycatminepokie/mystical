package com.skycat.mystical.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.skycat.mystical.HudManager;
import com.skycat.mystical.MysticalClient;
import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.spell.consequence.SpellConsequence;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

     @Inject(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V", shift = At.Shift.BEFORE))
     private void mystical_renderFakeStatusEffects(DrawContext context, float tickDelta, CallbackInfo ci, @Local(ordinal = 0) int beneficialEffects, @Local(ordinal = 1) int harmfulEffects, @Local(ordinal = 0) List<Runnable> list) {
         // Adapted from original method
         StatusEffectSpriteManager spriteManager = MinecraftClient.getInstance().getStatusEffectSpriteManager();
         for (Spell spell : MysticalClient.HUD_MANAGER.getCachedSpells()) {
             SpellConsequence consequence = spell.getConsequence();
             int x = context.getScaledWindowWidth();
             int y = 1;
             if (consequence.getDifficulty() > 0) {
                 x -= 25 * ++beneficialEffects;
             } else {
                 x -= 25 * ++harmfulEffects;
                 y = 27;
             }
             context.drawGuiTexture(HudManager.SPELL_OUTLINE, x, y, 24, 24);
             Sprite sprite = ((SpriteAtlasHolderAccessor) spriteManager).mystical_invokeGetSprite(HudManager.iconForSpell(consequence.getFactory()));
             int finalX = x;
             int finalY = y;
             list.add(() -> {
                 context.setShaderColor(1.0f, 1.0f, 1.0f, 1f);
                 context.drawSprite(finalX + 3, finalY + 3, 0, 18, 18, sprite);
                 context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
             });
         }
     }

    @ModifyExpressionValue(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Ljava/util/Collection;isEmpty()Z"))
    private boolean mystical_alsoRenderIfSpells(boolean original) {
         return original && MysticalClient.HUD_MANAGER.getCachedSpells().isEmpty();
    }
}
