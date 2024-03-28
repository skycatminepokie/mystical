package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.util.LogLevel;
import com.skycat.mystical.util.Utils;
import lombok.NonNull;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * When this is active, a subset of sounds is swapped with other sounds.
 */
public class SoundSwapConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    protected final Map<Identifier, @NonNull Identifier> soundToSoundMap;

    protected SoundSwapConsequence(int swaps) {
        this(makeNewSoundMap(swaps));
    }

    protected SoundSwapConsequence(Map<Identifier, @NonNull Identifier> soundToSoundMap) {
        super(SoundSwapConsequence.class, null, 1d);
        this.soundToSoundMap = soundToSoundMap;
    }

    /**
     * Make a new map of sounds to be used for swapping.
     *
     * @param size The number of sounds to map (not zero-indexed!)
     * @return A new map.
     * @implNote If a random sound cannot be retrieved (key or value), that entry will be skipped. This may result in a map of size < {@code size}.
     */
    protected static HashMap<Identifier, @NonNull Identifier> makeNewSoundMap(int size) {
        HashMap<Identifier, @NonNull Identifier> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            RegistryEntry<SoundEvent> key = Utils.getRandomRegistryEntry(Registries.SOUND_EVENT);
            RegistryEntry<SoundEvent> value = Utils.getRandomRegistryEntry(Registries.SOUND_EVENT);
            if (key == null || value == null) {
                Utils.log("Could not get a random sound - Utils.getRandomRegistryEntry returned null", LogLevel.ERROR);
                continue;
            }
            map.put(key.value().getId(), value.value().getId());
        }
        return map;
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    protected Map<Identifier, @NonNull Identifier> getMap() {
        return soundToSoundMap;
    }

    protected @NotNull Identifier randomSoundEventIdentifier() {
        return Objects.requireNonNull(Utils.getRandomRegistryValue(Registries.SOUND_EVENT), "Could not get a random SoundEvent.").getId();
    }

    /**
     * @param sound The original sound.
     * @return The sound to swap the given sound with, or {@code sound} if there is no swap.
     */
    public @NotNull RegistryEntry<SoundEvent> swapSound(@NotNull RegistryEntry<SoundEvent> sound) {
        Identifier originalId = sound.value().getId(); // Original sound
        Identifier newSoundId = soundToSoundMap.getOrDefault(originalId, originalId); // New sound
        SoundEvent newSoundEvent = Registries.SOUND_EVENT.get(newSoundId); // New sound
        if (newSoundEvent == null) { // New sound doesn't exist anymore (might happen across restarts)
            Utils.log("Sound with ID: " + newSoundId + " is not registered for " + getFactory().shortName + ", choosing a new sound mapping. If you didn't change mods or versions recently, please report this at https://github.com/skycatminepokie/mystical/issues.", LogLevel.ERROR);
            newSoundId = soundToSoundMap.put(originalId, randomSoundEventIdentifier()); // Get a new one
            newSoundEvent = Registries.SOUND_EVENT.get(newSoundId);
            /*assert newSoundEvent != null;*/ // The Identifier should really point to an existing SoundEvent, since we just got it now.
        }
        return Registries.SOUND_EVENT.getEntry(newSoundEvent);
    }

    public static class Factory extends ConsequenceFactory<SoundSwapConsequence> {

        protected Factory() {
            super("soundSwap",
                    "Random sound swapping",
                    "Ears broken",
                    "Swapped a sound",
                    SoundSwapConsequence.class,
                    Codec.unboundedMap(Identifier.CODEC, Identifier.CODEC).xmap(SoundSwapConsequence::new, SoundSwapConsequence::getMap)
            );
        }

        @Override
        public double getWeight() {
            return Mystical.CONFIG.soundSwap.weight();
        }

        @Override
        public @NotNull SoundSwapConsequence make(@NonNull Random random, double points) {
            return new SoundSwapConsequence(Mystical.CONFIG.soundSwap.numberOfSwaps());
        }
    }
}
