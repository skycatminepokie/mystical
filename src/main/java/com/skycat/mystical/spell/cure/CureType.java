package com.skycat.mystical.spell.cure;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

public interface CureType<T extends SpellCure> {

    @NotNull
    MapCodec<? extends SpellCure> getCodec();

}
