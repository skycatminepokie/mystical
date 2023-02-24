package skycat.mystical.mixin;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skycat.mystical.Mystical;

import java.util.ArrayList;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin { // I could probably turn this into an event
    private static final ArrayList<CatVariant> VARIANTS = new ArrayList<>();
    @Shadow public abstract void setVariant(CatVariant variant);

    @Inject(method = "eat", at = @At("HEAD"))
    public void eat(PlayerEntity entity, Hand hand, ItemStack stack, CallbackInfo ci) {
        if (VARIANTS.isEmpty()) { // TODO Should this be in static? Seems like delaying initialization might be good though.
            for (CatVariant catVariant : Registry.CAT_VARIANT) {
                VARIANTS.add(catVariant);
            }
        }
        if (Mystical.SPELL_HANDLER.shouldChangeCatVariant()) {
            setVariant(Util.getRandom(VARIANTS, Mystical.MC_RANDOM));
        }
    }
}
