package com.skycat.mystical.spell.cure;

import com.mojang.serialization.Codec;
import lombok.NonNull;

public interface CureType<T extends SpellCure> {

    @NonNull Codec<? extends SpellCure> getCodec();

}
