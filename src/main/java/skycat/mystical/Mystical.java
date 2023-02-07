package skycat.mystical;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
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
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.CheckedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skycat.mystical.command.MysticalCommandHandler;
import skycat.mystical.spell.SpellHandler;
import skycat.mystical.spell.consequence.SpellConsequence;
import skycat.mystical.spell.cure.SpellCure;
import skycat.mystical.util.*;

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
            .registerTypeAdapter(StatType.class, new StatTypeSerializer())
            .registerTypeAdapter(Stat.class, new StatSerializer())
            .registerTypeAdapter(Identifier.class, new Identifier.Serializer())
            .registerTypeAdapter(SpellCure.class, new SpellCure.Serializer())
            .registerTypeAdapter(SpellConsequence.class, new SpellConsequence.Serializer())
            .registerTypeAdapter(Class.class, new ClassSerializer())
            .create();
    @Getter public static final MysticalEventHandler EVENT_HANDLER = new MysticalEventHandler();
    @Getter public static final Random RANDOM = new Random();
    @Getter public static final net.minecraft.util.math.random.Random MC_RANDOM = new CheckedRandom(RANDOM.nextLong()); // WARN: Probably not a great way to do this
    @Getter public static final skycat.mystical.MysticalConfig CONFIG = skycat.mystical.MysticalConfig.createAndLoad();
    @Getter public static final SpellHandler SPELL_HANDLER = SpellHandler.loadOrNew();
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
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register(EVENT_HANDLER);
    }
}
