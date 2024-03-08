package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.DisableDaylightBurningConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements EntityLike {
    @Shadow @Final protected GoalSelector targetSelector;

    @Inject(method = "isAffectedByDaylight", at = @At("HEAD"), cancellable = true)
    private void cancelDaylightEffects(CallbackInfoReturnable<Boolean> cir) {
        if (!Mystical.isClientWorld() &&
                Mystical.getSpellHandler().isConsequenceActive(DisableDaylightBurningConsequence.class) &&
                !Mystical.getHavenManager().isInHaven(getBlockPos())) {
            Utils.log(Utils.translateString(DisableDaylightBurningConsequence.FACTORY.getDescriptionKey()), Mystical.CONFIG.disableDaylightBurning.logLevel());
            cir.setReturnValue(false);
        }
    }

    @ModifyArg(method = "playAmbientSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
    private SoundEvent changeAmbientSound(SoundEvent par1) {
        // Idea: Spawn the other entity just to get the sound, then cache it. Or maybe have one of my own hanging around but not spawned? And modify receiver?
        return Registries.SOUND_EVENT.get(Registries.SOUND_EVENT.getRawId(par1) + 10);
        // return Utils.getRandomRegistryEntry(Registries.SOUND_EVENT);
    }
}
