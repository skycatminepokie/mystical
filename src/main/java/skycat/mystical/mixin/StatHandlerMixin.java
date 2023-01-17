package skycat.mystical.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skycat.mystical.Mystical;

@Mixin(StatHandler.class)
public class StatHandlerMixin {
    @Inject(method = "increaseStat", at = @At("HEAD"))
    public void statIncreased(PlayerEntity player, Stat<?> stat, int value, CallbackInfo ci) {
        Mystical.SPELL_HANDLER.onStatIncreased(player, stat, value);
    }
}
