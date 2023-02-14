package skycat.mystical;

import io.wispforest.owo.config.annotation.*;

@SuppressWarnings("unused") // They are used by owo-config
@Modmenu(modId = "mystical")
@Config(name = "mysticalConfig", wrapperName = "MysticalConfig")
public class ConfigModel {
    @SectionHeader("General")
    public boolean devMode = false; // Not implemented

    @SectionHeader("Spells")
    @Nest public BigCreeperExplosionConsequence bigCreeperExplosionConsequence = new BigCreeperExplosionConsequence();


    @SectionHeader("Logging") // Note: Logging as ERROR level does not always mean a critical error.
    public LogLevel failedToSetNightTimerLogLevel = LogLevel.WARN;
    public LogLevel timeOfDayAtStartupLogLevel = LogLevel.DEBUG;
    public LogLevel failedToLoadHavenManagerLogLevel = LogLevel.INFO;
    public LogLevel failedToSaveHavenManagerLogLevel = LogLevel.INFO;
    public LogLevel playerContributedLogLevel = LogLevel.OFF; // Not implemented
    public LogLevel failedToGetRandomBlockLogLevel = LogLevel.ERROR;
    public LogLevel failedToLoadSpellHandlerLogLevel = LogLevel.WARN;
    public LogLevel failedToSaveSpellHandlerLogLevel = LogLevel.ERROR;

    public static class BigCreeperExplosionConsequence {
        public boolean enabled = true; // Not implemented
        @RangeConstraint(min = 0.0001d, max = 100.0d)
        public double multiplier = 2.0;
        @RangeConstraint(min = 0.0001d, max = 100.0d)
        public double chance = 100.0;
        public LogLevel logLevel = LogLevel.OFF;
        @RangeConstraint(min = 0.0001d, max = Double.MAX_VALUE)
        public double weight = 1; // Not implemented
    }
    
    public static class RandomTreeTypeConsequence {
        public boolean enabled = true; // Not implemented
        @RangeConstraint(min = 0.0001d, max = 100.0d)
        public double chance = 100.0; // Not implemented
        public boolean fromAcacia = true; // Not implemented
        public boolean fromAzalea = true; // Not implemented
        public boolean fromBirch = true; // Not implemented
        public boolean fromDarkOak = true; // Not implemented
        public boolean fromJungle = true; // Not implemented
        public boolean fromMangrove = true; // Not implemented
        public boolean fromOak = true; // Not implemented
        public boolean fromSpruce = true; // Not implemented
        public boolean toAcacia = true; // Not implemented
        public boolean toAzalea = true; // Not implemented
        public boolean toBirch = true; // Not implemented
        public boolean toDarkOak = true; // Not implemented
        public boolean toJungle = true; // Not implemented
        public boolean toMangrove = true; // Not implemented
        public boolean toOak = true; // Not implemented
        public boolean toSpruce = true; // Not implemented
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        public double weight = 1; // Not implemented
    }

    public static class LevitateConsequence {
        public boolean enabled = true; // Not implemented
        public double chance = 100.0; // Not implemented
        // TODO: block/event type options
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        public double weight = 1; // Not implemented
    }



}