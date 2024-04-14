package com.skycat.mystical.datagen;

import com.skycat.mystical.MysticalTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {

    public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(MysticalTags.BOSSES)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.WARDEN)
                .add(EntityType.ELDER_GUARDIAN)
                .addOptionalTag(new Identifier("c:bosses"));
        getOrCreateTagBuilder(MysticalTags.ZOMBIE_VARIANTS)
                .add(EntityType.ZOMBIE)
                .add(EntityType.HUSK)
                .add(EntityType.ZOMBIE_VILLAGER)
                .add(EntityType.ZOMBIFIED_PIGLIN)
                .add(EntityType.DROWNED);
        getOrCreateTagBuilder(MysticalTags.SKELETON_VARIANTS)
                .add(EntityType.SKELETON)
                .add(EntityType.WITHER_SKELETON)
                .add(EntityType.STRAY);
        getOrCreateTagBuilder(MysticalTags.ENDERMAN_VARIANTS)
                .add(EntityType.ENDERMAN)
                .add(EntityType.ENDERMITE);
        getOrCreateTagBuilder(MysticalTags.EVOKER_SUMMONABLE)
                .add(EntityType.VEX)
                .add(EntityType.SILVERFISH)
                .add(EntityType.ENDERMITE)
                .add(EntityType.FOX)
                .add(EntityType.PARROT);
        getOrCreateTagBuilder(MysticalTags.RANDOM_EGG_SPAWNABLE)
                .add(EntityType.FOX)
                .add(EntityType.PARROT)
                .add(EntityType.FROG)
                .add(EntityType.BEE)
                .add(EntityType.COD)
                .add(EntityType.BAT)
                .add(EntityType.RABBIT)
                .add(EntityType.SILVERFISH)
                .add(EntityType.ENDERMITE);
    }
}
