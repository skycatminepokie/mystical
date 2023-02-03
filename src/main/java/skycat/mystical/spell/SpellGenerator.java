package skycat.mystical.spell;

import net.minecraft.block.Block;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.stat.Stats;
import net.minecraft.util.registry.Registry;
import skycat.mystical.Mystical;
import skycat.mystical.spell.consequence.KillOnSleepConsequence;
import skycat.mystical.spell.consequence.SpellConsequence;
import skycat.mystical.spell.cure.SpellCure;
import skycat.mystical.spell.cure.StatBackedSpellCure;
import skycat.mystical.util.Utils;

import java.util.ArrayList;
import skycat.mystical.util.Utils;

import java.util.ArrayList;
import java.util.function.Supplier;

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
    private static final ArrayList<Supplier<SpellConsequence>> consequenceSuppliers = new ArrayList<>();
    private static final ArrayList<SpellCure> cures = new ArrayList<>();
    private static final ArrayList<Supplier<SpellCure>> cureSuppliers = new ArrayList<>();

    static {
        consequenceSuppliers.add(()-> new KillOnSleepConsequence());

        cures.add(new StatBackedSpellCure(100.0, Stats.MINED.getOrCreateStat(Blocks.CACTUS)));
    }

    public static Spell get() {
        return new Spell(
                new KillOnSleepConsequence(),
               EntitySleepEvents.StartSleeping.class,
                new StatBackedSpellCure(100.0, Stats.MINED.getOrCreateStat(Blocks.CACTUS))
        );
    }

    // TODO: Weight things
    public static SpellConsequence getConsequence() {
        if (consequenceSuppliers.isEmpty()) {
            Utils.log("SpellGenerator found an empty consequence supplier list. Using default consequence."); // TODO note what the default is
            return new KillOnSleepConsequence(); // TODO change default
        }
        return Utils.chooseRandom(Mystical.getRANDOM(), consequenceSuppliers).get();
    }

    public static SpellCure getCure() {
        if (cures.isEmpty()) {
            Utils.log("SpellGenerator found an empty cure list. Using default cure (mine 10 cactus).");
            return new StatBackedSpellCure(10, Stats.MINED.getOrCreateStat(Blocks.CACTUS));
        }
        return Utils.chooseRandom(Mystical.getRANDOM(), cures);
    }

    private static Block getRandomBlock() {
        var randomBlock = Registry.BLOCK.getRandom(Mystical.getMC_RANDOM());
        if (randomBlock.isPresent()) {
            return randomBlock.get().value();
        }
        // TODO: Logging
        return Blocks.COMMAND_BLOCK;
    }
}
