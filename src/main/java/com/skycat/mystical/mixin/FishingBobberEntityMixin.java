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
        if (!Mystical.getHavenManager().isInHaven(owner) &&
                Mystical.getSpellHandler().isConsequenceActive(FishingRodSwapConsequence.class) &&
                Utils.percentChance(Mystical.CONFIG.fishingRodSwap.chance())) {
            velocityMultiplier *= -1; // fishingRodSwap
        }

        if (!Mystical.getHavenManager().isInHaven(hooked) &&
        Mystical.getSpellHandler().isConsequenceActive(FishingRodLaunchConsequence.class) &&
        Utils.percentChance(Mystical.CONFIG.fishingRodLaunch.chance())) {
            velocityMultiplier *= Mystical.CONFIG.fishingRodLaunch.multiplier();
        }

        if (velocityMultiplier != 1) { // If neither consequence activated, might as well keep compatibility in mind.
            original.call(hooked, velocity);
            return;
        }

        owner.setVelocity(owner.getVelocity().add(velocity.multiply(velocityMultiplier)));

        if (owner instanceof ServerPlayerEntity player) {
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player)); // Thanks @Wesley1808#9858 :)
        }
    }
}
