package skycat.mystical.spell.cure;

import lombok.Getter;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.text.MutableText;

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
    public MutableText getDescription() {
        return super.getDescription(); // TODO
    }
}
