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

    /**
     * Loads the state, supporting the old format. First, it will try to load the old format and delete old files.
     * If it fails, it will load the new format. If that doesn't work, it will create a save with the new format.
     * @return The loaded or created SaveState.
     */
    public static SaveState loadSupportOld() {
        if (HavenManager.getSAVE_FILE().exists() || SpellHandler.getSAVE_FILE().exists()) { // Load old format
            SaveState save = new SaveState(HavenManager.loadOrNew(), SpellHandler.loadOrNew());
            if (HavenManager.getSAVE_FILE().delete()) {
                Utils.log("Updated HavenManager to the new format.");
            }
            if (SpellHandler.getSAVE_FILE().delete()) {
                Utils.log("Updated SpellHandler to the new format.");
            }
            return save;
        }
        return new SaveState();
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
        return stateManager.getOrCreate(SaveState::readFromNbt, SaveState::loadSupportOld, "mystical");
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
