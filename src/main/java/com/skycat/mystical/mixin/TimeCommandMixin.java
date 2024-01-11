package com.skycat.mystical.mixin;

import com.skycat.mystical.Mystical;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TimeCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TimeCommand.class)
public abstract class TimeCommandMixin {
    @Inject(method = "executeSet", at = @At("HEAD"))
    private static void onTimeSetCommand(ServerCommandSource source, int time, CallbackInfoReturnable<Integer> cir) {
        Mystical.EVENT_HANDLER.setNightTimer();
    }

    @Inject(method = "executeAdd", at = @At("HEAD"))
    private static void onTimeAddCommand(ServerCommandSource source, int time, CallbackInfoReturnable<Integer> cir) {
        Mystical.EVENT_HANDLER.setNightTimer();
    }
}
