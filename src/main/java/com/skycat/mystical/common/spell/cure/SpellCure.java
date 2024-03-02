package com.skycat.mystical.common.spell.cure;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.util.Utils;
import lombok.Getter;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.Nullable;

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
        for (int contrib : contributions.values()) {
            contributionTotal += contrib;
        }
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
        if (uuid == null) {
            uuid = UUID.fromString("09b4c37c-1dd6-4eb4-8adf-f660dd111410"); // This better not collide with anything.
        }
        if (contributions.containsKey(uuid)) {
            contributions.put(uuid, contributions.get(uuid) + amount);
        } else {
            contributions.put(uuid, amount);
        }
        contributionTotal += amount;
        String contributor;
        contributor = uuid.toString();
        Utils.log(Utils.translateString("text.mystical.logging.spellContribution", contributor, amount), Mystical.CONFIG.spellContributionLogLevel());
        Mystical.saveUpdated();
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
            Mystical.getHavenManager().addPower(uuid, (int) Math.min(totalPower * ((double) contributions.get(uuid) / contributionTotal), max));
        }
    }

    public int getContributionsLeft() {
        return getContributionGoal() - getContributionTotal();
    }

}
