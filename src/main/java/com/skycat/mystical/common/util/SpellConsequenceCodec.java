package com.skycat.mystical.common.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.skycat.mystical.common.spell.consequence.SpellConsequence;

public class SpellConsequenceCodec implements Codec<SpellConsequence> {
    @Override
    public <T> DataResult<Pair<SpellConsequence, T>> decode(DynamicOps<T> ops, T input) {
        return null;
    }

    @Override
    public <T> DataResult<T> encode(SpellConsequence input, DynamicOps<T> ops, T prefix) {
        return null;
    }
}
