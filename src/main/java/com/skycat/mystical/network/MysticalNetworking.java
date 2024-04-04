package com.skycat.mystical.network;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.Spell;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MysticalNetworking implements ServerPlayConnectionEvents.Join {
    public static final @NotNull Identifier ACTIVE_SPELLS = Objects.requireNonNull(Identifier.of(Mystical.MOD_ID, "active_spells"));
    public static final String SPELLS_KEY = "spells";

    public void sendActiveSpells(MinecraftServer server) { // TODO: Send on join and change
        CustomPayloadS2CPacket packet = makeActiveSpellPacket(server);
        for (ServerPlayerEntity player : PlayerLookup.all(server)) {
            player.networkHandler.sendPacket(packet);
        }
    }

    private static CustomPayloadS2CPacket makeActiveSpellPacket(MinecraftServer server) {
        PacketByteBuf packetBuf = PacketByteBufs.create();
        NbtCompound nbtSpells = new NbtCompound();
        NbtElement spells = Spell.CODEC
                .listOf()
                .encode(Mystical.getSpellHandler().getActiveSpells(), NbtOps.INSTANCE, NbtOps.INSTANCE.empty())
                .getOrThrow(false, (s) -> {throw new RuntimeException(s);});

        nbtSpells.put(SPELLS_KEY, spells);

        packetBuf.writeNbt(nbtSpells);
        return new CustomPayloadS2CPacket(ACTIVE_SPELLS, packetBuf);
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        handler.sendPacket(makeActiveSpellPacket(server));
    }
}
