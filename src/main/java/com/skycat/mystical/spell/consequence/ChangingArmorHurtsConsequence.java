package com.skycat.mystical.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.util.Utils;
import lombok.NonNull;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static net.minecraft.entity.damage.DamageTypes.THORNS;

public class ChangingArmorHurtsConsequence extends SpellConsequence implements ServerEntityEvents.EquipmentChange { // TODO: Tests
    public static final Factory FACTORY = new Factory();

    public ChangingArmorHurtsConsequence() {
        super(ChangingArmorHurtsConsequence.class, ServerEntityEvents.EquipmentChange.class, 75);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    @Override
    public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        if (livingEntity instanceof ServerPlayerEntity Player && Utils.percentChance(Mystical.CONFIG.changingArmorHurts.chance()) && equipmentSlot.isArmorSlot()) {
            Player.damage(Player.getServerWorld().getDamageSources().create(THORNS), 1);
            Utils.log(Utils.translateString(FACTORY.getDescriptionKey()), Mystical.CONFIG.changingArmorHurts.logLevel());
        }
    }

    public static class Factory extends ConsequenceFactory<ChangingArmorHurtsConsequence> {
        protected Factory() {
            super("changingArmorHurts",
                    "Changing Armor Hurts",
                    "Reverse thorns",
                    "Armor changed, applying damage",
                    ChangingArmorHurtsConsequence.class,
                    Codec.unit(ChangingArmorHurtsConsequence::new));
        }

        @Override
        public @NotNull ChangingArmorHurtsConsequence make(@NonNull Random random, double points) {
            return new ChangingArmorHurtsConsequence();
        }


        @Override
        public double getWeight() {
            return Mystical.CONFIG.changingArmorHurts.enabled() ? Mystical.CONFIG.changingArmorHurts.weight() : 0;
        }
    }
}
