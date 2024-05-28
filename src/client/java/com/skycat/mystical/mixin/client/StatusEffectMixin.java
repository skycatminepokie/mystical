package com.skycat.mystical.mixin.client;

import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StatusEffect.class)
public abstract class StatusEffectMixin {
    //@WrapOperation(method = "<init>", at = @At(target = "Lnet/minecraft/registry/Registry;createEntry(Ljava/lang/Object;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;", value = "INVOKE"))
    //public <T> RegistryEntry.Reference<T> mystical_noEntryForSpellStatusEffects(Registry<T> instance, T t, Operation<RegistryEntry.Reference<T>> original) {
    //    if (t instanceof SpellStatusEffect) {
    //        return null;
    //    }
    //    return original.call(instance, t);
    //}
}
