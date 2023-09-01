package com.skycat.mystical.datagen;

import com.skycat.mystical.Mystical;
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
        getOrCreateTagBuilder(Mystical.BOSSES)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityType.WARDEN)
                .add(EntityType.ELDER_GUARDIAN)
                .addOptionalTag(new Identifier("c:bosses"));
        getOrCreateTagBuilder(Mystical.ZOMBIE_VARIANTS)
                .add(EntityType.ZOMBIE)
                .add(EntityType.HUSK)
                .add(EntityType.ZOMBIE_VILLAGER)
                .add(EntityType.ZOMBIFIED_PIGLIN)
                .add(EntityType.DROWNED);
        getOrCreateTagBuilder(Mystical.SKELETON_VARIANTS)
                .add(EntityType.SKELETON)
                .add(EntityType.WITHER_SKELETON)
                .add(EntityType.STRAY);
        getOrCreateTagBuilder(Mystical.ENDERMAN_VARIANTS)
                .add(EntityType.ENDERMAN)
                .add(EntityType.ENDERMITE);
        getOrCreateTagBuilder(Mystical.EVOKER_SUMMONABLE)
                .add(EntityType.CAT)
                .add(EntityType.SQUID);
    }
}
