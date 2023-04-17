package com.skycat.mystical.client;

import com.skycat.mystical.client.command.MysticalClientCommandHandler;
import com.skycat.mystical.client.gui.CureConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;

public class MysticalClient implements ClientModInitializer {
    private static boolean shouldOpenConfig = false;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(new MysticalClientCommandHandler());
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hello").executes(context -> {
                context.getSource().sendFeedback(Text.literal("Hello, world!"));
                return 0;
            }));
        });
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (shouldOpenConfig) {
                client.setScreen(new CureConfigScreen());
                shouldOpenConfig = false;
            }
        });
    }

    /**
     * Opens the config at the end of the next client tick
     */
    public static void openConfig() {
        shouldOpenConfig = true;
    }
}
