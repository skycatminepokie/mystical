package com.skycat.mystical.network;

import com.mojang.datafixers.util.Pair;
import com.skycat.mystical.MysticalClient;
import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.util.LogLevel;
import com.skycat.mystical.util.Utils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;

import java.util.List;
import java.util.Optional;

public class ClientNetworkHandler implements ClientPlayNetworking.PlayChannelHandler {
    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) { // Active spells packet
        NbtCompound nbt = buf.readNbt();
        if (nbt == null) {
            Utils.log(Utils.translateString("text.mystical.client.networkHandler.spellPacket.null"), LogLevel.WARN);
            return;
        }
        NbtElement nbtSpells = nbt.get(MysticalNetworking.SPELLS_KEY);
        Optional<Pair<List<Spell>, NbtElement>> decodedSpells = Spell.CODEC.listOf().decode(NbtOps.INSTANCE, nbtSpells).result();
        if (decodedSpells.isPresent()) {
            MysticalClient.HUD_MANAGER.updateCachedSpells(decodedSpells.get().getFirst());
        } else {
            Utils.log(Utils.translateString("text.mystical.client.networkHandler.spellPacket.failedDeserialize"), LogLevel.WARN);
        }
    }
}
