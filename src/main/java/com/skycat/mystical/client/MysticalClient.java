package com.skycat.mystical.client;

import com.skycat.mystical.client.command.MysticalClientCommandHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class MysticalClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(new MysticalClientCommandHandler());
    }
}
