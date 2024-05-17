package com.skycat.mystical;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.Identifier;

public class MysticalClient implements ClientModInitializer {
    public static final Identifier SPELL_OUTLINE = Identifier.of(Mystical.MOD_ID, "hud/spell_background");

    @Override
    public void onInitializeClient() {
    }
}