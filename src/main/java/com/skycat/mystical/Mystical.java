package com.skycat.mystical;

import com.skycat.mystical.common.spell.SpellHandler;
import com.skycat.mystical.server.HavenManager;
import com.skycat.mystical.server.MysticalEventHandler;
import com.skycat.mystical.server.SaveState;
import com.skycat.mystical.server.command.MysticalCommandHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.CheckedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

// WARNING: Package structure looks like split-sources structure. It's not. I'm working on it.
public class Mystical implements ModInitializer, ServerWorldEvents.Load {
    public static final Logger LOGGER = LoggerFactory.getLogger("mystical");
    public static final MysticalEventHandler EVENT_HANDLER = new MysticalEventHandler();
    public static final Random RANDOM = new Random();
    public static final net.minecraft.util.math.random.Random MC_RANDOM = new CheckedRandom(RANDOM.nextLong());
    public static final com.skycat.mystical.common.MysticalConfig CONFIG = com.skycat.mystical.common.MysticalConfig.createAndLoad();
    public static final MysticalCommandHandler COMMAND_HANDLER = new MysticalCommandHandler();
    // Using tags like this will make startup time a bit longer, but will allow for compatibility
    public static final TagKey<EntityType<?>> BOSSES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:bosses"));
    public static final TagKey<EntityType<?>> ZOMBIE_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:zombie_variants"));
    public static final TagKey<EntityType<?>> SKELETON_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:skeleton_variants"));
    public static final TagKey<EntityType<?>> ENDERMAN_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:enderman_variants"));
    public static final TagKey<EntityType<?>> EVOKER_SUMMONABLE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:evoker_summonable"));
    public static final TagKey<Block> GLAZED_TERRACOTTA = TagKey.of(RegistryKeys.BLOCK, new Identifier("mystical:glazed_terracotta"));
    public static SaveState save;
    private static boolean isClientWorld = true;

    public static HavenManager getHavenManager() {
        if (save == null) {
            throw new NullPointerException("Cannot get haven manager - save is null");
        }
        return save.getHavenManager();
    }

    public static boolean isClientWorld() {
        return isClientWorld;
    }

    public static void saveUpdated() {
        save.markDirty();
    }

    public static SpellHandler getSpellHandler() {
        if (save == null) {
            throw new NullPointerException("Cannot get spell handler - save is null");
        }
        return save.getSpellHandler();
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(COMMAND_HANDLER);

        ServerWorldEvents.LOAD.register(this);
        ServerLifecycleEvents.SERVER_STOPPING.register(EVENT_HANDLER);
    }

    @Override
    public void onWorldLoad(MinecraftServer server, ServerWorld world) {
        if (!server.getOverworld().equals(world)) return;
        /*assert !world.isClient()*/
        isClientWorld = false;
        EVENT_HANDLER.onWorldLoad(server, world);
        save = SaveState.loadSave(server);

        EntitySleepEvents.START_SLEEPING.register(getSpellHandler());
        EntitySleepEvents.STOP_SLEEPING.register(getSpellHandler());
        PlayerBlockBreakEvents.BEFORE.register(getSpellHandler());
        PlayerBlockBreakEvents.AFTER.register(getSpellHandler());
        ServerPlayerEvents.AFTER_RESPAWN.register(getSpellHandler());
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(getSpellHandler());
        AttackBlockCallback.EVENT.register(getSpellHandler());
        ServerEntityEvents.EQUIPMENT_CHANGE.register(getSpellHandler());
    }
}
