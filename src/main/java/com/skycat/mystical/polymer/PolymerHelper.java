package com.skycat.mystical.polymer;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.Spells;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class PolymerHelper {
    public static void init() {
        for (ConsequenceFactory<?> consequenceFactory : Spells.getConsequenceFactories()) {
            Registry.register(Registries.STATUS_EFFECT, Identifier.of(Mystical.MOD_ID, consequenceFactory.shortName), new ConsequenceStatusEffect(consequenceFactory, 0x000000));
        }
    }

}
