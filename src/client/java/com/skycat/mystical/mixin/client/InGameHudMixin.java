package com.skycat.mystical.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    //@ModifyVariable(method = "renderStatusEffectOverlay", at = @At(value = "STORE", ordinal = 0))
    //private Collection<StatusEffectInstance> mystical_addFakeStatusEffect(Collection<StatusEffectInstance> original) {
    //    ArrayList<StatusEffectInstance> list = new ArrayList<>(List.of(original.toArray(new StatusEffectInstance[0])));
    //    list.addAll(MysticalClient.HUD_MANAGER.getFakeStatusEffects());
    //    return list;
    //}
//
    //@ModifyArg(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
    //private Identifier mystical_useSpellOutline(Identifier original, @Local StatusEffectInstance statusEffectInstance) {
    //    if (statusEffectInstance.getEffectType() instanceof SpellStatusEffect) {
    //        return HudManager.SPELL_OUTLINE;
    //    }
    //    return original;
    //}

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
