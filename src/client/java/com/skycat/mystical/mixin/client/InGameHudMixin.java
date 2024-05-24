package com.skycat.mystical.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    /* For later, if we want to move to doing it manually instead of trying to hook in to the overlay
     @Inject(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V", shift = At.Shift.BEFORE))
     private void mystical_renderFakeStatusEffects(DrawContext context, CallbackInfo ci, @Local(ordinal = 0) int beneficialEffects, @Local(ordinal = 1) int harmfulEffects, @Local(ordinal = 0) List<Runnable> list) {
         for (Spell spell : getSpells()) {
             int x, y;
             list.add(() -> {
                 context.setShaderColor(1f, 1f, 1f, 1f);
                 context.drawSprite(x, y, 0, 18, 18, );
             });
         }
     }
    */
}
