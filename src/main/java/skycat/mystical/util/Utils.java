package skycat.mystical.util;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import skycat.mystical.LogLevel;
import skycat.mystical.Mystical;

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

    public static void sendMessageToPlayer(ServerPlayerEntity player, String msg) {
        sendMessageToPlayer(player, textOf(msg)); // Converts String to Text
    }

    public static void sendMessageToPlayer(ServerPlayerEntity player, String msg, boolean actionBar) {
        sendMessageToPlayer(player, textOf(msg), actionBar); // Converts String to Text
    }

    public static void sendMessageToPlayer(ServerPlayerEntity player, Text msg) {
        sendMessageToPlayer(player, msg, false); // Assumes actionBar = false
    }

    public static void sendMessageToPlayer(ServerPlayerEntity player, Text msg, boolean actionBar) {
        player.sendMessage(msg, actionBar);
    }

    public static Text textOf(String str) {
        return Text.of(str);
    }

}
