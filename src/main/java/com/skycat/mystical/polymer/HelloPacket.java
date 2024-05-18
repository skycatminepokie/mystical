package com.skycat.mystical.polymer;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import eu.pb4.polymer.networking.api.ContextByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record HelloPacket() implements CustomPayload {
    @NotNull public static final Identifier ID = Objects.requireNonNull(Identifier.of(Mystical.MOD_ID, "hello"));
    public static final CustomPayload.Id<HelloPacket> PACKET_ID = new CustomPayload.Id<>(ID);
    public static final PacketCodec<ContextByteBuf, HelloPacket> CODEC = PacketCodecs.codec(Codec.unit(new HelloPacket())).cast();
    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
