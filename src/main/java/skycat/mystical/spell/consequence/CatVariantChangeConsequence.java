package skycat.mystical.spell.consequence;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CatVariantChangeConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();
    public CatVariantChangeConsequence() { // TODO: Config
        super(CatVariantChangeConsequence.class, CatVariantChangeConsequence.class, "text.mystical.spellConsequence.catVariantChange");
    }

    public static class Factory implements ConsequenceFactory<CatVariantChangeConsequence> {
        @Override
        public @NotNull CatVariantChangeConsequence make(@NonNull Random random, double points) {
            return new CatVariantChangeConsequence();
        }
    }
}
