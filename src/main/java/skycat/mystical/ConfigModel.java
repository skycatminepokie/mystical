package skycat.mystical;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.ExcludeFromScreen;
import skycat.mystical.curses.Curse;
import skycat.mystical.curses.CurseConsequence;
import skycat.mystical.curses.CurseHandler;
import skycat.mystical.curses.CurseRemovalCondition;

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
    public ArrayList<CurseConsequence> consequences = new ArrayList<>();
    public ArrayList<CurseRemovalCondition> removalConditions = new ArrayList<>();
    @ExcludeFromScreen
    public CurseHandler curseHandler = new CurseHandler();

    public int curseDamageMultiplier = 2;
    public int curseEquipmentChangeDamage = 25;
}
