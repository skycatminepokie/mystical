package com.skycat.mystical.advancement;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class MakeHavenCriterion extends AbstractCriterion<MakeHavenCriterion.Conditions> {
    public static final @NotNull Identifier ID = Objects.requireNonNull(Identifier.of("mystical", "make_haven"));

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player) {
        super.trigger(player, com.skycat.mystical.advancement.MakeHavenCriterion.Conditions::requirementsMet);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static class Conditions implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = Codecs.createStrictOptionalFieldCodec(LootContextPredicate.CODEC, "player").xmap(Conditions::new, Conditions::player).codec();
         protected Optional<LootContextPredicate> playerPredicate;
        public Conditions(Optional<LootContextPredicate> playerPredicate) {
            this.playerPredicate = playerPredicate;
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        @SuppressWarnings("SameReturnValue")
        public boolean requirementsMet() {
            return true; // No requirements (player predicate is handled by AbstractCriterion#trigger)
        }
    }
}
