package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.util.Utils;
import com.skycat.mystical.test.TestUtils;
import lombok.NonNull;
import net.minecraft.block.entity.SculkShriekerWarningManager;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class OneStrikeWardensConsequence extends SpellConsequence {
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

        private void setUpTest(TestContext context) {
            TestUtils.resetMystical(context);
            context.killAllEntities();
            ServerPlayerEntity player = Utils.createMockCreativeServerPlayerEntity(context);
            player.changeGameMode(GameMode.SURVIVAL);
            Optional<SculkShriekerWarningManager> optWarningManager = player.getSculkShriekerWarningManager();
            if (optWarningManager.isPresent()) {
                optWarningManager.get().setWarningLevel(0);
            }
            BlockPos teleportTo = context.getAbsolutePos(new BlockPos(6, 11, 6));
            System.out.println(teleportTo);
            player.updatePosition(teleportTo.getX(), teleportTo.getY(), teleportTo.getZ());
        }

        @GameTest(templateName = TestUtils.WARDEN_SUMMON_BOX, tickLimit = 275)
        public void testHaven(TestContext context) {
            setUpTest(context);
            TestUtils.havenAll(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(this);

            context.waitAndRun(250, () -> {
                context.dontExpectEntity(EntityType.WARDEN);
                context.complete();
            });
        }

        @GameTest(templateName = TestUtils.WARDEN_SUMMON_BOX, tickLimit = 275)
        public void testSpell(TestContext context) {
            setUpTest(context);
            Mystical.getSpellHandler().activateNewSpellWithConsequence(this);

            context.waitAndRun(250, () -> {
                context.expectEntity(EntityType.WARDEN);
                context.complete();
            });
        }

        @GameTest(templateName = TestUtils.WARDEN_SUMMON_BOX, tickLimit = 275)
        public void testSpellAndHaven(TestContext context) {
            setUpTest(context);
            TestUtils.havenAll(context);

            context.waitAndRun(250, () -> {
                context.dontExpectEntity(EntityType.WARDEN);
                context.complete();
            });
        }

        @GameTest(templateName = TestUtils.WARDEN_SUMMON_BOX, tickLimit = 275)
        public void testVanilla(TestContext context) {
            setUpTest(context);

            context.waitAndRun(250, () -> {
                context.dontExpectEntity(EntityType.WARDEN);
                context.complete();
            });
        }
    }

}
