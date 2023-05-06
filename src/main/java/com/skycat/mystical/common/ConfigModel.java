package com.skycat.mystical.common;

import io.wispforest.owo.config.annotation.*;

@SuppressWarnings("unused") // They are used by owo-config
@Modmenu(modId = "mystical")
@Config(name = "mysticalConfig", wrapperName = "MysticalConfig")
public class ConfigModel {
    @SectionHeader("General")
    public boolean devMode = false; // Not implemented

    @SectionHeader("Spells")
    @Nest public BigCreeperExplosionConfig bigCreeperExplosion = new BigCreeperExplosionConfig();
    @Nest public CatVariantChangeConfig catVariantChange = new CatVariantChangeConfig();
    @Nest public EnderTypeChangeConfig enderTypeChange = new EnderTypeChangeConfig();
    @Nest public FishingRodLaunchConfig fishingRodLaunch = new FishingRodLaunchConfig();
    @Nest public LevitateConfig levitate = new LevitateConfig();
    @Nest public RandomTreeTypeConfig randomTreeType = new RandomTreeTypeConfig();
    @Nest public SheepColorChangeConfig sheepColorChange = new SheepColorChangeConfig();
    @Nest public ZombieTypeChangeConfig zombieTypeChange = new ZombieTypeChangeConfig();
    @Nest public SkeletonTypeChangeConfig skeletonTypeChange = new SkeletonTypeChangeConfig();
    @Nest public DisableDaylightBurningConfig disableDaylightBurning = new DisableDaylightBurningConfig();
    @Nest public NoFuseConfig noFuse = new NoFuseConfig();
    @Nest public MobSpawnSwapConfig mobSpawnSwap = new MobSpawnSwapConfig();

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
    public boolean newSpellCommandBroadcast = true;

    public static class BigCreeperExplosionConfig {
        public boolean enabled = true; // Not implemented
        public double multiplier = 2.0;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1; // Not implemented

        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class CatVariantChangeConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class EnderTypeChangeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class FishingRodLaunchConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public double multiplier = 20;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class LevitateConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        // TODO: block/event type options
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class RandomTreeTypeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 100.0;
        // TODO: Config tree types
        public LogLevel logLevel = LogLevel.OFF; // Not implemented
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class SheepColorChangeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class SkeletonTypeChangeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;
        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class ZombieTypeChangeConfig {
        public boolean enabled = true;
        @PredicateConstraint("chancePredicate")
        public double chance = 25.0;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean chancePredicate(double value) {
            return ConfigModel.chancePredicate(value);
        }

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class DisableDaylightBurningConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class NoFuseConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    public static class MobSpawnSwapConfig {
        public boolean enabled = true;
        public LogLevel logLevel = LogLevel.OFF;
        @PredicateConstraint("weightPredicate")
        public double weight = 1;

        public static boolean weightPredicate(double value) {
            return ConfigModel.weightPredicate(value);
        }
    }

    /**
     * Verify that the chance is valid.
     * Used instead of {@link RangeConstraint} because it doesn't make a slider.
     *
     * @param value The chance value to check
     * @return value >= 0 && value <= 100
     */
    public static boolean chancePredicate(double value) {
        return value >= 0 && value <= 100;
    }

    /**
     * Verify that the given weight is valid.
     * Used instead of {@link RangeConstraint} because it doesn't make a slider.
     * @param value The weight to check
     * @return value >= 0
     */
    public static boolean weightPredicate(double value) {
        return value >= 0;
    }

}