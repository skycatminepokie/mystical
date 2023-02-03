package skycat.mystical;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;

@SuppressWarnings("unused") // They are used by owo-config
@Modmenu(modId = "mystical")
@Config(name = "mysticalConfig", wrapperName = "MysticalConfig")
public class ConfigModel {

    @SectionHeader("Logging")
    public LogLevel failedToSetNightTimerLogLevel = LogLevel.WARN;
    public LogLevel timeOfDayAtStartupLogLevel = LogLevel.DEBUG;
    public LogLevel failedToLoadHavenManager = LogLevel.INFO;
    public LogLevel failedToSaveHavenManager = LogLevel.INFO;
    public LogLevel playerContributed = LogLevel.OFF; // Not used
    public LogLevel failedToGetRandomBlock = LogLevel.ERROR;

}