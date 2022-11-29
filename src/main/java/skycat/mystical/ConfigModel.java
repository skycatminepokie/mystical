package skycat.mystical;

import io.wispforest.owo.config.annotation.Config;
import skycat.mystical.curses.Curse;

import java.util.ArrayList;

@SuppressWarnings("unused") // They are used by owo-config
@Config(name = "mysticalConfig", wrapperName = "MysticalConfig")
public class ConfigModel {
    public LogLevel newSaveFileCreatedLogLevel = LogLevel.INFO;
    public LogLevel newSettingsFileCreatedLogLevel = LogLevel.INFO;
    public LogLevel failedToSetNightTimerLogLevel = LogLevel.WARN;
    public LogLevel savingLogLevel = LogLevel.INFO;
    public LogLevel timeOfDayAtStartupLogLevel = LogLevel.DEBUG;
    public ArrayList<Curse> activeCurses = new ArrayList<>();
    public int curseEquipmentChangeDamage = 5;
    // TODO Curse toggles
}
