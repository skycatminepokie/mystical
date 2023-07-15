package com.skycat.mystical.common.spell.cure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.common.util.StatCodec;
import com.skycat.mystical.common.util.Utils;
import lombok.NonNull;

import java.util.HashMap;

public class StatBackedCureType implements CureType<StatBackedSpellCure> {
    @Override
    public @NonNull Codec<StatBackedSpellCure> getCodec() {
        return RecordCodecBuilder.create(spellCureInstance -> spellCureInstance.group(
                Codec.INT.fieldOf("contributionGoal").forGetter(StatBackedSpellCure::getContributionGoal),
                StatCodec.INSTANCE.fieldOf("stat").forGetter(StatBackedSpellCure::getStat),
                Codec.unboundedMap(Utils.UUID_CODEC, Codec.INT).fieldOf("contributions").forGetter(StatBackedSpellCure::getContributionCopy)
        ).apply(spellCureInstance, (goal, stat, contributions) -> new StatBackedSpellCure(goal, stat, new HashMap<>(contributions)))); // Done to make the map into a HashMap.
    }
}
