package com.skycat.mystical.common.spell.consequence;

import com.mojang.serialization.Codec;
import com.skycat.mystical.common.util.Utils;
import io.wispforest.owo.offline.DataSavedEvents;
import lombok.NonNull;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import net.minecraft.entity.damage.DamageSources;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.parser.Entity;
import java.util.Random;
import static net.minecraft.entity.damage.DamageTypes.THORNS;

public class ChangeArmorHurtConsequence extends SpellConsequence implements ServerEntityEvents.EquipmentChange {
    public static final Factory FACTORY = new Factory();
    public ChangeArmorHurtConsequence() {
        super(ChangeArmorHurtConsequence.class, ServerEntityEvents.EquipmentChange.class, 10D);
    }

    @Override
    public @NonNull ConsequenceFactory<? extends SpellConsequence> getFactory() {
        return FACTORY;
    }

    @Override
    public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        if(livingEntity instanceof ServerPlayerEntity Player && Utils.percentChance(10d))  {
            Player.damage(Player.getServerWorld().getDamageSources().create(THORNS), 1);
        }


    }
    public static class Factory extends ConsequenceFactory<ChangeArmorHurtConsequence> {
        protected Factory() {
            super("reverse_thorns",
                    "Armor Hurts You",
                    "Armor Fighting Back!",
                    "Armor Changed",
                    ChangeArmorHurtConsequence.class,
                    Codec.unit(ChangeArmorHurtConsequence::new));
        }

        @Override
        public @NotNull ChangeArmorHurtConsequence make(@NonNull Random random, double points) {
            return new ChangeArmorHurtConsequence();
        }

        @Override
        public double getWeight() {
            return 0;
        }
    }


}
