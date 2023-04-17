package com.skycat.mystical.client.gui;

import com.skycat.mystical.common.spell.SpellGenerator;
import com.skycat.mystical.common.util.Utils;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class CureConfigScreen extends BaseOwoScreen<FlowLayout> {
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);
        LabelComponent title = Components.label(Text.of("Cure Config"));
        rootComponent.child(title);

        GridLayout grid = makeGrid();

        ScrollContainer<GridLayout> scrollContainer = Containers.verticalScroll(Sizing.fill(90), Sizing.fill(90), grid);
        rootComponent.child(scrollContainer);


    }

    private GridLayout makeGrid() {
        var factories = SpellGenerator.getConsequenceFactories();
        int rows = factories.size();
        int columns = 4;
        GridLayout grid = Containers.grid(Sizing.fill(100), Sizing.content(0), rows, columns);
        for (int i = 0; i < rows; i++) {
            grid.child(Components.label(Utils.textOf((i + 1) + ". ").copy().append(Utils.translatable(factories.get(i).getLongNameKey()))), i, 0);
            // grid.child(Components.label(Utils.translatable(factories.get(i).getDescriptionKey())), i, 2);
            int finalI = i;
            grid.child(Components.button(Utils.textOf("More"), (buttonComponent -> System.out.println(factories.get(finalI).shortName))), i, 2);
            grid.child(Components.checkbox(Text.of("Enabled")), i, 3);
        }
        return grid;
    }
}
