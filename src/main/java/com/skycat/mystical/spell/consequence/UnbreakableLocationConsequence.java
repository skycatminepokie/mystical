package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.MysticalCriteria;
import com.skycat.mystical.util.Utils;
import lombok.Getter;
import lombok.NonNull;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class UnbreakableLocationConsequence extends SpellConsequence implements AttackBlockCallback { // TODO: Tests
    public static final Factory FACTORY = new Factory();
    @Getter private final long seed;
    /**
     * The seed will be set every time we pull from this. That needs to be done anyway, so we have a single object to do it.
     */
    private static final Random RANDOM = new Random();

    public UnbreakableLocationConsequence() {
        this(Mystical.RANDOM.nextLong());
    }

    public UnbreakableLocationConsequence(long seed) {
        super(UnbreakableLocationConsequence.class, AttackBlockCallback.class, 200);
        this.seed = seed;
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) { // The actual spell part
        if (world.isClient() || Mystical.getHavenManager().isInHaven(pos)) return ActionResult.PASS;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        RANDOM.setSeed(seed * pos.hashCode());
        boolean preventBreaking = Utils.percentChance(Mystical.CONFIG.unbreakableLocation.chance(), RANDOM);
        if (preventBreaking) {
            Utils.sendMessageToPlayer(serverPlayer, Utils.translatable("text.mystical.consequence.unbreakableLocation.noBreaking"), true);
            MysticalCriteria.PREVENTED_BREAKING_CRITERION.trigger(serverPlayer, pos, serverPlayer.getStackInHand(hand));
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static class Factory extends ConsequenceFactory<UnbreakableLocationConsequence> {
        protected Factory() {
            super("unbreakableLocation",
                    "Unbreakable Block (location)",
                    "WorldGuard but awful",
                    "Prevented a block from being broken.",
                    UnbreakableLocationConsequence.class,
                    Codec.LONG.xmap(UnbreakableLocationConsequence::new, UnbreakableLocationConsequence::getSeed));
        }

        @Override
        public @NotNull UnbreakableLocationConsequence make(@NonNull Random random, double points) {
            return new UnbreakableLocationConsequence();
        }


        @Override
        public double getWeight() {
            return Mystical.CONFIG.unbreakableLocation.enabled() ? Mystical.CONFIG.unbreakableLocation.weight() : 0;
        }
    }
}
