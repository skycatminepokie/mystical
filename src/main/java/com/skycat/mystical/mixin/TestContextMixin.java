package com.skycat.mystical.mixin;

import net.minecraft.test.TestContext;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TestContext.class)
public interface TestContextMixin {
    @Invoker("getTestBox")
    Box mystical_getTestBox();
}
