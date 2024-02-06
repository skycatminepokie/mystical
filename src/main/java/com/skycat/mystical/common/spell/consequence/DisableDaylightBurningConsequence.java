package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import lombok.NonNull;
import net.minecraft.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class DisableDaylightBurningConsequence extends SpellConsequence { // TODO: Config
    public static Factory FACTORY = new Factory();

    protected DisableDaylightBurningConsequence() {
        super(DisableDaylightBurningConsequence.class, null, 15d); // TODO: Scaling
    }

    @Override
    public @NotNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<DisableDaylightBurningConsequence> {

        protected Factory() {
            super("disableDaylightBurning",
                    "Disable Daylight Burning",
                    "Mobs bought some sunscreen",
                    "Said no this mob doesn't burn",
                    DisableDaylightBurningConsequence.class,
                    Codec.unit(DisableDaylightBurningConsequence::new));
        }

        @Override
        public @NotNull DisableDaylightBurningConsequence make(@NonNull Random random, double points) {
            return new DisableDaylightBurningConsequence();
        }

        @Override
        public void test(TestContext context) { // TODO
            com.skycat.mystical.common.util.Utils.log("Test not implemented for " + shortName, com.skycat.mystical.common.LogLevel.WARN);
            context.complete();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.disableDaylightBurning.enabled() ? Mystical.CONFIG.disableDaylightBurning.weight() : 0);
        }
    }
}
