package skycat.mystical.spell.consequence;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BigCreeperExplosionConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    private BigCreeperExplosionConsequence(Class consequenceType, Class callbackType) {
        super(consequenceType, callbackType, "text.mystical.spellConsequence.bigCreeperExplosion"); // TODO: TRANSLATE
    }

    public static class Factory implements ConsequenceFactory<BigCreeperExplosionConsequence> {
        @NotNull
        @Override
        public BigCreeperExplosionConsequence make(@NonNull Random random, double points) {
            return new BigCreeperExplosionConsequence(BigCreeperExplosionConsequence.class, BigCreeperExplosionConsequence.class);
        }
    }
}
