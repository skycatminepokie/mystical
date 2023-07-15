package com.skycat.mystical.common.spell.cure;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.util.Utils;
import lombok.Getter;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class SpellCure {
    @Getter protected int contributionGoal;
    @Getter protected final Class cureType;
    @Getter public final CureType cureTypeId;
    @Getter private int contributionTotal = 0;
    public static final Codec<SpellCure> CODEC = CureTypes.TYPE_CODEC.dispatch("cureTypeID", SpellCure::getCureTypeId, CureType::getCodec);

    /**
     * Make sure to update {@link #contributionTotal} when adding to or removing from this
     */
    private final HashMap<UUID, Integer> contributions;

    public Map<UUID, Integer> getContributionCopy() {
        return Map.copyOf(contributions);
    }

    public SpellCure(int contributionGoal, Class cureType, CureType cureTypeId) {
        this(contributionGoal, cureType, cureTypeId, new HashMap<>());
    }

    public SpellCure(int contributionGoal, Class cureType, CureType cureTypeId, HashMap<UUID, Integer> contributions) {
        this.contributionGoal = contributionGoal;
        this.cureType = cureType;
        this.cureTypeId = cureTypeId;
        this.contributions = contributions;
    }

    /**
     * Get a player-readable description/"recipe" for the cure.
     */
    public abstract MutableText getDescription();

    /**
     * Recalculates the total value of all contributions.
     * Use {@link #getContributionTotal()} to get the total.
     *
     * @implNote This is costly: O(n).
     */
    public void sumContributions() {
        contributionTotal = 0;
        for (Integer amount : contributions.values()) {
            contributionTotal += amount;
        }
    }

    public void contribute(@Nullable UUID uuid, int amount) {
        if (contributions.containsKey(uuid)) {
            contributions.put(uuid, contributions.get(uuid) + amount);
        } else {
            contributions.put(uuid, amount);
        }
        contributionTotal += amount;
        String contributor;
        if (uuid != null) {
            contributor = uuid.toString();
        } else {
            contributor = "unknown";
        }
        Utils.log(Utils.translateString("text.mystical.logging.spellContribution", contributor, amount), Mystical.CONFIG.spellContributionLogLevel());
    }

    public boolean isSatisfied() {
        return contributionTotal >= contributionGoal;
    }

    /**
     * Award power to players based on their contributions.
     * @param totalPower The total amount of power to distribute among the players
     * @implNote Simple overload of {@link #awardPower(int, int)}.
     */
    public void awardPower(int totalPower) {
        awardPower(totalPower, Integer.MAX_VALUE);
    }

    /**
     * Award power to players based on their contributions.
     * @param totalPower The total power to distribute among contributors.
     * @param max The maximum power to give to any one contributor.
     */
    public void awardPower(int totalPower, int max) {
        for (UUID uuid : contributions.keySet()) {
            if (contributions.get(uuid) <= 0) continue;
            // Formula: min(totalPower * percentContributed, max)
            Mystical.HAVEN_MANAGER.addPower(uuid, (int) Math.min(totalPower * ((double) contributions.get(uuid) / contributionTotal), max));
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
