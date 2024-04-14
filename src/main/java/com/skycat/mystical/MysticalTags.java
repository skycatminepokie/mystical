package com.skycat.mystical;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MysticalTags {
    public static final TagKey<EntityType<?>> BOSSES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Mystical.MOD_ID, "bosses"));
    public static final TagKey<EntityType<?>> ZOMBIE_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Mystical.MOD_ID, "zombie_variants"));
    public static final TagKey<EntityType<?>> SKELETON_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Mystical.MOD_ID, "skeleton_variants"));
    public static final TagKey<EntityType<?>> ENDERMAN_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Mystical.MOD_ID, "enderman_variants"));
    public static final TagKey<EntityType<?>> EVOKER_SUMMONABLE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Mystical.MOD_ID, "evoker_summonable"));
    public static final TagKey<EntityType<?>> RANDOM_EGG_SPAWNABLE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Mystical.MOD_ID, "random_egg_spawnable"));
    public static final TagKey<Block> GLAZED_TERRACOTTA = TagKey.of(RegistryKeys.BLOCK, new Identifier(Mystical.MOD_ID, "glazed_terracotta"));
    public static void init() {}
}
