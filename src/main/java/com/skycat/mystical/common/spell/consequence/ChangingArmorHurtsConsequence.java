package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.common.util.Utils;
import lombok.NonNull;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static net.minecraft.entity.damage.DamageTypes.THORNS;

public class ChangingArmorHurtsConsequence extends SpellConsequence implements ServerEntityEvents.EquipmentChange {
    public static final Factory FACTORY = new Factory();

    public ChangingArmorHurtsConsequence() {
        super(ChangingArmorHurtsConsequence.class, ServerEntityEvents.EquipmentChange.class, 10D);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    @Override
    public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        if (livingEntity instanceof ServerPlayerEntity Player && Utils.percentChance(10d) && equipmentSlot.isArmorSlot()) {
            Player.damage(Player.getServerWorld().getDamageSources().create(THORNS), 1);
        }
    }

    public static class Factory extends ConsequenceFactory<ChangingArmorHurtsConsequence> {
        protected Factory() {
            super("reverse_thorns",
                    "Armor Hurts You",
                    "Armor Fighting Back!",
                    "Armor Changed",
                    ChangingArmorHurtsConsequence.class,
                    Codec.unit(ChangingArmorHurtsConsequence::new));
        }

        @Override
        public double getWeight() {
            return 0;
        }

        @Override
        public @NotNull ChangingArmorHurtsConsequence make(@NonNull Random random, double points) {
            return new ChangingArmorHurtsConsequence();
        }
    }


}
