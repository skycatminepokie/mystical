package skycat.mystical.spell;

import lombok.Getter;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;

@Getter
public class StatBackedSpellCure<T> extends SpellCure {
    private final Stat<T> stat; // Ex Stats.MINED.getOrCreateStat(Blocks.Cactus); // TODO Serializer

    public StatBackedSpellCure(double contributionGoal, Stat<T> stat) {
        super(contributionGoal);
        this.stat = stat;
    }

    public StatType<T> getStatType() {
        return stat.getType();
    }
}
