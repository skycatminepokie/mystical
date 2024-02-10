package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.test.TestUtils;
import lombok.NonNull;
import net.minecraft.block.entity.SculkShriekerWarningManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class OneStrikeWardensConsequence extends SpellConsequence { // TODO: Tests
    public static final Factory FACTORY = new Factory();

    public OneStrikeWardensConsequence() {
        super(OneStrikeWardensConsequence.class, null, 100d);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    public static class Factory extends ConsequenceFactory<OneStrikeWardensConsequence> {

        protected Factory() {
            super("oneStrikeWardens",
                    "One Strike Wardens",
                    "Mr. Clompy Shoes returns!",
                    "Hah. New warden, nerd.",
                    OneStrikeWardensConsequence.class,
                    Codec.unit(OneStrikeWardensConsequence::new));
        }

        @Override
        public @NotNull OneStrikeWardensConsequence make(@NonNull Random random, double points) {
            return new OneStrikeWardensConsequence();
        }


        @Override
        public double getWeight() {
            return (Mystical.CONFIG.oneStrikeWardens.enabled() ? Mystical.CONFIG.oneStrikeWardens.weight() : 0);
        }

        private static void testNotActive(TestContext context) {
            TestUtils.resetMystical(context);
            context.killAllEntities();
            TestUtils.havenAll(context);
            PlayerEntity player = context.createMockSurvivalPlayer();
            Optional<SculkShriekerWarningManager> optWarningManager = player.getSculkShriekerWarningManager();
            if (optWarningManager.isPresent()) {
                optWarningManager.get().setWarningLevel(0);
            }
            BlockPos teleportTo = context.getAbsolutePos(new BlockPos(6, 8, 6));
            player.teleport(teleportTo.getX(), teleportTo.getY(), teleportTo.getZ(), false);


            context.waitAndRun(75, () -> {
                context.dontExpectEntity(EntityType.WARDEN);
            });
            context.complete();
        }

        @GameTest(templateName = TestUtils.WARDEN_SUMMON_BOX)
        public void testHaven(TestContext context) { // TODO: Test
            testNotActive(context);
        }

        @GameTest(templateName = TestUtils.WARDEN_SUMMON_BOX)
        public void testSpell(TestContext context) { // TODO: Test
            TestUtils.resetMystical(context);
            context.killAllEntities();
            Mystical.getSpellHandler().activateNewSpellWithConsequence(this);
            PlayerEntity player = context.createMockSurvivalPlayer();
            Optional<SculkShriekerWarningManager> optWarningManager = player.getSculkShriekerWarningManager();
            if (optWarningManager.isPresent()) {
                optWarningManager.get().setWarningLevel(0);
            }
            BlockPos teleportTo = context.getAbsolutePos(new BlockPos(6, 8, 6));
            player.teleport(teleportTo.getX(), teleportTo.getY(), teleportTo.getZ(), false);


            context.waitAndRun(75, () -> {
                context.expectEntity(EntityType.WARDEN);
            });
            context.complete();
        }

        @GameTest(templateName = TestUtils.WARDEN_SUMMON_BOX)
        public void testVanilla(TestContext context) { // TODO: Test
            TestUtils.resetMystical(context);
            context.killAllEntities();
            PlayerEntity player = context.createMockSurvivalPlayer();
            Optional<SculkShriekerWarningManager> optWarningManager = player.getSculkShriekerWarningManager();
            if (optWarningManager.isPresent()) {
                optWarningManager.get().setWarningLevel(0);
            }
            BlockPos teleportTo = context.getAbsolutePos(new BlockPos(6, 8, 6));
            player.teleport(teleportTo.getX(), teleportTo.getY(), teleportTo.getZ(), false);


            context.waitAndRun(75, () -> {
                context.dontExpectEntity(EntityType.WARDEN);
            });
            context.complete();
        }


    }

}
