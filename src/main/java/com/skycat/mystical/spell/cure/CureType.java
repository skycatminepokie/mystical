package com.skycat.mystical.spell.cure;

import com.mojang.serialization.MapCodec;
import lombok.NonNull;

public interface CureType<T extends SpellCure> {

    @NonNull MapCodec<? extends SpellCure> getCodec();

}
