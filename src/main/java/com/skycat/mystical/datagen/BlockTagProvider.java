package com.skycat.mystical.datagen;

import com.skycat.mystical.MysticalTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.GlazedTerracottaBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        Iterator<Block> it = Registries.BLOCK.iterator();
        FabricTagProvider<Block>.FabricTagBuilder tagBuilder = getOrCreateTagBuilder(MysticalTags.GLAZED_TERRACOTTA);
        while (it.hasNext()) { // For all the blocks that exist
            Block block = it.next();
            if (block instanceof GlazedTerracottaBlock) { // If it's glazed terracotta
                tagBuilder.add(block); // Add it
            }
        }
    }
}
