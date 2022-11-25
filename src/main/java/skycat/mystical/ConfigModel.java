package skycat.mystical;

import io.wispforest.owo.config.annotation.Config;
import skycat.mystical.curses.Curse;
import skycat.mystical.curses.CurseConsequence;
import skycat.mystical.curses.CurseRemovalCondition;

import java.util.ArrayList;

@Config(name = "mysticalConfig", wrapperName = "MysticalConfig")
public class ConfigModel { // TODO: Nothing implemented
    public LogLevel newSaveFileCreated = LogLevel.INFO;
    public LogLevel newSettingsFileCreated = LogLevel.INFO;
    public LogLevel nightTimerSetFailed = LogLevel.WARN;
    public LogLevel saving = LogLevel.INFO;
    public LogLevel timeOfDayAtStartup = LogLevel.DEBUG;
    public ArrayList<Curse> activeCurses = new ArrayList<>();
    public ArrayList<CurseConsequence> consequences = new ArrayList<>();
    public ArrayList<CurseRemovalCondition> removalConditions = new ArrayList<>();

    public int damageMultiplierCurseValue = 2;
    public int curseEquipmentChangeDamage = 25;
}
