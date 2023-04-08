package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.FishingRodLaunchConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {
    @ModifyArg(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), index = 0)
    public Vec3d onSetVelocity(Vec3d velocity) {
        if (Mystical.SPELL_HANDLER.isConsequenceActive(FishingRodLaunchConsequence.class)) {
            Utils.log(Utils.translateString("text.mystical.consequence.fishingRodLaunch.fired"), Mystical.CONFIG.fishingRodLaunch.logLevel()); // TODO: Translate
            return velocity.multiply(20.0); // TODO: config
        }
        return velocity;
    }

    @Inject(method = "pullHookedEntity", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void afterPullHooked(Entity entity, CallbackInfo ci, Entity entity2, Vec3d vec3d) {
        if (Mystical.SPELL_HANDLER.isConsequenceActive(FishingRodLaunchConsequence.class) && entity instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity));
        }
    }


}