package com.skycat.mystical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import com.skycat.mystical.common.spell.SpellHandler;
import com.skycat.mystical.common.spell.consequence.SpellConsequence;
import com.skycat.mystical.common.spell.cure.SpellCure;
import com.skycat.mystical.common.util.BlockSerializer;
import com.skycat.mystical.common.util.ClassSerializer;
import com.skycat.mystical.common.util.LocalDateTimeSerializer;
import com.skycat.mystical.common.util.StatCodec;
import com.skycat.mystical.server.HavenManager;
import com.skycat.mystical.server.MysticalEventHandler;
import com.skycat.mystical.server.command.MysticalCommandHandler;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.CheckedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Getter
public class Mystical implements ModInitializer {
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
            .create();
    @Getter public static final MysticalEventHandler EVENT_HANDLER = new MysticalEventHandler();
    @Getter public static final Random RANDOM = new Random();
    @Getter public static final net.minecraft.util.math.random.Random MC_RANDOM = new CheckedRandom(RANDOM.nextLong()); // Probably not a great way to do this, but oh well.
    @Getter public static final com.skycat.mystical.common.MysticalConfig CONFIG = com.skycat.mystical.common.MysticalConfig.createAndLoad();
    @Getter public static SpellHandler SPELL_HANDLER = SpellHandler.loadOrNew();
    @Getter public static final HavenManager HAVEN_MANAGER = HavenManager.loadOrNew();
    public static final MysticalCommandHandler COMMAND_HANDLER = new MysticalCommandHandler();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(COMMAND_HANDLER);

        EntitySleepEvents.START_SLEEPING.register(SPELL_HANDLER);
        EntitySleepEvents.STOP_SLEEPING.register(SPELL_HANDLER);
        PlayerBlockBreakEvents.BEFORE.register(SPELL_HANDLER);
        PlayerBlockBreakEvents.AFTER.register(SPELL_HANDLER);
        ServerPlayerEvents.AFTER_RESPAWN.register(SPELL_HANDLER);
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(SPELL_HANDLER);

        ServerLifecycleEvents.SERVER_STARTED.register(EVENT_HANDLER);
        ServerLifecycleEvents.SERVER_STOPPING.register(EVENT_HANDLER);
    }
}
