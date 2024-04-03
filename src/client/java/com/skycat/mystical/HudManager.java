package com.skycat.mystical;

import com.skycat.mystical.spell.Spell;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class HudManager {

    public void updateHud(List<Spell> spells) {
        // TODO
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("We got some spells!"));
    }
}
