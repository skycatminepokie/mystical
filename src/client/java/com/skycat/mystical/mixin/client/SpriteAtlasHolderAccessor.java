package com.skycat.mystical.mixin.client;


import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpriteAtlasHolder.class)
public interface SpriteAtlasHolderAccessor {
    @Invoker("getSprite")
    Sprite mystical_invokeGetSprite(Identifier id);
}