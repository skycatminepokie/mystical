package skycat.mystical.util;

import com.google.gson.annotations.SerializedName;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;

public enum EventCallbackEnum {
    @SerializedName("START_SLEEPING")
    START_SLEEPING(EntitySleepEvents.StartSleeping.class);

    private final Class<?> clazz;
    EventCallbackEnum(Class<?> clazz) {
        this.clazz = clazz;
    }
    // Hello! I need to be able to serialize a Class<?>. Here's what I've got:
    // The problem is, GSON seems to think it needs to sear

    public Class<?> getClazz() {
        return clazz;
    }
}
