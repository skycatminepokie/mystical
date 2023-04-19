package com.skycat.mystical.common.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.LogLevel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;

import java.util.List;
import java.util.Random;

public class Utils {
    /**
     * {@code Registry.STAT_TYPE.getCodec} is for regular StatTypes
     * {@code Identifier.CODEC} is for custom StatTypes
     */
    public static final Codec<Either<StatType<?>, Identifier>> STAT_TYPE_CODEC = Codec.either(Registry.STAT_TYPE.getCodec(), Identifier.CODEC);
    // public static final Codec<Pair<Either<StatType<?>, Identifier>, Stat<?>>> STAT_CODEC = Codec.pair(STAT_TYPE_CODEC, )

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

    public static MutableText mutableTextOf(String str) {
        return Text.of(str).copy();
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

    public static MutableText translatable(String path) {
        return Text.translatable(path);
    }

    public static MutableText translatable(String path, Object... args) {
        return Text.translatable(path, args);
    }

    public static String translateString(String path) {
        return translatable(path).getString();
    }

    public static String translateString(String path, Object... args) {
        return translatable(path, args).getString();
    }

    public static boolean percentChance(double chance) {
        return Mystical.RANDOM.nextDouble(0, 100) < chance;
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
}
