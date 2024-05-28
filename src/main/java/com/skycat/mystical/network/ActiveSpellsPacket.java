package com.skycat.mystical.network;

import com.skycat.mystical.spell.Spell;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.List;

public record ActiveSpellsPacket(List<Spell> spells) implements CustomPayload {
    public static final CustomPayload.Id<ActiveSpellsPacket> ID = new CustomPayload.Id<>(MysticalNetworking.ACTIVE_SPELLS_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, ActiveSpellsPacket> CODEC = PacketCodecs.codec(Spell.CODEC.listOf()).xmap(ActiveSpellsPacket::new, ActiveSpellsPacket::spells).cast();

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
