package com.skycat.mystical.common.spell.cure;

import com.skycat.mystical.common.util.Utils;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.text.MutableText;

@Getter
public class StatBackedSpellCure extends SpellCure {
    private final Stat stat;

    @Deprecated
    public StatBackedSpellCure(int contributionGoal, Stat stat, String translationKey) {
        super(contributionGoal, StatBackedSpellCure.class, translationKey);
        this.stat = stat;
    }

    public StatBackedSpellCure(int contributionGoal, Stat stat) {
        super(contributionGoal, StatBackedSpellCure.class, null);
        this.stat = stat;
    }

    public StatType getStatType() {
        return stat.getType();
    }

    @Override
    public MutableText getDescription() {
        MutableText text = Utils.translateStat(stat);
        var statValue = stat.getValue();
        if (statValue instanceof Block) {
            text.append(" (");
            text.append(((Block) statValue).getName());
            text.append(")");
        } else if (statValue instanceof Item) {
            text.append(" (");
            text.append(((Item) statValue).getName());
            text.append(")");
        } else if (statValue instanceof EntityType) {
            text.append(" (");
            text.append(((EntityType<?>) statValue).getName());
            text.append(")");
        }
        text.append(" (" + stat.format(getContributionTotal()) + "/" + stat.format(contributionGoal) + ")");
        return text;
    }
}
