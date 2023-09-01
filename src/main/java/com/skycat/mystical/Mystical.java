package com.skycat.mystical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import com.skycat.mystical.common.spell.SpellHandler;
import com.skycat.mystical.common.spell.consequence.SpellConsequence;
import com.skycat.mystical.common.spell.cure.SpellCure;
import com.skycat.mystical.common.util.*;
import com.skycat.mystical.server.HavenManager;
import com.skycat.mystical.server.MysticalEventHandler;
import com.skycat.mystical.server.SaveState;
import com.skycat.mystical.server.command.MysticalCommandHandler;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.CheckedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Getter
public class Mystical implements ModInitializer, ServerLifecycleEvents.ServerStarted {
    @Getter public static final Logger LOGGER = LoggerFactory.getLogger("mystical");
    @Getter public static final Gson GSON = new GsonBuilder()
            .setVersion(0.1)
            .registerTypeAdapter(Block.class, new BlockSerializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
            .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            .registerTypeAdapter(Identifier.class, new Identifier.Serializer())
            .registerTypeAdapter(SpellCure.class, new SpellCure.Serializer())
            .registerTypeAdapter(SpellConsequence.class, new SpellConsequence.Serializer())
            .registerTypeAdapter(Class.class, new ClassSerializer())
            .registerTypeAdapter(Stat.class, new StatCodec())
            .registerTypeAdapter(EntityType.class, Utils.serializerOf(Registries.ENTITY_TYPE.getCodec()))
            .registerTypeAdapter(EntityType.class, Utils.deserializerOf(Registries.ENTITY_TYPE.getCodec()))
            .create();
    @Getter public static final MysticalEventHandler EVENT_HANDLER = new MysticalEventHandler();
    @Getter public static final Random RANDOM = new Random();
    @Getter public static final net.minecraft.util.math.random.Random MC_RANDOM = new CheckedRandom(RANDOM.nextLong()); // Probably not a great way to do this, but oh well.
    @Getter public static final com.skycat.mystical.common.MysticalConfig CONFIG = com.skycat.mystical.common.MysticalConfig.createAndLoad();
    public static final MysticalCommandHandler COMMAND_HANDLER = new MysticalCommandHandler();
    // Using tags like this will make startup time a bit longer, but will allow for compatibility
    public static final TagKey<EntityType<?>> BOSSES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:bosses"));
    public static final TagKey<EntityType<?>> ZOMBIE_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:zombie_variants"));
    public static final TagKey<EntityType<?>> SKELETON_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:skeleton_variants"));
    public static final TagKey<EntityType<?>> ENDERMAN_VARIANTS = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:enderman_variants"));
    public static final TagKey<EntityType<?>> EVOKER_SUMMONABLE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("mystical:evoker_summonable"));
    public static SaveState save;

    public static HavenManager getHavenManager() {
        if (save == null) {
            throw new NullPointerException("Cannot get haven manager - save is null");
        }
        return save.getHavenManager();
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

        ServerLifecycleEvents.SERVER_STARTED.register(EVENT_HANDLER);
        ServerLifecycleEvents.SERVER_STARTED.register(this);
        ServerLifecycleEvents.SERVER_STOPPING.register(EVENT_HANDLER);
    }

    @Override
    public void onServerStarted(MinecraftServer server) {
        save = SaveState.loadSave(server);

        EntitySleepEvents.START_SLEEPING.register(getSpellHandler());
        EntitySleepEvents.STOP_SLEEPING.register(getSpellHandler());
        PlayerBlockBreakEvents.BEFORE.register(getSpellHandler());
        PlayerBlockBreakEvents.AFTER.register(getSpellHandler());
        ServerPlayerEvents.AFTER_RESPAWN.register(getSpellHandler());
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(getSpellHandler());
        AttackBlockCallback.EVENT.register(getSpellHandler());
    }
}
