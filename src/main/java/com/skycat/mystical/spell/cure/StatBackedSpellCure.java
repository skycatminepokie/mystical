package com.skycat.mystical.spell.cure;

import com.skycat.mystical.util.Utils;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class StatBackedSpellCure extends SpellCure {
    private final Stat stat;

    public StatBackedSpellCure(int contributionGoal, Stat stat) {
        this(contributionGoal, stat, new HashMap<>());
    }

    public StatBackedSpellCure(int contributionGoal, Stat stat, HashMap<UUID, Integer> contributions) {
        super(contributionGoal, StatBackedSpellCure.class, CureTypes.STAT_BACKED, contributions);
        this.stat = stat;
    }

    public StatType getStatType() {
        return stat.getType();
    }

    @Override
    public MutableText getDescription() {
        MutableText text;
        if (getStatType().equals(Stats.KILLED)) {
            text = Utils.translatable("text.mystical.cure.kill", ((EntityType<?>) stat.getValue()).getName());
        } else {
            text = Utils.translateStat(stat); // This is the default. It works best with CUSTOM type stats.
        }
        appendType(text);
        appendCompletion(text);
        return text;
    }

    protected void appendCompletion(MutableText text) {
        text.append(" (" + stat.format(getContributionTotal()) + "/" + stat.format(contributionGoal) + ")");
    }

    protected void appendType(MutableText text) {
        var statValue = stat.getValue();
        if (statValue instanceof Block) {
            text.append(" (");
            text.append(((Block) statValue).getName());
            text.append(")");
        } else if (statValue instanceof Item) {
            text.append(" (");
            text.append(((Item) statValue).getName());
            text.append(")");
        }
    }
}
