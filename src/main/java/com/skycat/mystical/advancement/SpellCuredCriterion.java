package com.skycat.mystical.advancement;

import com.google.gson.JsonObject;
import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.spell.cure.SpellCure;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SpellCuredCriterion extends AbstractCriterion<SpellCuredCriterion.Conditions> {
    public static final Identifier ID = Identifier.of("mystical", "spell_cured");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        NumberRange.FloatRange contributionPercentage = NumberRange.FloatRange.fromJson(obj.get("contributionPercentage")); // Ends up with NumberRange.FloatRange.ANY if obj.get(...) returns null.
        NumberRange.IntRange participants = NumberRange.IntRange.fromJson(obj.get("participants")); // Ends up with NumberRange.IntRange.ANY if obj.get(...) returns null.
        return new Conditions(playerPredicate, contributionPercentage, participants);
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, Spell spell) {
        super.trigger(player, conditions -> conditions.requirementsMet(player, spell));
    }

    public static class Conditions extends AbstractCriterionConditions { // TODO: Turn this into a full-fledged matching system?
        protected final NumberRange.FloatRange contributionPercentage;
        protected final NumberRange.IntRange participants;

        public Conditions(LootContextPredicate entity, NumberRange.FloatRange contributionPercentage) {
            this(entity, contributionPercentage, NumberRange.IntRange.ANY);
        }

        public Conditions(LootContextPredicate entity, NumberRange.FloatRange contributionPercentage, NumberRange.IntRange participants) {
            super(ID, entity);
            this.contributionPercentage = contributionPercentage;
            this.participants = participants;
        }

        public boolean requirementsMet(ServerPlayerEntity player, Spell spell) {
            SpellCure cure = spell.getCure();
            return contributionPercentage.test(((double) cure.getContributionsOf(player.getUuid())/cure.getContributionGoal()) * 100) &&
                    participants.test(cure.getContributorCount());
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject json = super.toJson(predicateSerializer);
            json.add("contributionPercentage", contributionPercentage.toJson());
            json.add("participants", participants.toJson());
            return json;
        }
    }
}
