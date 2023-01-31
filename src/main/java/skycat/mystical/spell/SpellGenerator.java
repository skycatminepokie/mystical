package skycat.mystical.spell;

import net.minecraft.block.Blocks;
import net.minecraft.stat.Stats;
import skycat.mystical.spell.consequence.KillOnSleepConsequence;
import skycat.mystical.spell.cure.StatBackedSpellCure;
import skycat.mystical.util.EventCallbackEnum;

public class SpellGenerator {

    public static Spell get() {
        return new Spell(
                new KillOnSleepConsequence(),
                EventCallbackEnum.START_SLEEPING,
                new StatBackedSpellCure(100.0, Stats.MINED.getOrCreateStat(Blocks.CACTUS))
        );
    }
}
