package com.skycat.mystical.network;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.Spell;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public record ActiveSpellsPacket(List<Spell> spells) implements CustomPayload {
    public static final @NotNull Identifier IDENTIFIER = Objects.requireNonNull(Identifier.tryParse(Mystical.MOD_ID, "active_spells"));
    public static final CustomPayload.Id<ActiveSpellsPacket> ID = new CustomPayload.Id<>(IDENTIFIER);
    public static final PacketCodec<RegistryByteBuf, ActiveSpellsPacket> CODEC = PacketCodecs.codec(Spell.CODEC.listOf()).xmap(ActiveSpellsPacket::new, ActiveSpellsPacket::spells).cast();


    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
