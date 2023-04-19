package com.skycat.mystical.common.spell.cure;

import com.google.gson.*;
import com.skycat.mystical.common.util.Utils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
public abstract class SpellCure {

    protected int contributionGoal;
    protected final Class cureType;
    protected ArrayList<CureContribution> contributions = new ArrayList<>();
    protected final String translationKey;

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
     * Finds the total value of all contributions
     *
     * @return the total value of all contributions
     * @implNote This is costly: O(n).
     */
    public int sumContributions() { // TODO: Migrate to O(1) method by caching
        int sum = 0;
        for (CureContribution contribution : contributions) {
            sum += contribution.amount;
        }
        return sum;
    }

    public void contribute(@Nullable UUID uuid, double amount) {
        contributions.add(new CureContribution(uuid, LocalDateTime.now(), amount));
        // TODO: Logging
    }

    public boolean isSatisfied() {
        double fulfilled = 0;
        for (CureContribution contribution : contributions) {
            fulfilled += contribution.amount;
        }
        return fulfilled >= contributionGoal;
    }

    public static class CureContribution {
        @Nullable UUID contributor;
        @Nullable LocalDateTime time;
        double amount;

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
