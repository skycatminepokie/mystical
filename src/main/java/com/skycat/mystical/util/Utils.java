package com.skycat.mystical.util;

import com.demonwav.mcdev.annotations.Translatable;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.test.TestContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Supplier;

public class Utils {
    /**
     * {@code Registries.STAT_TYPE.getCodec} is for regular StatTypes
     * {@code Identifier.CODEC} is for custom StatTypes
     */
    public static final Codec<Either<StatType<?>, Identifier>> STAT_TYPE_CODEC = Codec.either(Registries.STAT_TYPE.getCodec(), Identifier.CODEC);
    public static final Codec<Class<?>> CLASS_CODEC = Codec.STRING.xmap(className -> {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }, Class::getName); // Arguments are essentially Class::forNameOrThrow, Class::getName (forNameOrThrow doesn't exist, but that's what this is)
    public static final Codec<ChunkPos> CHUNK_POS_CODEC = Codec.LONG.xmap(ChunkPos::new, ChunkPos::toLong);

    /**
     * Create a Codec to store HashMaps as a List of Pairs.
     * @param keyCodec The codec for the key type
     * @param valueCodec The codec for the value type
     * @return A new codec
     * @param <K> The key type
     * @param <V> The value type
     * @deprecated Please provide field names with {@link Utils#hashMapCodec(Codec, String, Codec, String)}
     */
    @Deprecated
    public static <K, V> Codec<HashMap<K, V>> hashMapCodec(Codec<K> keyCodec, Codec<V> valueCodec) {
        return hashMapCodec(keyCodec, "first", valueCodec, "second");
    }

    /**
     * Create a Codec to store HashMaps as a List of Pairs.
     *
     * @param <K>            The key type
     * @param <V>            The value type
     * @param keyCodec       The codec for the key type
     * @param keyFieldName   The name to use for the first field
     * @param valueCodec     The codec for the value type
     * @param valueFieldName The name to use for the second field
     * @return A new codec
     */
    public static <K, V> Codec<HashMap<K, V>> hashMapCodec(Codec<K> keyCodec, String keyFieldName, Codec<V> valueCodec, String valueFieldName) {
        return Utils.pairCodec(keyCodec, keyFieldName, valueCodec, valueFieldName).listOf().xmap(Utils::pairsToMap, Utils::mapToPairs);
    }

    private static <K, V> HashMap<K, V> pairsToMap(List<Pair<K, V>> pairs) {
        HashMap<K, V> map = new HashMap<>(/*pairs.size()*/);
        for (Pair<K, V> pair : pairs) {
            map.put(pair.getFirst(), pair.getSecond());
        }
        return map;
    }

    public static <K, V> LinkedList<Pair<K, V>> mapToPairs(Map<K, V> map) {
        LinkedList<Pair<K, V>> list = new LinkedList<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            list.add(mapEntryToPair(entry));
        }
        return list;
    }

    public static <K, V> Pair<K, V> mapEntryToPair(Map.Entry<K, V> entry) {
        return Pair.of(entry.getKey(), entry.getValue());
    }

    public static <F, S> Codec<Pair<F,S>> pairCodec(Codec<F> firstCodec, String firstFieldName, Codec<S> secondCodec, String secondFieldName) {
        return Codec.pair(firstCodec.fieldOf(firstFieldName).codec(), secondCodec.fieldOf(secondFieldName).codec());
    }

    public static <T> List<T> setToList(Set<T> set) {
        return set.stream().toList();
    }

    public static <K, V> HashMap<K, V> toHashMap(Map<K, V> map) {
        if (map instanceof HashMap<K,V> hashMap) { // I think this will save some time since it doesn't rehash.
            return hashMap;
        }
        return new HashMap<>(map);
    }

    public static boolean log(String msg) {
        return log(msg, LogLevel.INFO, Mystical.LOGGER);
    }

    public static boolean log(String msg, Logger logger) {
        return log(msg, LogLevel.INFO, logger);
    }

    public static boolean log(String msg, LogLevel level) {
        return log(msg, level, Mystical.LOGGER);
    }

    public static boolean log(String msg, LogLevel level, Logger logger) {
        switch (level) {
            case INFO: logger.info(msg); return logger.isInfoEnabled();
            case DEBUG: logger.debug(msg); return logger.isDebugEnabled();
            case WARN: logger.warn(msg); return logger.isWarnEnabled();
            case ERROR: logger.error(msg); return logger.isErrorEnabled();
            default: return false; // case OFF
        }
    }

    public static void sendMessageToPlayer(PlayerEntity player, String msg) {
        sendMessageToPlayer(player, textOf(msg)); // Converts String to Text
    }

    public static <T> T getRandomEntryFromTag(Registry<T> registry, TagKey<T> tag) {
        var entryListOptional = registry.getEntryList(tag);
        if (entryListOptional.isEmpty()) return null;
        var entryList = entryListOptional.get();
        var entry = entryList.getRandom(Mystical.MC_RANDOM);
        //noinspection OptionalIsPresent
        if (entry.isEmpty()) return null;
        return entry.get().value();
    }

    public static void sendMessageToPlayer(PlayerEntity player, String msg, boolean actionBar) {
        sendMessageToPlayer(player, textOf(msg), actionBar); // Converts String to Text
    }

    public static void sendMessageToPlayer(PlayerEntity player, Text msg) {
        sendMessageToPlayer(player, msg, false); // Assumes actionBar = false
    }

    public static void sendMessageToPlayer(PlayerEntity player, Text msg, boolean actionBar) {
        player.sendMessage(msg, actionBar);
    }

    public static Text textOf(String str) {
        return Text.of(str);
    }

    public static Supplier<Text> textSupplierOf(String str) {
        return () -> textOf(str);
    }


    public static MutableText mutableTextOf(String str) {
        return Text.of(str).copy();
    }

    /**
     * Converts a target mob to a random mob from the given tag. <p>
     * Will pick up to 10 random types, stopping and doing the conversion when finding something that {@code extends} {@link MobEntity}.
     *
     * @param toConvert The mob to convert
     * @param tag The tag to choose from
     * @return The converted mob
     */
    @Nullable
    public static MobEntity convertToRandomInTag(MobEntity toConvert, TagKey<EntityType<?>> tag) {
        return convertToRandomInTag(toConvert, tag, Mystical.MC_RANDOM);
    }

    /**
     * Converts a target mob to a random mob from the given tag. <p>
     * Will pick up to 10 random types, stopping and doing the conversion when finding something that {@code extends} {@link MobEntity}.
     *
     * @param toConvert The mob to convert
     * @param tag       The tag to choose from
     * @return The converted mob
     */
    @Nullable
    public static MobEntity convertToRandomInTag(MobEntity toConvert, TagKey<EntityType<?>> tag, net.minecraft.util.math.random.Random random) {
        EntityType<?> randomType = Utils.getRandomEntryFromTag(Registries.ENTITY_TYPE, tag);
        if (randomType == null) {
            Utils.log("Failed to get randomType to convert to.", LogLevel.WARN);
            return null;
        }
        MobEntity newEntity = null;
        for (int i = 0; i < 10; i++) {
            try { // Checking the cast here
                //noinspection unchecked
                newEntity = toConvert.convertTo((EntityType<MobEntity>) randomType, true);
                break;
            } catch (ClassCastException e) {
                Utils.log("EntityType<?>" + randomType.getName().getString() + " in " + tag.id() + " - ? doesn't extend MobEntity - Attempt #" + (i + 1) + " :(", LogLevel.WARN);
            }
        }
        if (newEntity == null) {
            Utils.log("Failed to convert - see previous logging and check tags. For now, we'll skip it.", LogLevel.ERROR);
            return null;
        }
        return newEntity;
    }

    /**
     * Gives a status effect to an entity
     *
     * @param entity       The entity to give the status effect to
     * @param statusEffect The status effect to give
     * @param length       The length of the effect
     * @param level        The level (amplifier) of the effect.
     */
    public static void giveStatusEffect(LivingEntity entity, StatusEffect statusEffect, int length, int level) {
        entity.addStatusEffect(new StatusEffectInstance(statusEffect, length, level));
    }

    /**
     * Gives a status effect to an entity
     * @param entity The entity to give the status effect to
     * @param statusEffect The status effect to give
     * @param length The length of the effect
     * @param addLength If true and the entity already has the effect, it will extend the effect time by {@code length}
     *                  If false, the longest length (either new or old) will be kept
     * @param level The level (amplifier) of the effect.
     * @param addLevel If true and the entity already has the effect, it will increase the effect level by {@code level}
     *                 If false, the largest level (either new or old) will be kept
     */
    public static void giveStatusEffect(LivingEntity entity, StatusEffect statusEffect, int length, boolean addLength, int level, boolean addLevel) {
        StatusEffectInstance currentEffect = entity.getStatusEffect(statusEffect);
        if (currentEffect != null) {
            int currentEffectLength = currentEffect.getDuration();
            int currentEffectLevel = currentEffect.getAmplifier();
            if (addLength) {
                length += currentEffectLength;
            } else if (currentEffectLength > length) {
                length = currentEffectLength;
            }
            if (addLevel) {
                level += currentEffectLevel;
            } else if (currentEffectLevel > level) {
                level = currentEffectLevel;
            }
        }
        giveStatusEffect(entity, statusEffect, length, level);
    }

    /**
     * Returns a random element from a list
     *
     * @param <T>    The type of the list's elements
     * @param random The Random object to use
     * @param list   The list of elements
     * @return A random element from a list
     */
    public static <T> T chooseRandom(Random random, List<T> list) {
        return list.get(random.nextInt(0, list.size()));
    }

    public static MutableText translatable(@Translatable String path) {
        return Text.translatable(path);
    }

    public static MutableText translatable(@Translatable String path, @Translatable Object... args) {
        return Text.translatable(path, args);
    }

    public static Supplier<Text> translatableSupplier(@Translatable String path) {
        return () -> Text.translatable(path);
    }

    public static Supplier<Text> translatableSupplier(@Translatable String path, Object... args) {
        return () -> Text.translatable(path, args);
    }

    public static String translateString(@Translatable String path) {
        return translatable(path).getString();
    }

    public static String translateString(@Translatable String path, Object... args) {
        return translatable(path, args).getString();
    }

    /**
     * Returns {@code true} about chance% of the time.
     * @param chance The chance to return true (out of 100)
     * @return {@code true} about chance% of the time.
     */
    public static boolean percentChance(double chance) {
        return Mystical.RANDOM.nextDouble(0, 100) < chance;
    }

    /**
     * Returns {@code true} about chance% of the time.
     * @param chance The chance to return true (out of 100)
     * @param random The {@link Random} to use.
     * @return {@code true} about chance% of the time.
     */
    public static boolean percentChance(double chance, Random random) {
        return random.nextDouble(0, 100) < chance;
    }

    /**
     * Gets the translation for a stat as shown on the stats screen (ex "Times Mined")
     *
     * @param stat The stat to get the translation for
     * @return The translation
     */
    public static MutableText translateStat(Stat<?> stat) {
        if (stat.getValue() instanceof Identifier) {
            return translatable("stat." + stat.getValue().toString().replace(':', '.'));
        }
        return stat.getType().getName().copy();
    }

    public static StatusEffect getRandomStatusEffect() {
        Optional<RegistryEntry.Reference<StatusEffect>> entry =  Registries.STATUS_EFFECT.getRandom(Mystical.MC_RANDOM);
        if (entry.isPresent()) {
            return entry.get().value();
        }
        log("Couldn't get a random status effect, using absorption instead. This probably shouldn't happen. Dumping stack.", LogLevel.ERROR);
        Thread.dumpStack(); // https://stackoverflow.com/a/945020
        return StatusEffects.ABSORPTION;
    }

    public static ServerPlayerEntity createMockCreativeServerPlayerEntity(TestContext context) {
        return context.createMockCreativeServerPlayerInWorld();
    }

    /**
     * Get a random value from a {@link Registry}. Simple addition to {@link Utils#getRandomRegistryEntry(Registry)}
     * @param registry The registry to get from.
     * @return A random value, or {@code null} if the registry is empty (see {@link Registry#getRandom(net.minecraft.util.math.random.Random)}).
     * @param <T> The type parameter of the registry.
     */
    public static <T> @Nullable T getRandomRegistryValue(Registry<T> registry) {
        RegistryEntry<T> registryEntry = getRandomRegistryEntry(registry);
        if (registryEntry == null) return null;
        return registryEntry.value();
    }

    /**
     * Get a random entry from a {@link Registry}.
     * @param registry The registry to get from.
     * @return A random entry, or {@code null} if the registry is empty (see {@link Registry#getRandom(net.minecraft.util.math.random.Random)}).
     * @param <T> The type parameter of the registry.
     */
    public static <T> @Nullable RegistryEntry<T> getRandomRegistryEntry(Registry<T> registry) {
        Optional<RegistryEntry.Reference<T>> optionalEntry = registry.getRandom(Mystical.MC_RANDOM);
        return optionalEntry.orElse(null);
    }

    public static String camelCaseToSnakeCase(String camelCase) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_');
            }
            result.append(Character.toLowerCase(c));
        }
        return result.toString();
    }
}
