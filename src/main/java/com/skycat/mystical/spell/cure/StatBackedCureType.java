package com.skycat.mystical.spell.cure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.util.StatCodec;
import lombok.NonNull;
import net.minecraft.util.Uuids;

import java.util.HashMap;

public class StatBackedCureType implements CureType<StatBackedSpellCure> {
    @Override
    public @NonNull Codec<StatBackedSpellCure> getCodec() {
        return RecordCodecBuilder.create(spellCureInstance -> spellCureInstance.group(
                Codec.INT.fieldOf("contributionGoal").forGetter(StatBackedSpellCure::getContributionGoal),
                StatCodec.INSTANCE.fieldOf("stat").forGetter(StatBackedSpellCure::getStat),
                Codec.unboundedMap(Uuids.CODEC, Codec.INT).fieldOf("contributions").forGetter(StatBackedSpellCure::getContributionCopy)
        ).apply(spellCureInstance, (goal, stat, contributions) -> new StatBackedSpellCure(goal, stat, new HashMap<>(contributions)))); // Done to make the map into a HashMap.
    }
}
