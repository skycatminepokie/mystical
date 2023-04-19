package com.skycat.mystical.common.spell.cure;

import com.google.gson.*;
import com.skycat.mystical.common.util.Utils;
import lombok.Getter;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public abstract class SpellCure {

    @Getter protected int contributionGoal;
    @Getter protected final Class cureType;
    /**
     * Make sure to update {@link #contributionTotal} when adding to or removing from this
     */
    private final ArrayList<CureContribution> contributions = new ArrayList<>();
    @Getter protected final String translationKey;
    @Getter private int contributionTotal = 0;

    /**
     * Please provide a translation key with {@link SpellCure#SpellCure(int, Class, String)}
     */
    public SpellCure(int contributionGoal, Class cureType) {
        this(contributionGoal, cureType, "text.mystical.spellCure.default");
    }

    public SpellCure(int contributionGoal, Class cureType, String translationKey) {
        this.contributionGoal = contributionGoal;
        this.cureType = cureType;
        this.translationKey = translationKey;
    }

    /**
     * This is a player-readable description/"recipe" for the cure.
     * Override this if you have parameters to add to the translation.
     */
    public MutableText getDescription() {
        return Utils.translatable(translationKey);
    }

    /**
     * Recalculates the total value of all contributions.
     * Use {@link #getContributionTotal()} to get the total.
     *
     * @implNote This is costly: O(n).
     */
    public void sumContributions() {
        contributionTotal = 0;
        for (CureContribution contribution : contributions) {
            contributionTotal += contribution.amount;
        }
    }

    public void contribute(@Nullable UUID uuid, double amount) {
        contributions.add(new CureContribution(uuid, LocalDateTime.now(), amount));
        contributionTotal += amount;
        // TODO: Logging
    }

    public boolean isSatisfied() {
        double fulfilled = 0;
        for (CureContribution contribution : contributions) {
            fulfilled += contribution.amount;
        }
        return fulfilled >= contributionGoal;
    }

    public static class CureContribution { // TODO: Move to having a sum, remove time.
        @Nullable UUID contributor;
        @Nullable LocalDateTime time;
        public double amount;

        public CureContribution(@Nullable UUID uuid, @Nullable LocalDateTime now, double amount) {
            contributor = uuid;
            time = now;
            this.amount = amount;
        }
    }

    public static class Serializer implements JsonSerializer<SpellCure>, JsonDeserializer<SpellCure> {
        @Override
        public SpellCure deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Class cureType = context.deserialize(json.getAsJsonObject().get("cureType"), Class.class);
            return context.deserialize(json, cureType);
        }

        @Override
        public JsonElement serialize(SpellCure src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src, src.getCureType());
        }
    }
}
