package com.skycat.mystical.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skycat.mystical.SpellStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StatusEffect.class)
public abstract class StatusEffectMixin {
    @WrapOperation(method = "<init>", at = @At(target = "Lnet/minecraft/registry/Registry;createEntry(Ljava/lang/Object;)Lnet/minecraft/registry/entry/RegistryEntry$Reference;", value = "INVOKE"))
    public <T> RegistryEntry.Reference<T> mystical_noEntryForSpellStatusEffects(Registry<T> instance, T t, Operation<RegistryEntry.Reference<T>> original) {
        if (t instanceof SpellStatusEffect) {
            return null;
        }
        return original.call(instance, t);
    }
}
