package skycat.mystical;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Nest;
import io.wispforest.owo.config.annotation.SectionHeader;
import skycat.mystical.curses.Curse;

import java.util.ArrayList;

@SuppressWarnings("unused") // They are used by owo-config
@Config(name = "mysticalConfig", wrapperName = "MysticalConfig")
public class ConfigModel {
    @SectionHeader("General")
    public ArrayList<Curse> activeCurses = new ArrayList<>();
    public boolean removeDisabledCurses = true; // Not implemented

    // TODO Curse toggles
    @SectionHeader("Curses")
    @Nest
    public DamageEquipmentOnChangeCurse DamageEquipmentOnChangeCurse = new DamageEquipmentOnChangeCurse();

    // @SectionHeader("Cures")

    @SectionHeader("Logging")
    public LogLevel newSaveFileCreatedLogLevel = LogLevel.INFO; // Not implemented
    public LogLevel newSettingsFileCreatedLogLevel = LogLevel.INFO; // Not implemented
    public LogLevel failedToSetNightTimerLogLevel = LogLevel.WARN; // Not implemented
    public LogLevel savingLogLevel = LogLevel.INFO; // Not implemented
    public LogLevel timeOfDayAtStartupLogLevel = LogLevel.DEBUG; // Not implemented

    public static class DamageEquipmentOnChangeCurse {
        public boolean enabled = true; // Not implemented
        public int damageAmount = 5; // Not implemented
        public boolean notifyPlayer = false; // Not implemented
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
    }
}
