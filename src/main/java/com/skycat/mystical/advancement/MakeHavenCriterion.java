package com.skycat.mystical.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class MakeHavenCriterion extends AbstractCriterion<MakeHavenCriterion.Conditions> {
    public static final Identifier ID = Identifier.of("mystical", "make_haven"); // TODO: Figure out where this goes

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, Optional<LootContextPredicate> playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(playerPredicate);
    }

    public void trigger(ServerPlayerEntity player) {
        super.trigger(player, Conditions::requirementsMet);
    }

    public static class Conditions extends AbstractCriterionConditions {
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        public Conditions(Optional<LootContextPredicate> playerPredicate) {
            super(playerPredicate);
        }

        @SuppressWarnings("SameReturnValue")
        public boolean requirementsMet() {
            return true; // No requirements (player predicate is handled by AbstractCriterion#trigger)
        }
    }
}
