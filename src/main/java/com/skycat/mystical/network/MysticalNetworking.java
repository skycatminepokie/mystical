package com.skycat.mystical.network;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.Spell;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MysticalNetworking {
    public static final Identifier ACTIVE_SPELLS = Identifier.of(Mystical.MOD_ID, "active_spells");

    public static void sendActiveSpells(MinecraftServer server) {
        PacketByteBuf packetBuf = PacketByteBufs.create();
        NbtCompound nbtSpells = new NbtCompound();
        NbtElement spells = Spell.CODEC
                .listOf()
                .encode(Mystical.getSpellHandler().getActiveSpells(), NbtOps.INSTANCE, NbtOps.INSTANCE.empty())
                .getOrThrow(false, (s) -> {throw new RuntimeException(s);});

        nbtSpells.put("spells", spells);

        packetBuf.writeNbt(nbtSpells);

        for (ServerPlayerEntity player : PlayerLookup.all(server)) {
            player.networkHandler.sendPacket(new CustomPayloadS2CPacket(ACTIVE_SPELLS, packetBuf));
        }
    }
}
