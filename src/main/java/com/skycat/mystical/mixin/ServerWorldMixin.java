package com.skycat.mystical.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.spell.consequence.SoundSwapConsequence;
import com.skycat.mystical.util.Utils;
import com.skycat.mystical.MysticalEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.level.ServerWorldProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Shadow @Final private ServerWorldProperties worldProperties;
    @Shadow public abstract @NotNull MinecraftServer getServer();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V", shift = At.Shift.BEFORE))
    private void beforeSkippingNight(CallbackInfo ci) {
        if (worldProperties.getTimeOfDay() % 24000 <= MysticalEventHandler.NIGHT_TIME) { // If the time to do night things hasn't passed, do them now
            Mystical.EVENT_HANDLER.doNighttimeEvents(getServer());
        }
    }

    @ModifyVariable(method = "playSound", at = @At(value = "HEAD"), argsOnly = true, ordinal = 0)
    private RegistryEntry<SoundEvent> changeSound(RegistryEntry<SoundEvent> original, @Local(ordinal = 0) double x, @Local(ordinal = 1) double y, @Local(ordinal = 2) double z) {
        return modifySoundIfRequired(original, x, y, z);
    }

    @ModifyVariable(method = "playSoundFromEntity", at = @At(value = "HEAD"), argsOnly = true, ordinal = 0)
    private RegistryEntry<SoundEvent> changeEntitySound(RegistryEntry<SoundEvent> original, @Local Entity entity) { // get rid of: weather, ui, music, music_disk, ambient
        return modifySoundIfRequired(original, entity.getX(), entity.getY(), entity.getZ());
    }

    @Unique
    private RegistryEntry<SoundEvent> modifySoundIfRequired(RegistryEntry<SoundEvent> original, double x, double y, double z) {
        ArrayList<Spell> matchingSpells = Mystical.getSpellHandler().spellsOfConsequenceType(SoundSwapConsequence.class);
        if (matchingSpells.isEmpty() || Mystical.getHavenManager().isInHaven(new BlockPos((int) x, (int) y, (int) z))) { // If there's no soundSwap spells or we're in a haven
            return original;
        }

        Iterator<SoundSwapConsequence> soundSwappers = matchingSpells.stream().map((spell -> (SoundSwapConsequence) spell.getConsequence())).iterator();
        RegistryEntry<SoundEvent> newSound = original;
        while (soundSwappers.hasNext()) {
            SoundSwapConsequence consequence = soundSwappers.next();
            newSound = consequence.swapSound(newSound); // Chain swaps
        }
        if (newSound != original) {
            Utils.log(SoundSwapConsequence.FACTORY.getFiredMessage(), Mystical.CONFIG.soundSwap.logLevel());
        }

        return newSound;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V", shift = At.Shift.AFTER))
    private void afterSkippingNight(CallbackInfo ci) {
        Mystical.EVENT_HANDLER.setNightTimer();
    }
}
