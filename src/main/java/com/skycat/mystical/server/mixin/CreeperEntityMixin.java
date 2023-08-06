package com.skycat.mystical.server.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.consequence.BigCreeperExplosionConsequence;
import com.skycat.mystical.common.spell.consequence.NoFuseConsequence;
import com.skycat.mystical.common.spell.consequence.RandomCreeperEffectCloudsConsequence;
import com.skycat.mystical.common.util.Utils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.LinkedList;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {
    @Shadow
    private int explosionRadius;

    @Shadow private int fuseTime;

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/mob/CreeperEntity;fuseTime:I", opcode = Opcodes.GETFIELD))
    private int changeFuseTime(CreeperEntity instance) {
        if (!Mystical.getHavenManager().isInHaven(instance) && Mystical.getSpellHandler().isConsequenceActive(NoFuseConsequence.class)) {
            return 1;
        }
        return fuseTime;
    }

    @ModifyVariable(method = "spawnEffectsCloud", at = @At("STORE"), index = 1)
    private Collection<StatusEffectInstance> makeEffectsCloud(Collection<StatusEffectInstance> oldEffects) {
        if (Mystical.getSpellHandler().isConsequenceActive(RandomCreeperEffectCloudsConsequence.class)) { // TODO: Config (chance, allowed effects)
            LinkedList<StatusEffectInstance> newStatusEffects = new LinkedList<>(oldEffects);
            newStatusEffects.add(new StatusEffectInstance(Utils.getRandomStatusEffect(), Mystical.CONFIG.randomCreeperEffectClouds.effectDuration() * 20, Mystical.CONFIG.randomCreeperEffectClouds.effectAmplifier()));
            return newStatusEffects;
        }
        return oldEffects;
    }

    @Redirect(method = "explode", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/mob/CreeperEntity;explosionRadius:I", opcode = Opcodes.GETFIELD))
    int modifyExplosionRadius(CreeperEntity instance){
        if (Mystical.getSpellHandler().isConsequenceActive(BigCreeperExplosionConsequence.class) &&
                Utils.percentChance(Mystical.CONFIG.bigCreeperExplosion.chance()) &&
                !Mystical.getHavenManager().isInHaven(instance)
        ) {
            Utils.log(Utils.translateString("text.mystical.consequence.bigCreeperExplosion.fired"), Mystical.CONFIG.bigCreeperExplosion.logLevel());
            return (int) (explosionRadius * Mystical.CONFIG.bigCreeperExplosion.multiplier());
        }
        return explosionRadius;
    }

}
