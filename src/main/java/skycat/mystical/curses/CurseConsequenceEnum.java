package skycat.mystical.curses;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import skycat.mystical.MysticalServer;
import skycat.mystical.util.Utils;

import java.util.HashMap;

import static skycat.mystical.MysticalServer.CONFIG;

@SuppressWarnings("rawtypes")
public enum CurseConsequenceEnum {
    DAMAGE_EQUIPMENT_ON_CHANGE("DAMAGE_EQUIPMENT_ON_CHANGE"),
    PREVENT_SLEEPING("PREVENT_SLEEPING");

    public final String id;
    private static final HashMap<CurseConsequenceEnum, CurseConsequence> lookupMap = new HashMap<>();
    static {
        lookupMap.put(DAMAGE_EQUIPMENT_ON_CHANGE, new CurseConsequence<ServerEntityEvents.EquipmentChange>(
                (livingEntity, equipmentSlot, previousStack, currentStack) -> {
                    if (livingEntity.isPlayer()) {
                        if (currentStack.getDamage() + CONFIG.damageEquipmentOnChangeCurse.damageAmount() < currentStack.getMaxDamage()) { // Don't break it more than possible TODO: check if this allows going to 0 dmg
                            currentStack.damage(CONFIG.damageEquipmentOnChangeCurse.damageAmount(), MysticalServer.MC_RANDOM, null); // Player is null so that stats aren't affected
                        }
                    }
                }, ServerEntityEvents.EquipmentChange.class));
        lookupMap.put(PREVENT_SLEEPING, new CurseConsequence<EntitySleepEvents.StartSleeping>(
                ((entity, sleepingPos) -> {
                    if (entity.isPlayer()) {
                        entity.wakeUp();
                        if (CONFIG.preventSleepingCurse.sendMessageToPlayer()) {
                            Utils.sendMessageToPlayer((ServerPlayerEntity) entity, CONFIG.preventSleepingCurse.message(), CONFIG.preventSleepingCurse.actionBar());
                        }
                    } else if (!CONFIG.preventSleepingCurse.playersOnly()) {
                        entity.wakeUp();
                    }
                    Utils.log("preventSleepingCurse triggered", CONFIG.preventSleepingCurse.logLevel());
                }), EntitySleepEvents.StartSleeping.class));
    }

    CurseConsequenceEnum(String id) {
        this.id = id;
    }

    public static CurseConsequence lookup(CurseConsequenceEnum consequenceEnum) {
        return lookupMap.get(consequenceEnum);
    }
}
