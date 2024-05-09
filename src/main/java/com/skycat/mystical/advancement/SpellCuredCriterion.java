package com.skycat.mystical.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.spell.cure.SpellCure;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class SpellCuredCriterion extends AbstractCriterion<SpellCuredCriterion.Conditions> {
    public static final @NotNull Identifier ID = Objects.requireNonNull(Identifier.of("mystical", "spell_cured")); // TODO: Figure out where this goes

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, Spell spell) {
        super.trigger(player, conditions -> conditions.requirementsMet(player, spell));
    }

    public static class Conditions implements AbstractCriterion.Conditions { // TODO: Turn this into a full-fledged matching system?
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> (instance.group(
                LootContextPredicate.CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                NumberRange.DoubleRange.CODEC.optionalFieldOf("contributionPercentage").forGetter(Conditions::getContributionPercentage),
                NumberRange.IntRange.CODEC.optionalFieldOf("participants").forGetter(Conditions::getParticipants)
        ).apply(instance, Conditions::new)));
        protected final @Nullable NumberRange.DoubleRange contributionPercentage;
        protected final @Nullable NumberRange.IntRange participants;
        protected final @Nullable LootContextPredicate player;
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        public Conditions(Optional<LootContextPredicate> player, Optional<NumberRange.DoubleRange> contributionPercentage, Optional<NumberRange.IntRange> participants) {
            this.contributionPercentage = contributionPercentage.orElse(null);
            this.participants = participants.orElse(null);
            this.player = player.orElse(null);
        }
        public Conditions(@Nullable LootContextPredicate player, @Nullable NumberRange.DoubleRange contributionPercentage, @Nullable NumberRange.IntRange participants) {
            this.contributionPercentage = contributionPercentage;
            this.participants = participants;
            this.player = player;
        }

        public Optional<NumberRange.DoubleRange> getContributionPercentage() {
            return Optional.ofNullable(contributionPercentage);
        }

        public Optional<NumberRange.IntRange> getParticipants() {
            return Optional.ofNullable(participants);
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.ofNullable(player);
        }

        public boolean requirementsMet(ServerPlayerEntity player, Spell spell) {
            SpellCure cure = spell.getCure();
            // Return true iff (participants is null OR succeeds) AND (contributionPercentage is null OR succeeds). In other words, a null requirement always succeeds.
            return (participants == null || participants.test(cure.getContributorCount())) &&
                    (contributionPercentage == null || contributionPercentage.test(((double) cure.getContributionsOf(player.getUuid()) / cure.getContributionGoal()) * 100));

        }

    }
}
