package com.skycat.mystical.datagen;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.advancement.MakeHavenCriterion;
import com.skycat.mystical.common.util.Utils;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class AdvancementProvider extends FabricAdvancementProvider {
    public static final String MAKE_HAVEN_ADVANCEMENT_ID = "mystical:make_haven";
    protected AdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement makeHavenAdvancement = Advancement.Builder.createUntelemetered()
                .display(
                        Items.ACACIA_SAPLING,
                        Utils.translatable("text.mystical.advancement.make_haven.title"),
                        Utils.translatable("text.mystical.advancement.make_haven.description"),
                        Identifier.of("minecraft", "textures/block/purpur_block.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion(String.valueOf(Mystical.MAKE_HAVEN_CRITERION.getId()), new MakeHavenCriterion.Conditions())
                .build(consumer, MAKE_HAVEN_ADVANCEMENT_ID);
    }
}
