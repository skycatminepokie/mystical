package com.skycat.mystical.common.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.util.Identifier;

public class ContributeToCureCriterion extends AbstractCriterion<ContributeToCureCriterion.Conditions> {
    public static final Identifier ID = Identifier.of("mystical", "contribute_to_cure");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return null; // TODO
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(Identifier id, LootContextPredicate entity) {
            super(id, entity);
        }
    }
}
