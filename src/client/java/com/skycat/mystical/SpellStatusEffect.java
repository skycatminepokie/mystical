package com.skycat.mystical;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.HashMap;

/**
 * A "fake" status effect that presents spells only on the client side.
 */
public class SpellStatusEffect extends StatusEffect {
    protected static final HashMap<Pair<Identifier, StatusEffectCategory>, SpellStatusEffect> INSTANCES = new HashMap<>();
    public static SpellStatusEffect getOrCreate(Identifier spriteId, StatusEffectCategory category) {
        return INSTANCES.computeIfAbsent(new Pair<>(spriteId, category), SpellStatusEffect::new);
    }

    protected final Identifier spriteId;
    protected SpellStatusEffect(Identifier spriteId, StatusEffectCategory category) {
        super(category, 0x000000);
        this.spriteId = spriteId;
    }

    protected SpellStatusEffect(Pair<Identifier, StatusEffectCategory> idCategoryPair) {
        this(idCategoryPair.getLeft(), idCategoryPair.getRight());
    }

    public Identifier getSpriteId() {
        return spriteId;
    }


}
