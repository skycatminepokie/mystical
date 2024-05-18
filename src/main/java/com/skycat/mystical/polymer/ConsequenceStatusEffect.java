package com.skycat.mystical.polymer;

import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import eu.pb4.polymer.core.api.other.PolymerStatusEffect;
import eu.pb4.polymer.networking.api.server.PolymerServerNetworking;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public class ConsequenceStatusEffect extends StatusEffect implements PolymerStatusEffect {
    protected ConsequenceFactory<?> consequenceType;

    protected ConsequenceStatusEffect(ConsequenceFactory<?> consequenceType, int color) {
        super(consequenceType.getStatusEffectCategory(), color);
        this.consequenceType = consequenceType;
    }

    @Override
    public @Nullable StatusEffect getPolymerReplacement(ServerPlayerEntity player) {
        if (PolymerServerNetworking.getSupportedVersion(player.networkHandler, HelloPacket.ID) != -1) {
            return this;
        }
        return PolymerStatusEffect.super.getPolymerReplacement(player);
    }
}
