package com.skycat.mystical.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.common.spell.SpellHandler;
import com.skycat.mystical.common.util.Utils;
import lombok.Getter;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

public class SaveState extends PersistentState {
    @Getter protected HavenManager havenManager;
    @Getter protected SpellHandler spellHandler;
    public static final Codec<SaveState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            HavenManager.CODEC.fieldOf("havenManager").forGetter(SaveState::getHavenManager),
            SpellHandler.CODEC.fieldOf("spellHandler").forGetter(SaveState::getSpellHandler)
    ).apply(instance, SaveState::new));

    public SaveState() {
        this(new HavenManager(), new SpellHandler());
    }

    public SaveState(HavenManager havenManager, SpellHandler spellHandler) {
        this.havenManager = havenManager;
        this.spellHandler = spellHandler;
    }
    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.put("mystical_save", CODEC.encode(this, NbtOps.INSTANCE, NbtOps.INSTANCE.empty()).getOrThrow(false, s -> Utils.log("Failed to save mystical data.")));
        return nbt;
    }

    public static SaveState loadSave(MinecraftServer server) {
        PersistentStateManager stateManager = server.getOverworld().getPersistentStateManager();

        return stateManager.getOrCreate(SaveState::readFromNbt, SaveState::new, "mystical");
    }

    private static SaveState readFromNbt(NbtCompound save) {
        return CODEC.decode(NbtOps.INSTANCE, save).getOrThrow(false, s -> Utils.log("Failed to load save data.")).getFirst();
    }
}
