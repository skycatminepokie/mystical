package com.skycat.mystical.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.MysticalCriteria;
import com.skycat.mystical.spell.consequence.SpellConsequence;
import com.skycat.mystical.spell.cure.SpellCure;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;

@Getter @Setter
public class Spell {
    // https://forge.gemwire.uk/wiki/Codecs
    public static final Codec<Spell> CODEC = RecordCodecBuilder.create(spellInstance -> spellInstance.group(
            SpellConsequence.CODEC.fieldOf("consequence").forGetter(Spell::getConsequence),
            SpellCure.CODEC.fieldOf("cure").forGetter(Spell::getCure)
    ).apply(spellInstance, Spell::new));
    private SpellConsequence consequence;
    private SpellCure cure;

    public Spell(SpellConsequence consequence, SpellCure cure) {
        this.consequence = consequence;
        this.cure = cure;
    }

    /**
     * Award power to players based on their contributions.
     * @param totalPower The total power to distribute among contributors.
     * @param max The maximum power to give to any one contributor.
     */
    public void onCured(int totalPower, int max, MinecraftServer server) {
        Map<UUID, Integer> contributions = cure.getContributionCopy();
        for (UUID uuid : contributions.keySet()) {
            if (contributions.get(uuid) <= 0) continue;
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player != null) {
                MysticalCriteria.SPELL_CURED_CRITERION.trigger(player, this);
            }
            // Formula: min(totalPower * percentContributed, max)
            Mystical.getHavenManager().addPower(uuid, (int) Math.min(totalPower * ((double) contributions.get(uuid) / cure.getContributionTotal()), max));
        }
    }
}
