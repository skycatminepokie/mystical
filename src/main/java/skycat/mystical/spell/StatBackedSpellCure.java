package skycat.mystical.spell;

import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;

public class StatBackedSpellCure<T> extends SpellCure {
    private Stat<T> stat; // Ex Stats.MINED.getOrCreateStat(Blocks.Cactus);

    public StatType<T> getStatType() {
        return stat.getType();
    }
}
