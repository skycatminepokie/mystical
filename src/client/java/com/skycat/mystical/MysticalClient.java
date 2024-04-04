package com.skycat.mystical;

import com.skycat.mystical.network.ClientNetworkHandler;
import com.skycat.mystical.network.MysticalNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class MysticalClient implements ClientModInitializer {
    public static final ClientNetworkHandler CLIENT_NETWORK_HANDLER = new ClientNetworkHandler();
    public static final HudManager HUD_MANAGER = new HudManager();
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(MysticalNetworking.ACTIVE_SPELLS, CLIENT_NETWORK_HANDLER);
    }
}