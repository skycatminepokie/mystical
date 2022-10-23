package skycat.mystical;


import lombok.Getter;
import lombok.Setter;

/**
 * Used to save settings for Mystical. This does not include game state, but rather configuration and preferences.
 */
public class Settings {
    public Settings(boolean logCreation) {
        if (logCreation) {
            Utils.log("Mystical created a new settings file", LoggingSettings.getNewSettingsFileCreated());
        }
    }

    public static class LoggingSettings {
        @Getter @Setter public static LogLevel newSaveFileCreated = LogLevel.INFO;
        @Getter @Setter public static LogLevel newSettingsFileCreated = LogLevel.INFO;
        @Getter @Setter public static LogLevel nightTimerSetFailed = LogLevel.WARN;
        @Getter @Setter public static LogLevel saving = LogLevel.INFO; // TODO
        @Getter @Setter public static LogLevel timeOfDayAtStartup = LogLevel.INFO;

    }
}
