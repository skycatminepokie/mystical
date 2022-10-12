package skycat.mystical;


import lombok.Getter;
import lombok.Setter;
import org.slf4j.event.Level;

/**
 * Used to save settings for Mystical. This does not include game state, but rather configuration and preferences.
 */
public class Settings {
    public Settings(boolean logCreation) {
        if (logCreation) {
            Utils.log("Mystical created a new settings file", LoggingSettings.getNewSaveLogLevel());
        }
    }

    public static class LoggingSettings {
        @Getter @Setter public static LogLevel newSaveLogLevel = LogLevel.INFO;
        @Getter @Setter public static LogLevel savingLogLevel = LogLevel.OFF;
        @Getter @Setter public static LogLevel newSettingsFileLogLevel = LogLevel.INFO;

    }
}
