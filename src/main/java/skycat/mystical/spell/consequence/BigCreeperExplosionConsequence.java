package skycat.mystical.spell.consequence;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import skycat.mystical.Mystical;

import java.util.Random;

public class BigCreeperExplosionConsequence extends SpellConsequence {
    public static final Factory FACTORY = new Factory();

    private BigCreeperExplosionConsequence() {
        super(BigCreeperExplosionConsequence.class, BigCreeperExplosionConsequence.class, "bigCreeperExplosion", "Bigger Creeper Explosions", "Creepers go boom. But more.");
    }

    public static class Factory implements ConsequenceFactory<BigCreeperExplosionConsequence> {
        @NotNull
        @Override
        public BigCreeperExplosionConsequence make(@NonNull Random random, double points) {
            return new BigCreeperExplosionConsequence();
        }

        @Override
        public double getChance() {
            return (Mystical.CONFIG.bigCreeperExplosion.enabled()?Mystical.CONFIG.bigCreeperExplosion.chance():0);
        }
    }
}
