package skycat.mystical.spell.consequence;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FishingRodLaunchConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    public FishingRodLaunchConsequence(Class consequenceType, Class callbackType) {
        this(consequenceType, callbackType, "text.mystical.spellConsequence.fishingRodLaunch");
    }

    public FishingRodLaunchConsequence(Class consequenceType, Class callbackType, String translationKey) {
        super(consequenceType, callbackType, translationKey);
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
