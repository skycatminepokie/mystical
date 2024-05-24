package com.skycat.mystical.spell.cure;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

public interface CureType<T extends SpellCure> {

    @NotNull Codec<? extends SpellCure> getCodec();

}
