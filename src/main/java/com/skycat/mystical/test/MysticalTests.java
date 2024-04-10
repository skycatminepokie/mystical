package com.skycat.mystical.test;

import com.skycat.mystical.HavenManager;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.MysticalTags;
import com.skycat.mystical.SaveState;
import com.skycat.mystical.spell.Spells;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import com.skycat.mystical.util.LogLevel;
import com.skycat.mystical.util.Utils;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.test.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class MysticalTests implements FabricGameTest {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "mystical.playerCanHaven")
    public void checkHavenWorks(TestContext context) {
        TestUtils.resetMystical(context);
        HavenManager havenManager = Mystical.getHavenManager();
        ServerPlayerEntity player = Utils.createMockCreativeServerPlayerEntity(context);
        havenManager.setPower(player, Integer.MAX_VALUE);
        context.assertTrue(havenManager.hasPower(player, havenManager.getHavenCost(player.getChunkPos())), "We couldn't haven with Integer.MAX_VALUE power.");
        context.assertTrue(havenManager.tryHaven(player.getChunkPos(), player), "Player couldn't haven, despite having enough power.");
        context.assertTrue(havenManager.isInHaven(player.getChunkPos()), "Havening worked, but we're not in a haven.");
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "mystical.playerCantHaven")
    public void checkHavenFails(TestContext context) {
        TestUtils.resetMystical(context);
        HavenManager havenManager = Mystical.getHavenManager();
        ServerPlayerEntity player = Utils.createMockCreativeServerPlayerEntity(context);

        havenManager.setPower(player, -1);
        context.assertFalse(havenManager.tryHaven(player.getChunkPos(), player), "We were able to haven with negative power???");

        havenManager.resetHavens();
        havenManager.setPower(player, 0);
        if (havenManager.tryHaven(player.getChunkPos(), player)) {
            Utils.log("We were able to haven with 0 power.", LogLevel.WARN);
        }
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE, batchId = "mystical.checkHavenSerialization")
    public void checkHavenSerialization(TestContext context) {
        HavenManager havenManager = Mystical.getHavenManager();
        havenManager.addHaven(0, 0);
        context.getWorld().save(null, true, false);
        HavenManager newHavenManager = SaveState.loadSave(context.getWorld().getServer()).getHavenManager();
        context.assertTrue(newHavenManager.equals(havenManager), "Serialization comparison failed.");
        context.complete();
    }

    @Override
    public void invokeTestMethod(TestContext context, Method method) {
        HavenManager havenManager = Mystical.getHavenManager();
        havenManager.resetHavens();
        havenManager.resetPower();
        Mystical.getSpellHandler().removeAllSpells();
        FabricGameTest.super.invokeTestMethod(context, method);
    }

    private static void addSpellTests(ArrayList<TestFunction> testFunctions) {
        for (ConsequenceFactory<?> factory : Spells.getConsequenceFactories()) {
            for (Method method : factory.getClass().getMethods()) {
                GameTest testInfo = method.getAnnotation(GameTest.class);
                if (testInfo != null) {
                    testFunctions.add(
                            new TestFunctionBuilder(factory.getShortName() + "." + method.getName(), methodToConsumer(method, factory))
                                    .tickLimit(testInfo.tickLimit())
                                    .batchId("mysticaltests.spell." + factory.getShortName() + "." + method.getName())
                                    .rotation(StructureTestUtil.getRotation(testInfo.rotation()))
                                    .required(testInfo.required())
                                    .templateName(testInfo.templateName())
                                    .duration(testInfo.duration())
                                    .maxAttempts(testInfo.maxAttempts())
                                    .requiredSuccesses(testInfo.requiredSuccesses())
                                    .build()
                    );
                }
            }
        }
    }

    public static Consumer<TestContext> methodToConsumer(Method method, Object object) {
        return (context) -> {
            try {
                method.invoke(object, context);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @CustomTestProvider
    public Collection<TestFunction> getTestFunctions() {
        ArrayList<TestFunction> testFunctions = new ArrayList<>();
        addSpellTests(testFunctions);
        testFunctions.sort(Comparator.comparing(TestFunction::getTemplateName));
        return testFunctions;
    }

    /**
     * Verify that {@link TestUtils#havenAll(TestContext)} works as expected.
     */
    @GameTest(templateName = TestUtils.EMPTY, batchId = "mystical.havenAll")
    public void testHavenAll(TestContext context) {
        TestUtils.resetMystical(context);
        TestUtils.havenAll(context);
        HavenManager havenManager = Mystical.getHavenManager();
        context.forEachRelativePos((blockPos) -> {
            blockPos = context.getAbsolutePos(blockPos);
            if (!havenManager.isInHaven(blockPos)) {
                throw new GameTestException("Block pos " + blockPos + " was expected to be havened.");
            }
        });
        context.complete();
    }

    /**
     * Verify that a few tags are not empty in order to prevent <br>
     * massive failure by publishing jars without generated data...<br>
     * <br>
     * ...again.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent") // Don't care, just fail please ty
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public static void testTags(TestContext context) { // TODO: Move this to MysticalTags if a good way can be found
        var bosses = Registries.ENTITY_TYPE.getEntryList(MysticalTags.BOSSES).get();
        bosses.get(0); // Throws IndexOutOfBoundsException if empty.

        var terracotta = Registries.BLOCK.getEntryList(MysticalTags.GLAZED_TERRACOTTA).get();
        terracotta.get(0); // Throws IndexOutOfBoundsException if empty.
        context.complete();
    }
}
