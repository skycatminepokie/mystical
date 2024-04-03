package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.BigCreeperExplosionConsequence;
import com.skycat.mystical.spell.consequence.NoFuseConsequence;
import com.skycat.mystical.spell.consequence.RandomCreeperEffectCloudsConsequence;
import com.skycat.mystical.util.Utils;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.CreeperEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.LinkedList;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin {
    @Shadow private int explosionRadius;

    @Shadow private int fuseTime;

    @Redirect(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/mob/CreeperEntity;fuseTime:I", opcode = Opcodes.GETFIELD)) // TODO: Don't redirect
    private int mystical_changeFuseTime(CreeperEntity instance) {
        if (!Mystical.isClientWorld() &&
                !Mystical.getHavenManager().isInHaven(instance) &&
                Mystical.getSpellHandler().isConsequenceActive(NoFuseConsequence.class)) {
            return 1;
        }
        return fuseTime;
    }

    @ModifyVariable(method = "spawnEffectsCloud", at = @At("STORE"), index = 1)
    private Collection<StatusEffectInstance> mystical_extraEffectsCloud(Collection<StatusEffectInstance> oldEffects) {
        if (!Mystical.isClientWorld() &&
                Mystical.getSpellHandler().isConsequenceActive(RandomCreeperEffectCloudsConsequence.class)) { // TODO: Config (chance, allowed effects)
            LinkedList<StatusEffectInstance> newStatusEffects = new LinkedList<>(oldEffects);
            newStatusEffects.add(new StatusEffectInstance(Utils.getRandomStatusEffect(), Mystical.CONFIG.randomCreeperEffectClouds.effectDuration() *
                    20, Mystical.CONFIG.randomCreeperEffectClouds.effectAmplifier()));
            return newStatusEffects;
        }
        return oldEffects;
    }

    @Redirect(method = "explode", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/mob/CreeperEntity;explosionRadius:I", opcode = Opcodes.GETFIELD))
    int mystical_modifyExplosionRadius(CreeperEntity instance) {
        if (!Mystical.isClientWorld() &&
                Mystical.getSpellHandler().isConsequenceActive(BigCreeperExplosionConsequence.class) &&
                !Mystical.getHavenManager().isInHaven(instance) &&
                Utils.percentChance(Mystical.CONFIG.bigCreeperExplosion.chance())) {
            Utils.log(Utils.translateString("text.mystical.consequence.bigCreeperExplosion.fired"), Mystical.CONFIG.bigCreeperExplosion.logLevel());
            return (int) (explosionRadius * Mystical.CONFIG.bigCreeperExplosion.multiplier());
        }
        return explosionRadius;
    }

}
