package com.skycat.mystical.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    // Using tags like this will make startup time a bit longer, but will allow for compatibility
    public static final TagKey<EntityType<?>> BOSSES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:bosses"));
    public static final TagKey<EntityType<?>> ZOMBIE_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:zombie_variants"));
    public static final TagKey<EntityType<?>> SKELETON_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:skeleton_variants"));
    public static final TagKey<EntityType<?>> ENDERMAN_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:enderman_variants"));

    public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BOSSES)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.WARDEN)
                .add(EntityType.ELDER_GUARDIAN)
                .addOptionalTag(new Identifier("c:bosses"));
        getOrCreateTagBuilder(ZOMBIE_VARIANTS)
                .add(EntityType.ZOMBIE)
                .add(EntityType.HUSK)
                .add(EntityType.ZOMBIE_VILLAGER)
                .add(EntityType.ZOMBIFIED_PIGLIN)
                .add(EntityType.DROWNED);
        getOrCreateTagBuilder(SKELETON_VARIANTS)
                .add(EntityType.SKELETON)
                .add(EntityType.WITHER_SKELETON)
                .add(EntityType.STRAY);
        getOrCreateTagBuilder(ENDERMAN_VARIANTS)
                .add(EntityType.ENDERMAN)
                .add(EntityType.ENDERMITE);
    }
}
