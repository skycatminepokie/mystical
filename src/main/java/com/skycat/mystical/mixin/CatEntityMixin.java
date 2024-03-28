package com.skycat.mystical.mixin;

import com.skycat.mystical.util.event.CatEntityEvents;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin {
    /**
     * Fire the {@link CatEntityEvents.Eat} event.
     */
    @Inject(method = "eat", at = @At("HEAD"))
    public void mystical_eat(PlayerEntity entity, Hand hand, ItemStack stack, CallbackInfo ci) {
        CatEntityEvents.EAT.invoker().onEat((CatEntity) (Object) this, entity, hand, stack);
    }
}
