package com.skycat.mystical.util.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class CatEntityEvents {
    public static final Event<Eat> EAT = EventFactory.createArrayBacked(Eat.class, (callbacks) -> (cat, player, hand, stack) -> {
        for (Eat eat : callbacks) {
            eat.onEat(cat, player, hand, stack);
        }
    });

            @FunctionalInterface
    public interface Eat {
        void onEat(CatEntity cat, PlayerEntity player, Hand hand, ItemStack stack);
    }
}
