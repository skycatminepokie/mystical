package skycat.mystical.spell.cure;

import lombok.Getter;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;

@Getter
public class StatBackedSpellCure extends SpellCure {
    private final Stat stat;

    public StatBackedSpellCure(double contributionGoal, Stat stat) {
        super(contributionGoal, SpellCureType.STAT_BACKED);
        this.stat = stat;
    }

    public StatType getStatType() {
        return stat.getType();
    }
}
