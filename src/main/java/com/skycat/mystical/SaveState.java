package com.skycat.mystical;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.spell.SpellHandler;
import com.skycat.mystical.util.Utils;
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
        var result = CODEC.decode(NbtOps.INSTANCE, save.get("mystical_save"));
        if (result.get().left().isPresent()) { // If we successfully loaded the PersistentState
            return result.get().left().get().getFirst();
        }
        Utils.log("Failed to load PersistentState - this is normal when updating versions from below 4.1.0");
        return null;
    }

    @Override
    public boolean isDirty() {
        return super.isDirty() || havenManager.isDirty() || spellHandler.isDirty();
    }

    @Override
    public void setDirty(boolean dirty) {
        if (!dirty) {
            havenManager.setDirty(false);
            spellHandler.setDirty(false);
        }
        super.setDirty(dirty);
    }
}
