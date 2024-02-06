package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.util.Utils;
import com.skycat.mystical.event.CatEntityEvents;
import lombok.NonNull;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class CatVariantChangeConsequence extends SpellConsequence implements CatEntityEvents.Eat {
    public static final Factory FACTORY = new Factory();
    private static final ArrayList<CatVariant> VARIANTS = new ArrayList<>();


    @Override
    public @NotNull ConsequenceFactory<CatVariantChangeConsequence> getFactory() {
        return FACTORY;
    }

    public CatVariantChangeConsequence() { // TODO: Config on variants
        super(CatVariantChangeConsequence.class, CatEntityEvents.Eat.class, 1d);  // TODO: Scaling
    }

    @Override
    public void onEat(CatEntity cat, PlayerEntity player, Hand hand, ItemStack stack) {
        if (VARIANTS.isEmpty()) { // Should this be in static? Seems like delaying initialization might be good though.
            for (CatVariant catVariant : Registries.CAT_VARIANT) {
                VARIANTS.add(catVariant);
            }
        }
        if (Mystical.getSpellHandler().isConsequenceActive(CatVariantChangeConsequence.class) &&
                !Mystical.getHavenManager().isInHaven(player) &&
                Utils.percentChance(Mystical.CONFIG.catVariantChange.chance())) {
            cat.setVariant(Util.getRandom(VARIANTS, Mystical.MC_RANDOM));
            Utils.log(Utils.translateString("text.mystical.consequence.catVariantChange.fired"), Mystical.CONFIG.catVariantChange.logLevel());
        }
    }

    public static class Factory extends ConsequenceFactory<CatVariantChangeConsequence> {
        public Factory() {
            super("catVariantChange",
                    "Cat Variant Change",
                    "We change coats, so why can't cats do the same?",
                    "Changed cat variant",
                    CatVariantChangeConsequence.class,
                    Codec.unit(CatVariantChangeConsequence::new));
        }

        @Override
        public @NotNull CatVariantChangeConsequence make(@NonNull Random random, double points) {
            return new CatVariantChangeConsequence();
        }

        @Override
        public void test(TestContext context) { // TODO
            com.skycat.mystical.common.util.Utils.log("Test not implemented for " + shortName, com.skycat.mystical.common.LogLevel.WARN);
            context.complete();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.catVariantChange.enabled() ? Mystical.CONFIG.catVariantChange.weight() : 0);
        }
    }
}
