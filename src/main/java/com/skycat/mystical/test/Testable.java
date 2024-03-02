package com.skycat.mystical.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.TestContext;
import net.minecraft.test.TestFunction;
import net.minecraft.util.BlockRotation;

public interface Testable {
    /**
     * @return The batch to run this class' test in.
     */
    default String getBatchId() {
        return "defaultBatch";
    }

    /**
     * @return The maximum number of attempts to pass this test.
     */
    default int getMaxAttempts() {
        return 1;
    }

    /**
     * @return The number of successful trials for this test to pass.
     */
    default int getRequiredSuccesses() {
        return 1;
    }

    /**
     * @return The rotation of the structure to use.
     */
    default BlockRotation getRotation() {
        return BlockRotation.NONE;
    }

    /**
     * @return The number of ticks to set up the test.
     */
    default long getSetupTicks() {
        return 0L;
    }

    /**
     * @return The structure to use for this test.
     */
    default String getStructure() {
        return FabricGameTest.EMPTY_STRUCTURE;
    }

    /**
     * This is essentially the name of the test. <br>
     * Fabric API's GameTest module uses {@code entrypointName.methodName}.
     */
    String getTemplatePath();

    default TestFunction getTestFunction() {
        return new TestFunction(getBatchId(),
                getTemplatePath(),
                getStructure(),
                getRotation(),
                getTickLimit(),
                getSetupTicks(),
                isRequired(),
                getRequiredSuccesses(),
                getMaxAttempts(),
                this::test);
    }

    /**
     * @return The maximum number of ticks before the test fails.
     */
    default int getTickLimit() {
        return 100;
    }

    /**
     * @return If this test is required.
     */
    default boolean isRequired() {
        return true;
    }

    /**
     * The test to run for this class.
     *
     * @param context The context to use when running this test.
     */
    void test(TestContext context);
}
