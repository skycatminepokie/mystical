package com.skycat.mystical.mixin;

import net.minecraft.block.SaplingGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SaplingGenerator.class)
public abstract class SaplingGeneratorMixin {
    @Accessor("GENERATORS")
    public static Map<String, SaplingGenerator> getGenerators() {
        throw new AssertionError();
    }
}
