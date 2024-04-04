package com.skycat.mystical;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.HashMap;

public class FakeStatusEffect extends StatusEffect {
    protected static final HashMap<Pair<Identifier, StatusEffectCategory>, FakeStatusEffect> INSTANCES = new HashMap<>();
    public static FakeStatusEffect getOrCreate(Identifier spriteId, StatusEffectCategory category) {
        return INSTANCES.computeIfAbsent(new Pair<>(spriteId, category), FakeStatusEffect::new);
    }

    protected final Identifier spriteId;
    protected FakeStatusEffect(Identifier spriteId, StatusEffectCategory category) {
        super(category, 0xad93f3);
        this.spriteId = spriteId;
    }

    protected FakeStatusEffect(Pair<Identifier, StatusEffectCategory> idCategoryPair) {
        this(idCategoryPair.getLeft(), idCategoryPair.getRight());
    }

    public Identifier getSpriteId() {
        return spriteId;
    }


}
