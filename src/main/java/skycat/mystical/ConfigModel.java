package skycat.mystical;

import io.wispforest.owo.config.annotation.*;

@SuppressWarnings("unused") // They are used by owo-config
@Modmenu(modId = "mystical")
@Config(name = "mysticalConfig", wrapperName = "MysticalConfig")
public class ConfigModel {
    @SectionHeader("General")
    public boolean devMode = false; // Not implemented

    @SectionHeader("Spells")
    @Nest public BigCreeperExplosionConfig bigCreeperExplosion = new BigCreeperExplosionConfig();
    @Nest public RandomTreeTypeConfig randomTreeType = new RandomTreeTypeConfig();
    @Nest public ZombieTypeChangeConfig zombieTypeChange = new ZombieTypeChangeConfig();
    @Nest public SkeletonTypeChangeConfig skeletonTypeChange = new SkeletonTypeChangeConfig();
    @Nest public EnderTypeChangeConfig enderTypeChange = new EnderTypeChangeConfig(); // TODO: Translate


    @SectionHeader("Logging") // Note: Logging as ERROR level does not always mean a critical error.
    public LogLevel failedToSetNightTimerLogLevel = LogLevel.WARN;
    public LogLevel timeOfDayAtStartupLogLevel = LogLevel.DEBUG;
    public LogLevel failedToLoadHavenManagerLogLevel = LogLevel.INFO;
    public LogLevel failedToSaveHavenManagerLogLevel = LogLevel.INFO;
    public LogLevel playerContributedLogLevel = LogLevel.OFF; // Not implemented
    public LogLevel failedToGetRandomBlockLogLevel = LogLevel.ERROR;
    public LogLevel failedToLoadSpellHandlerLogLevel = LogLevel.WARN;
    public LogLevel failedToSaveSpellHandlerLogLevel = LogLevel.ERROR;
    public LogLevel newSpellCommandLogLevel = LogLevel.INFO;

    public static class BigCreeperExplosionConfig {
        public boolean enabled = true; // Not implemented
        @RangeConstraint(min = 0.0001d, max = 100.0d)
        public double multiplier = 2.0;
        @RangeConstraint(min = 0.0001d, max = 100.0d)
        public double chance = 100.0;
        public LogLevel logLevel = LogLevel.OFF;
        public double weight = 1; // Not implemented
    }
    
    public static class RandomTreeTypeConfig {
        public boolean enabled = true; // Not implemented
        @RangeConstraint(min = 0.0001d, max = 100.0d)
        public double chance = 100.0;
        // public boolean fromAcacia = true; // Not implemented // TODO: Config
        // public boolean fromAzalea = true; // Not implemented // TODO: Config
        // public boolean fromBirch = true; // Not implemented // TODO: Config
        // public boolean fromDarkOak = true; // Not implemented // TODO: Config
        // public boolean fromJungle = true; // Not implemented // TODO: Config
        // public boolean fromMangrove = true; // Not implemented // TODO: Config
        // public boolean fromOak = true; // Not implemented // TODO: Config
        // public boolean fromSpruce = true; // Not implemented // TODO: Config
        // public boolean toAcacia = true; // Not implemented // TODO: Config
        // public boolean toAzalea = true; // Not implemented // TODO: Config
        // public boolean toBirch = true; // Not implemented // TODO: Config
        // public boolean toDarkOak = true; // Not implemented // TODO: Config
        // public boolean toJungle = true; // Not implemented // TODO: Config
        // public boolean toMangrove = true; // Not implemented // TODO: Config
        // public boolean toOak = true; // Not implemented // TODO: Config
        // public boolean toSpruce = true; // Not implemented // TODO: Config
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        public double weight = 1; // Not implemented
    }

    public static class LevitateConfig {
        public boolean enabled = true; // Not implemented
        public double chance = 100.0; // Not implemented
        // TODO: block/event type options
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        public double weight = 1; // Not implemented
    }

    public static class ZombieTypeChangeConfig {
        public boolean enabled = true; // Not implemented
        @RangeConstraint(min = 0.0001d, max = 100.0d)
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        public double weight = 1; // Not implemented
    }

    public static class SkeletonTypeChangeConfig {
        public boolean enabled = true; // Not implemented
        @RangeConstraint(min = 0.0001d, max = 100.0d)
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        public double weight = 1; // Not implemented
    }

    public static class EnderTypeChangeConfig {
        public boolean enabled = true; // Not implemented
        @RangeConstraint(min = 0.0001d, max = 100.0d)
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        public double weight = 1; // Not implemented
    }

}