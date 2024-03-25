package com.skycat.mystical.common.advancement;

import com.google.gson.JsonObject;
import com.skycat.mystical.common.spell.Spell;
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
        NumberRange.FloatRange contributionPercentage = NumberRange.FloatRange.fromJson(obj.get("contributionPercentage"));
        return new Conditions(playerPredicate, contributionPercentage);
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, Spell spell) {
        super.trigger(player, conditions -> conditions.requirementsMet(player, spell));
    }

    public static class Conditions extends AbstractCriterionConditions { // TODO: Turn this into a full-fledged matching system?
        // protected Pattern consequencePattern;
        // protected int goal;
        // protected int contributionTotal;
        protected NumberRange.FloatRange contributionPercentage;
        public Conditions(LootContextPredicate entity, NumberRange.FloatRange contributionPercentage) {
            super(ID, entity);
            this.contributionPercentage = contributionPercentage;
        }

        public boolean requirementsMet(ServerPlayerEntity player, Spell spell) {
            return contributionPercentage.test(spell.getCure().getContributionsOf(player.getUuid()));
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject json = super.toJson(predicateSerializer);
            json.add("contributionPercentage", contributionPercentage.toJson());
            return json;
        }
    }
}
