package com.skycat.mystical.spell;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.*;
import com.skycat.mystical.spell.cure.CureFactory;
import com.skycat.mystical.spell.cure.SpellCure;
import com.skycat.mystical.spell.cure.StatBackedSpellCure;
import com.skycat.mystical.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
public class SpellGenerator { // TODO: For now, a lot of things that could be randomized are just hard-coded
    @SuppressWarnings("rawtypes") private static final ArrayList<ConsequenceFactory> consequenceFactories = new ArrayList<>();
    @SuppressWarnings("rawtypes") private static final HashMap<String, ConsequenceFactory> shortNameToFactory = new HashMap<>();
    @SuppressWarnings("rawtypes") private static final ArrayList<CureFactory> cureFactories = new ArrayList<>();

    static {
        Collections.addAll(consequenceFactories,
                // KillOnSleepConsequence.FACTORY,
                LevitateConsequence.FACTORY,
                RandomTreeTypeConsequence.FACTORY,
                BigCreeperExplosionConsequence.FACTORY,
                FishingRodLaunchConsequence.FACTORY,
                CatVariantChangeConsequence.FACTORY,
                SheepColorChangeConsequence.FACTORY,
                ZombieTypeChangeConsequence.FACTORY,
                SkeletonTypeChangeConsequence.FACTORY,
                EnderTypeChangeConsequence.FACTORY
        );

        // For some reason, using "? extends SpellConsequence" gives a warning.
        for (ConsequenceFactory<?> factory : consequenceFactories) {
            getShortNameToFactory().put(factory.getShortName(), factory);
        }

        Collections.addAll(cureFactories,
                (random) -> (new StatBackedSpellCure(100.0, Stats.MINED.getOrCreateStat(Blocks.CACTUS), "text.mystical.spellCure.default")), // TODO: Translate
                (random) -> (new StatBackedSpellCure(1000, Stats.CUSTOM.getOrCreateStat(Stats.JUMP), "text.mystical.spellCure.default")), // TODO: Translate
                (random) -> (new StatBackedSpellCure(50, Stats.USED.getOrCreateStat(Items.SHEARS), "text.mystical.spellCure.default")), // TODO: Translate
                (random) -> (new StatBackedSpellCure(50000, Stats.CUSTOM.getOrCreateStat(Stats.SPRINT_ONE_CM), "text.mystical.spellCure.default")), // TODO: Translate
                (random) -> (new StatBackedSpellCure(640, Stats.CRAFTED.getOrCreateStat(Items.BARREL), "text.mystical.spellCure.default")), // TODO: Translate
                (random) -> (new StatBackedSpellCure(10, Stats.CUSTOM.getOrCreateStat(Stats.ANIMALS_BRED), "text.mystical.spellCure.default")) // TODO: Translate
        );
    }

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
    public static SpellConsequence getConsequence() { // I think we're getting rid of the notion of points for now. WARN: This may be slow with many consequences
        if (consequenceFactories.isEmpty()) { // Should not happen
            Utils.log(Utils.translateString("text.mystical.spellGenerator.emptyConsequenceList")); // TODO: Config
            return LevitateConsequence.FACTORY.make(Mystical.getRANDOM(), 0);
        }

        // Think of the chances as being on a number line.
        double sum = 0;
        for (ConsequenceFactory<?> factory : consequenceFactories) {
            sum += factory.getWeight(); // The line is as big as all the weights set end to end.
        }
        double rand = Mystical.RANDOM.nextDouble(sum); // rand is a place on the number line.
        for (ConsequenceFactory<?> consequenceFactory : consequenceFactories) {
            double chance = consequenceFactory.getWeight();
            if (rand < chance) { // If that place is in the "territory" of this consequence
                return consequenceFactory.make(Mystical.RANDOM, 0); // then that one is chosen.
            } else { // Otherwise, chop off the first part of the number line
                // by forgetting about this consequence, essentially shifting all the territories left to fill in the gap.
                rand -= chance; // All we have to do is make sure rand moves along with everything else
                assert rand >= 0; // WARN debug
            }
        }

        return null; // This should only happen if all consequences are disabled TODO: Logging
    }

    public static SpellCure getCure() {
        if (cureFactories.isEmpty()) {
            Utils.log(Utils.translateString("text.mystical.spellGenerator.emptyCureList")); // TODO: Config
            return new StatBackedSpellCure(10, Stats.MINED.getOrCreateStat(Blocks.CACTUS), "text.mystical.spellCure.default");
        }
        return Utils.chooseRandom(Mystical.getRANDOM(), cureFactories).make(Mystical.getRANDOM());
    }

    private static Block getRandomBlock() { // TODO: Make checked (no unbreakables, configurable rarities, etc)
        Optional<RegistryEntry<Block>> blockEntry = Registry.BLOCK.getRandom(Mystical.getMC_RANDOM());
        Block block;
        if (blockEntry.isPresent()) {
            block = blockEntry.get().value();
            if (block.getHardness() == -1) { // Unbreakable block
                return getRandomBlock(); // Try again
            }
            return block;
        }
        // Labeled in translation as critical error. Ideally, this should not happen.
        Utils.log(Utils.translateString("text.mystical.spellGenerator.failedRandomBlock"), Mystical.CONFIG.failedToGetRandomBlockLogLevel());
        return Blocks.COMMAND_BLOCK;
    }

    @SuppressWarnings("rawtypes")
    public static HashMap<String, ConsequenceFactory> getShortNameToFactory() {
        return shortNameToFactory;
    }
}
