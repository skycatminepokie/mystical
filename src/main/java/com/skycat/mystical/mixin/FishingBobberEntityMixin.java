package com.skycat.mystical.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.FishingRodLaunchConsequence;
import com.skycat.mystical.spell.consequence.FishingRodSwapConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin implements Ownable {

    @WrapOperation(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void mystical_managePullHookedEntitySpells(Entity hooked, Vec3d velocity, Operation<Void> original) {
        Entity owner = getOwner();
        assert owner != null; // Checked by vanilla
        if (Mystical.isClientWorld()) {
            original.call(hooked, velocity);
            return;
        }

        int velocityMultiplier = 1;
        if (!Mystical.getHavenManager().isInHaven(hooked) &&
                Mystical.getSpellHandler().isConsequenceActive(FishingRodLaunchConsequence.class) &&
                Utils.percentChance(Mystical.CONFIG.fishingRodLaunch.chance())) { // fishingRodLaunch
            velocityMultiplier *= Mystical.CONFIG.fishingRodLaunch.multiplier();
            Utils.log(FishingRodLaunchConsequence.FACTORY.getFiredMessage(), Mystical.CONFIG.fishingRodLaunch.logLevel());
        }

        if (!Mystical.getHavenManager().isInHaven(owner) &&
                Mystical.getSpellHandler().isConsequenceActive(FishingRodSwapConsequence.class) &&
                Utils.percentChance(Mystical.CONFIG.fishingRodSwap.chance())) { // fishingRodSwap
            velocityMultiplier *= -1;
            owner.setVelocity(owner.getVelocity().add(velocity.multiply(velocityMultiplier))); // If fishingRodSwap is active, swap who's moving and the direction
            Utils.log(FishingRodSwapConsequence.FACTORY.getFiredMessage(), Mystical.CONFIG.fishingRodSwap.logLevel());
            if (owner instanceof ServerPlayerEntity player) {
                mystical_sendUpdatePacket(player);
            }
        } else {
            original.call(hooked, velocity.multiply(velocityMultiplier)); // Otherwise, don't swap, but apply potential changes from fishingRodLaunch
            if (velocityMultiplier != 1 && hooked instanceof ServerPlayerEntity player) { // If it wasn't vanilla behavior and the hooked entity was a player
                mystical_sendUpdatePacket(player);
            }
        }

    }

    @Unique
    private static void mystical_sendUpdatePacket(ServerPlayerEntity player) {
        player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player)); // Thanks @Wesley1808#9858 :)
    }
}
