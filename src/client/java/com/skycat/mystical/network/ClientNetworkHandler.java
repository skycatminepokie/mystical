package com.skycat.mystical.network;

import com.skycat.mystical.MysticalClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworkHandler implements ClientPlayNetworking.PlayPayloadHandler<ActiveSpellsPacket> {

    @Override
    public void receive(ActiveSpellsPacket payload, ClientPlayNetworking.Context context) {
        if (payload instanceof ActiveSpellsPacket spellsPacket) {
            MysticalClient.HUD_MANAGER.updateCachedSpells(spellsPacket.spells());
        }
    }
}
