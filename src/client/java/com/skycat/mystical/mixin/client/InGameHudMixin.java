package com.skycat.mystical.mixin.client;

import com.skycat.mystical.FakeStatusEffect;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @ModifyVariable(method = "renderStatusEffectOverlay", at = @At(value = "STORE", ordinal = 0))
    public Collection<StatusEffectInstance> addFakeStatusEffect(Collection<StatusEffectInstance> original) {
        ArrayList<StatusEffectInstance> list = new ArrayList<>(List.of(original.toArray(new StatusEffectInstance[0])));
        list.add(new StatusEffectInstance(FakeStatusEffect.getOrCreate(Identifier.of("mystical", "spell/icon"), StatusEffectCategory.BENEFICIAL), -1));
        return list;
    }
}
