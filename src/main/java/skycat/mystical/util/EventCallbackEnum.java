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

    public Class<?> getClazz() {
        return clazz;
    }
}
