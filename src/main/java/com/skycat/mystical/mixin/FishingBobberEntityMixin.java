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
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin implements Ownable {

    @Shadow
    @Nullable
    public abstract Entity getHookedEntity();

    @Inject(method = "pullHookedEntity", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void mystical_updateForLaunchSpell(Entity entity, CallbackInfo ci, Entity entity2, Vec3d vec3d) {
        if (!Mystical.isClientWorld() &&
                !Mystical.getHavenManager().isInHaven(entity) &&
                !Mystical.getHavenManager().isInHaven(entity2) &&
                entity instanceof ServerPlayerEntity &&
                Mystical.getSpellHandler().isConsequenceActive(FishingRodLaunchConsequence.class)) {
            ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity)); // Thanks @Wesley1808#9858 :)
        }
    }

    @ModifyArg(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), index = 0)
    public Vec3d mystical_handleLaunchSpell(Vec3d velocity) {
        Entity hookedEntity = getHookedEntity();
        if (hookedEntity != null &&
                !Mystical.isClientWorld() &&
                !Mystical.getHavenManager().isInHaven(hookedEntity) &&
                Mystical.getSpellHandler().isConsequenceActive(FishingRodLaunchConsequence.class) &&
                Utils.percentChance(Mystical.CONFIG.fishingRodLaunch.chance())) {
            Utils.log(Utils.translateString("text.mystical.consequence.fishingRodLaunch.fired"), Mystical.CONFIG.fishingRodLaunch.logLevel());
            return velocity.multiply(Mystical.CONFIG.fishingRodLaunch.multiplier());
        }
        return velocity;
    }

    @WrapOperation(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void mystical_swapPulledEntity(Entity hooked, Vec3d velocity, Operation<Void> original) {
        Entity owner = getOwner();
        assert owner != null; // Checked by vanilla
        if (Mystical.isClientWorld() ||
                !Utils.percentChance(Mystical.CONFIG.fishingRodSwap.chance()) ||
                Mystical.getHavenManager().isInHaven(owner) ||
                !Mystical.getSpellHandler().isConsequenceActive(FishingRodSwapConsequence.class)) {
            original.call(hooked, velocity);
            return;
        }

        owner.setVelocity(owner.getVelocity().add(velocity.multiply(-1)));
        if (owner instanceof ServerPlayerEntity player) {
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player)); // Thanks @Wesley1808#9858 :)
        }
    }
}
