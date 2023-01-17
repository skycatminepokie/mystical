package skycat.mystical.spell;

import lombok.Getter;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;

@Getter
public class StatBackedSpellCure<T> extends SpellCure {
    private Stat<T> stat; // Ex Stats.MINED.getOrCreateStat(Blocks.Cactus);

    public StatType<T> getStatType() {
        return stat.getType();
    }
}
