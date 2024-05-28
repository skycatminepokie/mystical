package com.skycat.mystical.mixin.client;

import net.minecraft.client.texture.StatusEffectSpriteManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StatusEffectSpriteManager.class)
public abstract class StatusEffectSpriteManagerMixin {
    //@WrapOperation(method = "getSprite(Lnet/minecraft/entity/effect/StatusEffect;)Lnet/minecraft/client/texture/Sprite;", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"))
    //public Identifier mystical_modifyId(Registry<StatusEffect> registry, Object lookupKey, Operation<@Nullable Identifier> original) { // Can't ModifyArg, we need to stop it from executing so that it doesn't fail.
    //    if (lookupKey instanceof SpellStatusEffect effect) {
    //        return effect.getSpriteId();
    //    } else {
    //        return original.call(registry, lookupKey);
    //    }
    //}

}
