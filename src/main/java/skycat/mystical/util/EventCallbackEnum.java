package skycat.mystical.util;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.fabric.api.entity.event.v1.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;

public enum EventCallbackEnum {
    // Easy regex check for correct names: "@SerializedName\("(.*)"\)\n    \1"
    @SerializedName("SLEEP_START")
    SLEEP_START(EntitySleepEvents.StartSleeping.class),
    @SerializedName("ELYTRA_ALLOW")
    ELYTRA_ALLOW(EntityElytraEvents.Allow.class),
    @SerializedName("ELYTRA_CUSTOM")
    ELYTRA_CUSTOM(EntityElytraEvents.Custom.class),
    @SerializedName("SLEEP_ALLOW_BED")
    SLEEP_ALLOW_BED(EntitySleepEvents.AllowBed.class),
    @SerializedName("SLEEP_ALLOW_NEARBY_MONSTERS")
    SLEEP_ALLOW_NEARBY_MONSTERS(EntitySleepEvents.AllowNearbyMonsters.class),
    @SerializedName("SLEEP_ALLOW_RESETTING_TIME")
    SLEEP_ALLOW_RESETTING_TIME(EntitySleepEvents.AllowResettingTime.class),
    @SerializedName("SLEEP_ALLOW_SETTING_SPAWN")
    SLEEP_ALLOW_SETTING_SPAWN(EntitySleepEvents.AllowSettingSpawn.class),
    @SerializedName("SLEEP_ALLOW")
    SLEEP_ALLOW(EntitySleepEvents.AllowSleeping.class),
    @SerializedName("SLEEP_ALLOW_SLEEP_TIME")
    SLEEP_ALLOW_SLEEP_TIME(EntitySleepEvents.AllowSleepTime.class),
    @SerializedName("SLEEP_MODIFY_SLEEPING_DIRECTION")
    SLEEP_MODIFY_SLEEPING_DIRECTION(EntitySleepEvents.ModifySleepingDirection.class),
    @SerializedName("SLEEP_MODIFY_WAKE_UP_POSITION")
    SLEEP_MODIFY_WAKE_UP_POSITION(EntitySleepEvents.ModifyWakeUpPosition.class),
    @SerializedName("SLEEP_STOP")
    SLEEP_STOP(EntitySleepEvents.StopSleeping.class),
    @SerializedName("SLEEP_SET_BED_OCCUPATION_STATE")
    SLEEP_SET_BED_OCCUPATION_STATE(EntitySleepEvents.SetBedOccupationState.class),
    @SerializedName("SERVER_ENTITY_COMBAT_AFTER_KILLED_OTHER_ENTITY")
    SERVER_ENTITY_COMBAT_AFTER_KILLED_OTHER_ENTITY(ServerEntityCombatEvents.AfterKilledOtherEntity.class),
    @SerializedName("SERVER_ENTITY_WORLD_CHANGE_AFTER_ENTITY_CHANGE")
    SERVER_ENTITY_WORLD_CHANGE_AFTER_ENTITY_CHANGE(ServerEntityWorldChangeEvents.AfterEntityChange.class),
    @SerializedName("SERVER_ENTITY_WORLD_CHANGE_AFTER_PLAYER_CHANGE")
    SERVER_ENTITY_WORLD_CHANGE_AFTER_PLAYER_CHANGE(ServerEntityWorldChangeEvents.AfterPlayerChange.class),
    @SerializedName("SERVER_PLAYER_ALLOW_RESPAWN")
    SERVER_PLAYER_ALLOW_RESPAWN(ServerPlayerEvents.AfterRespawn.class),
    @SerializedName("SERVER_LIVING_ALLOW_DEATH")
    SERVER_LIVING_ALLOW_DEATH(ServerLivingEntityEvents.AllowDeath.class),
    SERVER_PLAYER_COPY_FROM(ServerPlayerEvents.CopyFrom.class),
    PLAYER_BLOCK_BREAK_AFTER(PlayerBlockBreakEvents.After.class),
    PLAYER_BLOCK_BREAK_BEFORE(PlayerBlockBreakEvents.Before.class),
    PLAYER_BLOCK_BREAK_CANCELED(PlayerBlockBreakEvents.Canceled.class),
    CUSTOM_DAMAGE_HANDLER(CustomDamageHandler.class), // Not really an event, but is helpful
    EQUIPMENT_SLOT_PROVIDER(EquipmentSlotProvider.class), // Not really an event, but might be helpful
    MODIFY_ITEM_ATTRIBUTE_MODIFIERS_CALLBACK(ModifyItemAttributeModifiersCallback.class), // Not really an event, but might be helpful
    SERVER_BLOCK_ENTITY_LOAD(ServerBlockEntityEvents.Load.class),
    SERVER_BLOCK_ENTITY_UNLOAD(ServerBlockEntityEvents.Unload.class),
    SERVER_CHUNK_LOAD(ServerChunkEvents.Load.class),
    ;


    private final Class<?> clazz;
    EventCallbackEnum(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
