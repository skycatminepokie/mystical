package skycat.mystical.curses;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import skycat.mystical.Mystical;
import skycat.mystical.util.Utils;

import java.util.HashMap;

import static skycat.mystical.Mystical.CONFIG;

@SuppressWarnings("rawtypes")
public enum CurseConsequenceEnum {
    DAMAGE_EQUIPMENT_ON_CHANGE("DAMAGE_EQUIPMENT_ON_CHANGE"),
    PREVENT_SLEEPING("PREVENT_SLEEPING"),
    LEVITATE_ON_LOG_BREAK("LEVITATE_ON_LOG_BREAK");

    public final String id;
    private static final HashMap<CurseConsequenceEnum, CurseConsequence> lookupMap = new HashMap<>();
    static {
        lookupMap.put(DAMAGE_EQUIPMENT_ON_CHANGE, new CurseConsequence<ServerEntityEvents.EquipmentChange>(
                (livingEntity, equipmentSlot, previousStack, currentStack) -> {
                    if (livingEntity.isPlayer()) {
                        if (currentStack.getDamage() + CONFIG.damageEquipmentOnChangeCurse.damageAmount() < currentStack.getMaxDamage()) { // Don't break it more than possible TODO: check if this allows going to 0 dmg
                            currentStack.damage(CONFIG.damageEquipmentOnChangeCurse.damageAmount(), Mystical.MC_RANDOM, null); // Player is null so that stats aren't affected
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
        lookupMap.put(LEVITATE_ON_LOG_BREAK, new CurseConsequence<PlayerBlockBreakEvents.After>(
                ((world, player, pos, state, blockEntity) -> {
                    if (state.isIn(BlockTags.LOGS)) {
                        // Utils.giveStatusEffect(player); TODO make configurable
                    }
                })
                , PlayerBlockBreakEvents.After.class));
    }

    CurseConsequenceEnum(String id) {
        this.id = id;
    }

    public static CurseConsequence lookup(CurseConsequenceEnum consequenceEnum) {
        return lookupMap.get(consequenceEnum);
    }
}
