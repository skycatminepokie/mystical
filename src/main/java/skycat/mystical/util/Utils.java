package skycat.mystical.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import skycat.mystical.LogLevel;
import skycat.mystical.Mystical;

import java.util.List;
import java.util.Random;

public class Utils {
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

    /**
     * Gives a status effect to an entity
     * @param entity The entity to give the status effect to
     * @param statusEffect The status effect to give
     * @param length The length of the effect
     * @param level The level (amplifier) of the effect.
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
}
