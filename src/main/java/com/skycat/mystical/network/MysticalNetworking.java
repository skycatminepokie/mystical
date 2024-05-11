package com.skycat.mystical.network;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.Spell;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MysticalNetworking implements ServerPlayConnectionEvents.Join {
    public static final @NotNull Identifier ACTIVE_SPELLS_PACKET_ID = Objects.requireNonNull(Identifier.of(Mystical.MOD_ID, "active_spells"));
    public static final String SPELLS_KEY = "spells";

    public void sendActiveSpells(MinecraftServer server) { // TODO: Send on join and change
        CustomPayloadS2CPacket packet = makeActiveSpellPacket(server);
        for (ServerPlayerEntity player : PlayerLookup.all(server)) {
            player.networkHandler.sendPacket(packet);
        }
    }

    private static CustomPayloadS2CPacket makeActiveSpellPacket(MinecraftServer server) {
        return new CustomPayloadS2CPacket(new ActiveSpellsPacket(Mystical.getSpellHandler().getActiveSpells()));
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        handler.sendPacket(new CustomPayloadS2CPacket(new ActiveSpellsPacket(Mystical.getSpellHandler().getActiveSpells())););
    }
}
