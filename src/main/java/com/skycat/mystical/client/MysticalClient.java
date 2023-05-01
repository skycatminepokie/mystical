package com.skycat.mystical.client;

import com.skycat.mystical.client.command.MysticalClientCommandHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;

public class MysticalClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(new MysticalClientCommandHandler());
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("hello").executes(context -> {
                context.getSource().sendFeedback(Text.literal("Hello, world!"));
                return 0;
            }));
        });
    }
}
