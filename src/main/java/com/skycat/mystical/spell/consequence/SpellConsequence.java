package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("rawtypes")
public abstract class SpellConsequence {
    public static final Codec<SpellConsequence> CODEC = ConsequenceFactory.FACTORY_CODEC.dispatch("type", SpellConsequence::getFactory, ConsequenceFactory::getCodec);
    private final Class consequenceType;
    /**
     * If there is no relevant callback, this should be null
     */
    private final Class callbackType;
    private final double difficulty;

    public SpellConsequence(Class consequenceType, Class callbackType, double difficulty) {
        this.consequenceType = consequenceType;
        this.callbackType = callbackType;
        this.difficulty = difficulty;
    }

    /**
     * Returns the factory associated with this type of consequence.
     * {@code ? extends SpellConsequence} should ALWAYS be {@code this.getClass()}.
     *
     * @return The factory associated with this type of consequence.
     */
    @NotNull
    public abstract ConsequenceFactory<? extends SpellConsequence> getFactory(); // Finding a way to force ? to be this.getClass() would be nice

    public <T> boolean supportsEvent(Class<T> eventClass) {
        return eventClass.isInstance(this);
    }

    public Class getConsequenceType() {
        return this.consequenceType;
    }

    public Class getCallbackType() {
        return this.callbackType;
    }

    public double getDifficulty() {
        return this.difficulty;
    }

    public void onActivated(MinecraftServer server) {}
}
