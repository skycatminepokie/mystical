package skycat.mystical.spell.consequence;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FishingRodLaunchConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    public FishingRodLaunchConsequence(Class consequenceType, Class callbackType) {
        super(consequenceType, callbackType, "fishingRodLaunch", "Fishing Rod Launch", "Hehe. Rod make cow go zoom.");
    }

    public FishingRodLaunchConsequence() {
        this(FishingRodLaunchConsequence.class, FishingRodLaunchConsequence.class);
    }

    public static class Factory implements ConsequenceFactory<FishingRodLaunchConsequence> {

        @Override
        public @NotNull FishingRodLaunchConsequence make(@NonNull Random random, double points) {
            return new FishingRodLaunchConsequence();
        }
    }
}
