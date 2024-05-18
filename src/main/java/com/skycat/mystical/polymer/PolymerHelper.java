package com.skycat.mystical.polymer;

import com.skycat.mystical.spell.Spells;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;

public class PolymerHelper {
    protected static final HashSet<RegistryEntry<ConsequenceStatusEffect>> HANDLED_STATUS_EFFECTS = new HashSet<>();
    protected static ServerLoginNetworkHandler LOGIN_HANDLER;
    public static void init() {
        for (ConsequenceFactory<?> consequenceFactory : Spells.getConsequenceFactories()) {
            Registry.register(Registries.STATUS_EFFECT, consequenceFactory.getId(), new ConsequenceStatusEffect(consequenceFactory, 0x000000));
        }
        MysticalXPolymerNetworking.init();

    }

    public static void updateStatusEffects(MinecraftServer server) {
        // TODO
    }

    public static void updateStatusEffects(ServerPlayerEntity player) {
        // TODO
    }

}
