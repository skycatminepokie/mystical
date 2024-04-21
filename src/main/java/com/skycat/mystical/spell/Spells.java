package com.skycat.mystical.spell;

import com.skycat.mystical.MysticalTags;
import com.skycat.mystical.spell.consequence.*;
import com.skycat.mystical.spell.cure.CureFactory;
import com.skycat.mystical.spell.cure.StatBackedSpellCure;
import com.skycat.mystical.util.Utils;
import lombok.Getter;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.stat.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Spells {
    @SuppressWarnings("rawtypes") @Getter
    private static final ArrayList<ConsequenceFactory> consequenceFactories = new ArrayList<>();
    @SuppressWarnings("rawtypes") @Getter private static final HashMap<String, ConsequenceFactory> shortNameToFactory = new HashMap<>();
    @SuppressWarnings("rawtypes") @Getter
    private static final ArrayList<CureFactory> cureFactories = new ArrayList<>();

    static {
        // Initialize all the consequence factories
        Collections.addAll(Spells.consequenceFactories,
                LevitateConsequence.FACTORY,
                RandomTreeTypeConsequence.FACTORY,
                BigCreeperExplosionConsequence.FACTORY,
                FishingRodLaunchConsequence.FACTORY,
                CatVariantChangeConsequence.FACTORY,
                SheepColorChangeConsequence.FACTORY,
                ZombieTypeChangeConsequence.FACTORY,
                SkeletonTypeChangeConsequence.FACTORY,
                EnderTypeChangeConsequence.FACTORY,
                DisableDaylightBurningConsequence.FACTORY,
                NoFuseConsequence.FACTORY,
                MobSpawnSwapConsequence.FACTORY,
                AggressiveGolemsConsequence.FACTORY,
                UnbreakableLocationConsequence.FACTORY,
                TurboChickensConsequence.FACTORY,
                OneStrikeWardensConsequence.FACTORY,
                RandomCreeperEffectCloudsConsequence.FACTORY,
                TurboMobsConsequence.FACTORY,
                RandomEvokerSummonsConsequence.FACTORY,
                IllusionersReplaceEvokersConsequence.FACTORY,
                ExplosionsInfestConsequence.FACTORY,
                BoldSlimesConsequence.FACTORY,
                ChangingArmorHurtsConsequence.FACTORY,
                SoundSwapConsequence.FACTORY,
                FishingRodSwapConsequence.FACTORY,
                MysteryEggsConsequence.FACTORY
        );

        // For some reason, using "? extends SpellConsequence" gives a warning.
        for (ConsequenceFactory<?> factory : Spells.consequenceFactories) {
            Spells.getShortNameToFactory().put(factory.getShortName(), factory);
        }

        // Initialize all the cure factories
        Collections.addAll(Spells.cureFactories,
                (random) -> (new StatBackedSpellCure(25, Stats.MINED.getOrCreateStat(Blocks.CACTUS))),
                (random) -> (new StatBackedSpellCure(3000, Stats.CUSTOM.getOrCreateStat(Stats.JUMP))),
                (random) -> (new StatBackedSpellCure(50, Stats.USED.getOrCreateStat(Items.SHEARS))),
                (random) -> (new StatBackedSpellCure(100000, Stats.CUSTOM.getOrCreateStat(Stats.SPRINT_ONE_CM))), // 1000 blocks
                (random) -> (new StatBackedSpellCure(10, Stats.CRAFTED.getOrCreateStat(Items.BARREL))),
                (random) -> (new StatBackedSpellCure(10, Stats.CUSTOM.getOrCreateStat(Stats.ANIMALS_BRED))),
                (random) -> (new StatBackedSpellCure(100, Stats.CRAFTED.getOrCreateStat(Items.BREAD))),
                (random) -> (new StatBackedSpellCure(35, Stats.CUSTOM.getOrCreateStat(Stats.EAT_CAKE_SLICE))), // 5 cakes
                (random) -> (new StatBackedSpellCure(20000, Stats.CUSTOM.getOrCreateStat(Stats.CROUCH_ONE_CM))), // 200 blocks
                (random) -> (new StatBackedSpellCure(5, Stats.KILLED.getOrCreateStat(EntityType.BAT))),
                (random) -> (new StatBackedSpellCure(25, Stats.KILLED.getOrCreateStat(EntityType.CREEPER))),
                (random) -> (new StatBackedSpellCure(25, Stats.KILLED.getOrCreateStat(EntityType.SKELETON))),
                (random) -> (new StatBackedSpellCure(25, Stats.KILLED.getOrCreateStat(EntityType.ZOMBIE))),
                (random) -> (new StatBackedSpellCure(256, Stats.MINED.getOrCreateStat(Blocks.STONE))), // 4 stacks
                (random) -> (new StatBackedSpellCure(10000, Stats.CUSTOM.getOrCreateStat(Stats.FALL_ONE_CM))), // 100 blocks
                (random) -> (new StatBackedSpellCure(50, Stats.USED.getOrCreateStat(Items.BREAD))),
                (random) -> (new StatBackedSpellCure(10, Stats.KILLED.getOrCreateStat(EntityType.DROWNED))),
                (random) -> (new StatBackedSpellCure(5, Stats.BROKEN.getOrCreateStat(Items.GOLDEN_SWORD))),
                (random) -> (new StatBackedSpellCure(100000, Stats.CUSTOM.getOrCreateStat(Stats.BOAT_ONE_CM))), // 1000 blocks
                (random) -> (new StatBackedSpellCure(100000, Stats.CUSTOM.getOrCreateStat(Stats.HORSE_ONE_CM))), // 1000 blocks
                (random) -> (new StatBackedSpellCure(10000, Stats.CUSTOM.getOrCreateStat(Stats.PIG_ONE_CM))), // 100 blocks
                (random) -> (new StatBackedSpellCure(50, Stats.USED.getOrCreateStat(Items.BREAD))),
                (random) -> (new StatBackedSpellCure(64, Stats.CRAFTED.getOrCreateStat(Items.GOLDEN_CARROT))),
                (random) -> (new StatBackedSpellCure(32, Stats.CRAFTED.getOrCreateStat(Utils.getRandomEntryFromTag(Registries.BLOCK, MysticalTags.GLAZED_TERRACOTTA).asItem()))) // Chooses a random item from the tag GLAZED_TERRACOTTA

        );
    }

    public static ConsequenceFactory<?> getFactory(String shortName) {
        return shortNameToFactory.get(shortName);
    }
}
