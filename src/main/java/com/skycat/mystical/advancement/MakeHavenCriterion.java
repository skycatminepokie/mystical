package com.skycat.mystical.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class MakeHavenCriterion extends AbstractCriterion<MakeHavenCriterion.Conditions> {
    public static final @NotNull Identifier ID = Objects.requireNonNull(Identifier.of("mystical", "make_haven")); // TODO: Figure out where this goes

    @Override
    protected com.skycat.mystical.advancement.MakeHavenCriterion.Conditions conditionsFromJson(JsonObject obj, Optional<LootContextPredicate> playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new com.skycat.mystical.advancement.MakeHavenCriterion.Conditions(playerPredicate);
    }

    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player) {
        super.trigger(player, com.skycat.mystical.advancement.MakeHavenCriterion.Conditions::requirementsMet);
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
