package skycat.mystical.util;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.fabric.api.entity.event.v1.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryRemovedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogic;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;


public enum EventCallbackEnum {
    // Easy regex check for correct names: "@SerializedName\("(.*)"\)\n    \1"
    // WARN: Uses a picked-over list of things that implement @FunctionalInterface. Some of these are not events, and some should probably be removed.
    @SerializedName("CUSTOM_DAMAGE_HANDLER")
    CUSTOM_DAMAGE_HANDLER(CustomDamageHandler.class), // Not really an event, but is helpful
    @SerializedName("DYNAMIC_REGISTRY_SETUP")
    DYNAMIC_REGISTRY_SETUP(DynamicRegistrySetupCallback.class),
    @SerializedName("ELYTRA_ALLOW")
    ELYTRA_ALLOW(EntityElytraEvents.Allow.class),
    @SerializedName("ELYTRA_CUSTOM")
    ELYTRA_CUSTOM(EntityElytraEvents.Custom.class),
    @SerializedName("ENTITY_TRACKING_START_TRACKING")
    ENTITY_TRACKING_START_TRACKING(EntityTrackingEvents.StartTracking.class),
    @SerializedName("ENTITY_TRACKING_STOP_TRACKING")
    ENTITY_TRACKING_STOP_TRACKING(EntityTrackingEvents.StopTracking.class),
    @SerializedName("EQUIPMENT_SLOT_PROVIDER")
    EQUIPMENT_SLOT_PROVIDER(EquipmentSlotProvider.class), // Not really an event, but might be helpful
    @SerializedName("MINECART_COMPARATOR_LOGIC")
    MINECART_COMPARATOR_LOGIC(MinecartComparatorLogic.class),
    @SerializedName("MODIFY_ITEM_ATTRIBUTE_MODIFIERS_CALLBACK")
    MODIFY_ITEM_ATTRIBUTE_MODIFIERS_CALLBACK(ModifyItemAttributeModifiersCallback.class), // Not really an event, but might be helpful
    @SerializedName("PLAYER_BLOCK_BREAK_AFTER")
    PLAYER_BLOCK_BREAK_AFTER(PlayerBlockBreakEvents.After.class),
    @SerializedName("PLAYER_BLOCK_BREAK_BEFORE")
    PLAYER_BLOCK_BREAK_BEFORE(PlayerBlockBreakEvents.Before.class),
    @SerializedName("PLAYER_BLOCK_BREAK_CANCELED")
    PLAYER_BLOCK_BREAK_CANCELED(PlayerBlockBreakEvents.Canceled.class),
    @SerializedName("REGISTRY_ENTRY_ADDED")
    REGISTRY_ENTRY_ADDED(RegistryEntryAddedCallback.class),
    @SerializedName("REGISTRY_ENTRY_REMOVED")
    REGISTRY_ENTRY_REMOVED(RegistryEntryRemovedCallback.class),
    @SerializedName("REGISTRY_ID_REMAP")
    REGISTRY_ID_REMAP(RegistryIdRemapCallback.class),
    @SerializedName("RENDER_ATTACHMENT_BLOCK_ENTITY")
    RENDER_ATTACHMENT_BLOCK_ENTITY(RenderAttachmentBlockEntity.class),
    @SerializedName("RENDER_CONTEXT_QUAD_TRANSFORM")
    RENDER_CONTEXT_QUAD_TRANSFORM(RenderContext.QuadTransform.class),
    @SerializedName("S2C_PLAY_CHANNEL_REGISTER")
    S2C_PLAY_CHANNEL_REGISTER(S2CPlayChannelEvents.Register.class),
    @SerializedName("S2C_PLAY_CHANNEL_UNREGISTER")
    S2C_PLAY_CHANNEL_UNREGISTER(S2CPlayChannelEvents.Unregister.class),
    @SerializedName("SERVER_BLOCK_ENTITY_LOAD")
    SERVER_BLOCK_ENTITY_LOAD(ServerBlockEntityEvents.Load.class),
    @SerializedName("SERVER_BLOCK_ENTITY_UNLOAD")
    SERVER_BLOCK_ENTITY_UNLOAD(ServerBlockEntityEvents.Unload.class),
    @SerializedName("SERVER_CHUNK_LOAD")
    SERVER_CHUNK_LOAD(ServerChunkEvents.Load.class),
    @SerializedName("SERVER_CHUNK_UNLOAD")
    SERVER_CHUNK_UNLOAD(ServerChunkEvents.Unload.class),
    @SerializedName("SERVER_ENTITY_COMBAT_AFTER_KILLED_OTHER_ENTITY")
    SERVER_ENTITY_COMBAT_AFTER_KILLED_OTHER_ENTITY(ServerEntityCombatEvents.AfterKilledOtherEntity.class),
    @SerializedName("SERVER_ENTITY_EQUIPMENT_CHANGE")
    SERVER_ENTITY_EQUIPMENT_CHANGE(ServerEntityEvents.EquipmentChange.class),
    @SerializedName("SERVER_ENTITY_LOAD")
    SERVER_ENTITY_LOAD(ServerEntityEvents.Load.class),
    @SerializedName("SERVER_ENTITY_UNLOAD")
    SERVER_ENTITY_UNLOAD(ServerEntityEvents.Unload.class),
    @SerializedName("SERVER_ENTITY_WORLD_CHANGE_AFTER_ENTITY_CHANGE")
    SERVER_ENTITY_WORLD_CHANGE_AFTER_ENTITY_CHANGE(ServerEntityWorldChangeEvents.AfterEntityChange.class),
    @SerializedName("SERVER_ENTITY_WORLD_CHANGE_AFTER_PLAYER_CHANGE")
    SERVER_ENTITY_WORLD_CHANGE_AFTER_PLAYER_CHANGE(ServerEntityWorldChangeEvents.AfterPlayerChange.class),
    @SerializedName("SERVER_LIFECYCLE_END_DATA_PACK_RELOAD")
    SERVER_LIFECYCLE_END_DATA_PACK_RELOAD(ServerLifecycleEvents.EndDataPackReload.class),
    @SerializedName("SERVER_LIFECYCLE_SERVER_STARTED")
    SERVER_LIFECYCLE_SERVER_STARTED(ServerLifecycleEvents.ServerStarted.class),
    @SerializedName("SERVER_LIFECYCLE_SERVER_STARTING")
    SERVER_LIFECYCLE_SERVER_STARTING(ServerLifecycleEvents.ServerStarting.class),
    @SerializedName("SERVER_LIFECYCLE_SERVER_STOPPED")
    SERVER_LIFECYCLE_SERVER_STOPPED(ServerLifecycleEvents.ServerStopped.class),
    @SerializedName("SERVER_LIFECYCLE_SERVER_STOPPING")
    SERVER_LIFECYCLE_SERVER_STOPPING(ServerLifecycleEvents.ServerStopping.class),
    @SerializedName("SERVER_LIFECYCLE_START_DATA_PACK_RELOAD")
    SERVER_LIFECYCLE_START_DATA_PACK_RELOAD(ServerLifecycleEvents.StartDataPackReload.class),
    @SerializedName("SERVER_LIFECYCLE_SYNC_DATA_PACK_CONTENTS")
    SERVER_LIFECYCLE_SYNC_DATA_PACK_CONTENTS(ServerLifecycleEvents.SyncDataPackContents.class),
    @SerializedName("SERVER_LIVING_ALLOW_DEATH")
    SERVER_LIVING_ALLOW_DEATH(ServerLivingEntityEvents.AllowDeath.class),
    @SerializedName("SERVER_LOGIN_CONNECTION_DISCONNECT")
    SERVER_LOGIN_CONNECTION_DISCONNECT(ServerLoginConnectionEvents.Disconnect.class),
    @SerializedName("SERVER_LOGIN_CONNECTION_INIT")
    SERVER_LOGIN_CONNECTION_INIT(ServerLoginConnectionEvents.Init.class),
    @SerializedName("SERVER_LOGIN_CONNECTION_QUERY_START")
    SERVER_LOGIN_CONNECTION_QUERY_START(ServerLoginConnectionEvents.QueryStart.class),
    @SerializedName("SERVER_LOGIN_NETWORKING_LOGIN_QUERY_RESPONSE_HANDLER")
    SERVER_LOGIN_NETWORKING_LOGIN_QUERY_RESPONSE_HANDLER(ServerLoginNetworking.LoginQueryResponseHandler.class),
    @SerializedName("SERVER_LOGIN_NETWORKING_LOGIN_SYNCHRONIZER")
    SERVER_LOGIN_NETWORKING_LOGIN_SYNCHRONIZER(ServerLoginNetworking.LoginSynchronizer.class),
    @SerializedName("SERVER_MESSAGE_ALLOW_CHAT_MESSAGE")
    SERVER_MESSAGE_ALLOW_CHAT_MESSAGE(ServerMessageEvents.AllowChatMessage.class),
    @SerializedName("SERVER_MESSAGE_ALLOW_COMMAND_MESSAGE")
    SERVER_MESSAGE_ALLOW_COMMAND_MESSAGE(ServerMessageEvents.AllowCommandMessage.class),
    @SerializedName("SERVER_MESSAGE_ALLOW_GAME_MESSAGE")
    SERVER_MESSAGE_ALLOW_GAME_MESSAGE(ServerMessageEvents.AllowGameMessage.class),
    @SerializedName("SERVER_MESSAGE_CHAT_MESSAGE")
    SERVER_MESSAGE_CHAT_MESSAGE(ServerMessageEvents.ChatMessage.class),
    @SerializedName("SERVER_MESSAGE_COMMAND_MESSAGE")
    SERVER_MESSAGE_COMMAND_MESSAGE(ServerMessageEvents.CommandMessage.class),
    @SerializedName("SERVER_MESSAGE_GAME_MESSAGE")
    SERVER_MESSAGE_GAME_MESSAGE(ServerMessageEvents.GameMessage.class),
    @SerializedName("SERVER_PLAYER_ALLOW_RESPAWN")
    SERVER_PLAYER_ALLOW_RESPAWN(ServerPlayerEvents.AfterRespawn.class),
    @SerializedName("SERVER_PLAYER_COPY_FROM")
    SERVER_PLAYER_COPY_FROM(ServerPlayerEvents.CopyFrom.class),
    @SerializedName("SERVER_PLAY_CONNECTION_DISCONNECT")
    SERVER_PLAY_CONNECTION_DISCONNECT(ServerPlayConnectionEvents.Disconnect.class),
    @SerializedName("SERVER_PLAY_CONNECTION_INIT")
    SERVER_PLAY_CONNECTION_INIT(ServerPlayConnectionEvents.Init.class),
    @SerializedName("SERVER_PLAY_CONNECTION_JOIN")
    SERVER_PLAY_CONNECTION_JOIN(ServerPlayConnectionEvents.Join.class),
    @SerializedName("SERVER_PLAY_NETWORKING_PLAY_CHANNEL_HANDLER")
    SERVER_PLAY_NETWORKING_PLAY_CHANNEL_HANDLER(ServerPlayNetworking.PlayChannelHandler.class),
    @SerializedName("SERVER_TICK_END_TICK")
    SERVER_TICK_END_TICK(ServerTickEvents.EndTick.class),
    @SerializedName("SERVER_TICK_END_WORLD_TICK")
    SERVER_TICK_END_WORLD_TICK(ServerTickEvents.EndWorldTick.class),
    @SerializedName("SERVER_TICK_START_TICK")
    SERVER_TICK_START_TICK(ServerTickEvents.StartTick.class),
    @SerializedName("SERVER_TICK_START_WORLD_TICK")
    SERVER_TICK_START_WORLD_TICK(ServerTickEvents.StartWorldTick.class),
    @SerializedName("SERVER_WORLD_LOAD")
    SERVER_WORLD_LOAD(ServerWorldEvents.Load.class),
    @SerializedName("SERVER_WORLD_UNLOAD")
    SERVER_WORLD_UNLOAD(ServerWorldEvents.Unload.class),
    @SerializedName("SLEEP_ALLOW")
    SLEEP_ALLOW(EntitySleepEvents.AllowSleeping.class),
    @SerializedName("SLEEP_ALLOW_BED")
    SLEEP_ALLOW_BED(EntitySleepEvents.AllowBed.class),
    @SerializedName("SLEEP_ALLOW_NEARBY_MONSTERS")
    SLEEP_ALLOW_NEARBY_MONSTERS(EntitySleepEvents.AllowNearbyMonsters.class),
    @SerializedName("SLEEP_ALLOW_RESETTING_TIME")
    SLEEP_ALLOW_RESETTING_TIME(EntitySleepEvents.AllowResettingTime.class),
    @SerializedName("SLEEP_ALLOW_SETTING_SPAWN")
    SLEEP_ALLOW_SETTING_SPAWN(EntitySleepEvents.AllowSettingSpawn.class),
    @SerializedName("SLEEP_ALLOW_SLEEP_TIME")
    SLEEP_ALLOW_SLEEP_TIME(EntitySleepEvents.AllowSleepTime.class),
    @SerializedName("SLEEP_MODIFY_SLEEPING_DIRECTION")
    SLEEP_MODIFY_SLEEPING_DIRECTION(EntitySleepEvents.ModifySleepingDirection.class),
    @SerializedName("SLEEP_MODIFY_WAKE_UP_POSITION")
    SLEEP_MODIFY_WAKE_UP_POSITION(EntitySleepEvents.ModifyWakeUpPosition.class),
    @SerializedName("SLEEP_SET_BED_OCCUPATION_STATE")
    SLEEP_SET_BED_OCCUPATION_STATE(EntitySleepEvents.SetBedOccupationState.class),
    @SerializedName("SLEEP_START")
    SLEEP_START(EntitySleepEvents.StartSleeping.class),
    @SerializedName("SLEEP_STOP")
    SLEEP_STOP(EntitySleepEvents.StopSleeping.class);


    private final Class<?> clazz;

    EventCallbackEnum(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
