package com.skycat.mystical.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MakeHavenCriterion extends AbstractCriterion<MakeHavenCriterion.Conditions> {
    public static final Identifier ID = Identifier.of("mystical", "make_haven");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(playerPredicate);
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player) {
        super.trigger(player, Conditions::requirementsMet);
    }

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions(LootContextPredicate playerPredicate) {
            super(ID, playerPredicate);
        }

        @SuppressWarnings("SameReturnValue")
        public boolean requirementsMet() {
            return true; // No requirements (player predicate is handled by AbstractCriterion#trigger)
        }
    }
}
