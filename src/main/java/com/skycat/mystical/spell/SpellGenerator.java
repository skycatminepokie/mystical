package com.skycat.mystical.spell;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import com.skycat.mystical.spell.consequence.LevitateConsequence;
import com.skycat.mystical.spell.consequence.SpellConsequence;
import com.skycat.mystical.spell.cure.SpellCure;
import com.skycat.mystical.spell.cure.StatBackedSpellCure;
import com.skycat.mystical.util.LogLevel;
import com.skycat.mystical.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.Stats;

import java.util.Optional;

/*
Notes on randomization scheme:
Difficulty: A double rating how much trouble a spell will cause, taking into account both the cure and consequence.
    A negative rating indicates that the spell may be useful in a way
Split: A target value for |consequence difficulty - cure difficulty|.
    Lower values should result in a more balanced spell, while higher values return harder consequences paired with easier cures, or harder cures with easier consequences.
Wildness: A measure indicating how different the gameplay is due to the spell - more unique spells have higher wildness.
    This is separate from difficulty, though difficulty will likely be correlated.
 */
public class SpellGenerator {

    /**
     * Return a new random spell.
     *
     * @return A new random spell.
     */
    public static Spell get() {
        return new Spell(getConsequence(), getCure());
    }

    public static Spell getWithConsequence(ConsequenceFactory<?> consequenceFactory) {
        return new Spell(consequenceFactory.make(Mystical.RANDOM, 0), getCure());
    }

    /**
     * Get a random consequence, weighted by config settings
     *
     * @return A new consequence, or null if all spells are disabled
     */
    public static SpellConsequence getConsequence() { // I think we're getting rid of the notion of points for now. This may be slow with many consequences
        if (Spells.getConsequenceFactories().isEmpty()) { // Should not happen
            Utils.log(Utils.translateString("text.mystical.spellGenerator.emptyConsequenceList"));
            return LevitateConsequence.FACTORY.make(Mystical.RANDOM, 0);
        }

        // Think of the chances as being on a number line.
        double sum = 0;
        for (ConsequenceFactory<?> factory : Spells.getConsequenceFactories()) {
            sum += factory.getWeight(); // The line is as big as all the weights set end to end.
        }
        double rand = Mystical.RANDOM.nextDouble(sum); // rand is a place on the number line.
        for (ConsequenceFactory<?> consequenceFactory : Spells.getConsequenceFactories()) {
            double chance = consequenceFactory.getWeight();
            if (rand < chance) { // If that place is in the "territory" of this consequence
                return consequenceFactory.make(Mystical.RANDOM, 0); // then that one is chosen.
            } else { // Otherwise, chop off the first part of the number line
                // by forgetting about this consequence, essentially shifting all the territories left to fill in the gap.
                rand -= chance; // All we have to do is make sure rand moves along with everything else
                // assert rand >= 0;
            }
        }
        Utils.log(Utils.translateString("text.mystical.spellGenerator.allConsequencesDisabled"), LogLevel.WARN);
        return null; // This should only happen if all consequences are disabled
    }

    public static SpellCure getCure() {
        if (Spells.getCureFactories().isEmpty()) {
            Utils.log(Utils.translateString("text.mystical.spellGenerator.emptyCureList"));
            return new StatBackedSpellCure(10, Stats.MINED.getOrCreateStat(Blocks.CACTUS));
        }
        return Utils.chooseRandom(Mystical.RANDOM, Spells.getCureFactories()).make(Mystical.RANDOM);
    }

    private static Block getRandomBlock() { // TODO: Make checked (no unbreakables, configurable rarities, etc)
        Optional<RegistryEntry.Reference<Block>> blockEntry = Registries.BLOCK.getRandom(Mystical.MC_RANDOM);
        Block block;
        if (blockEntry.isPresent()) {
            block = blockEntry.get().value();
            if (block.getHardness() == -1) { // Unbreakable block
                return getRandomBlock(); // Try again
            }
            return block;
        }
        // Labeled in translation as critical error. Ideally, this should not happen.
        Utils.log(Utils.translateString("text.mystical.logging.failedToGetRandomBlock"), Mystical.CONFIG.failedToGetRandomBlockLogLevel());
        return Blocks.STONE;
    }

}
