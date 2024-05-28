package com.skycat.mystical.datafixer;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import net.minecraft.datafixer.TypeReferences;

public class SpellCodecFix extends DataFix {
    public SpellCodecFix(Schema outputSchema, boolean changesType) {
        super(outputSchema, changesType);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        Type<?> levelType = getOutputSchema().getType(TypeReferences.LEVEL);

        return null;
    }
}
