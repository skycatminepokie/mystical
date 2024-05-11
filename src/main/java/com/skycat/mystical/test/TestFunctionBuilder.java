package com.skycat.mystical.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.test.TestContext;
import net.minecraft.test.TestFunction;
import net.minecraft.util.BlockRotation;

import java.util.function.Consumer;

public class TestFunctionBuilder {
    private String batchId = "defaultBatch";
    /**
     * Vanilla uses the format "class.method".
     */
    private String templatePath;
    private String templateName = FabricGameTest.EMPTY_STRUCTURE;
    private BlockRotation rotation = BlockRotation.NONE;
    private int tickLimit = 100;
    private long duration = 0L;
    private boolean required = true;
    private int requiredSuccesses = 1;
    private int maxAttempts = 1;
    private Consumer<TestContext> starter;
    private boolean manualOnly = false;
    private boolean skyAccess = false;

    /**
     * @param templatePath The name of the test.
     * @param starter      The method to run. {@link net.minecraft.test.GameTest} annotations are ignored.
     */
    public TestFunctionBuilder(String templatePath, Consumer<TestContext> starter) {
        this.templatePath = "mysticaltests." + templatePath;
        this.starter = starter;
    }

    public TestFunctionBuilder batchId(String batchId) {
        this.batchId = batchId;
        return this;
    }

    public TestFunctionBuilder templatePath(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    public TestFunction build() {
        return new TestFunction(batchId, templatePath, templateName, rotation, tickLimit, duration, required, manualOnly, maxAttempts, requiredSuccesses, skyAccess, starter);
    }

    public TestFunctionBuilder rotation(BlockRotation rotation) {
        this.rotation = rotation;
        return this;
    }

    public TestFunctionBuilder manualOnly(boolean manualOnly) {
        this.manualOnly = manualOnly;
        return this;
    }

    public TestFunctionBuilder skyAccess(boolean skyAccess) {
        this.skyAccess = skyAccess;
        return this;
    }

    public TestFunctionBuilder tickLimit(int tickLimit) {
        this.tickLimit = tickLimit;
        return this;
    }

    public TestFunctionBuilder duration(long duration) {
        this.duration = duration;
        return this;
    }

    /**
     * Equivalent to {@link TestFunctionBuilder#duration}.
     */
    public TestFunctionBuilder setupTicks(long setupTicks) {
        return duration(setupTicks);
    }

    public TestFunctionBuilder required(boolean required) {
        this.required = required;
        return this;
    }

    public TestFunctionBuilder requiredSuccesses(int requiredSuccesses) {
        this.requiredSuccesses = requiredSuccesses;
        return this;
    }

    public TestFunctionBuilder maxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    public TestFunctionBuilder starter(Consumer<TestContext> starter) {
        this.starter = starter;
        return this;
    }

    /**
     * Equivalent to {@link TestFunctionBuilder#templateName(String)}
     */
    public TestFunctionBuilder structure(String structure) {
        return templateName(structure);
    }

    public TestFunctionBuilder templateName(String templateName) {
        this.templateName = templateName;
        return this;
    }
}
