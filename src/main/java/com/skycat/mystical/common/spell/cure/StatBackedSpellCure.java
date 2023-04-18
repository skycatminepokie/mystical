package com.skycat.mystical.common.spell.cure;

import com.skycat.mystical.common.util.Utils;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

@Getter
public class StatBackedSpellCure extends SpellCure {
    private final Stat stat;

    public StatBackedSpellCure(double contributionGoal, Stat stat, String translationKey) {
        super(contributionGoal, StatBackedSpellCure.class, translationKey);
        this.stat = stat;
    }

    public StatType getStatType() {
        return stat.getType();
    }

    @Override
    public Text getTranslation() {
        MutableText text = Utils.translateStat(stat);
        var statValue = stat.getValue();
        if (statValue instanceof Block) {
            text.append(((Block) statValue).getName());
        } else if (statValue instanceof Item) {
            text.append(((Item) statValue).getName());
        } else if (statValue instanceof EntityType) {
            text.append(((EntityType<?>) statValue).getName());
        }
        return text;
    }

    @Override
    public MutableText getDescription() {
        return super.getDescription(); // TODO
    }
}
