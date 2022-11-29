package skycat.mystical.curses;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skycat.mystical.LogLevel;
import skycat.mystical.MysticalServer;
import skycat.mystical.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

import static skycat.mystical.MysticalServer.CONFIG;

public class CurseHandler implements EntitySleepEvents.StartSleeping, PlayerBlockBreakEvents.Before, ServerEntityEvents.EquipmentChange, CustomDamageHandler {
    @SuppressWarnings("rawtypes") ArrayList<CurseConsequence> consequences = new ArrayList<>();
    ArrayList<CurseRemovalCondition> removalConditions = new ArrayList<>();

    public CurseHandler() {
        initializeConsequences();
        initializeRemovalConditions();
    }

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        for (Curse curse : cursesOfConsequence(PlayerBlockBreakEvents.Before.class)) {
            boolean cancel = !((PlayerBlockBreakEvents.Before)curse.consequence.callback).beforeBlockBreak(world, player, pos, state, blockEntity);
            if (cancel) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int damage(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
        int maxDamage = 0;
        for (Curse curse : cursesOfConsequence(CustomDamageHandler.class)) {
            int newDamage = ((CustomDamageHandler)curse.consequence.callback).damage(stack, amount, entity, breakCallback);
            if (newDamage > maxDamage) {
                maxDamage = newDamage;
            }
        }
        return maxDamage;
    }

    public void doNighttimeEvents() {
        removeFulfilledCurses();
    }

    private void initializeConsequences() {
        // Pool of consequences goes here
        Collections.addAll(consequences,
                new CurseConsequence<ServerEntityEvents.EquipmentChange>(
                        (livingEntity, equipmentSlot, previousStack, currentStack) -> {
                            if (livingEntity.isPlayer()) {
                                if (currentStack.getDamage() + CONFIG.curseEquipmentChangeDamage() < currentStack.getMaxDamage()) { // Don't break it more than possible TODO: check if this allows going to 0 dmg
                                    currentStack.damage(CONFIG.curseEquipmentChangeDamage(), MysticalServer.MC_RANDOM, null); // Player is null so that stats aren't affected
                                }
                            }
                        }, ServerEntityEvents.EquipmentChange.class)
        );
    }

    private void initializeRemovalConditions() {
        // Pool of removal conditions goes here
        Collections.addAll(removalConditions,
                new TypedRemovalCondition<>(Stats.MINED, Blocks.COBBLESTONE, 10)

        );
    }

    @Override
    public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        for (Curse curse : cursesOfConsequence(ServerEntityEvents.EquipmentChange.class)) {
            ((ServerEntityEvents.EquipmentChange)curse.consequence.callback).onChange(livingEntity, equipmentSlot, previousStack, currentStack);
        }
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        for (Curse curse : cursesOfConsequence(EntitySleepEvents.StartSleeping.class)) {
            ((EntitySleepEvents.StartSleeping)curse.consequence.callback).onStartSleeping(entity, sleepingPos);
        }
    }

    @SuppressWarnings("unused") // Used by StatHandlerMixin
    public <T> void onStatIncreased(Stat<T> stat, int amount) {
        // Utils.log("stat increased: " + stat.getName() + " amount: " + amount);
        for (Curse curse : cursesOfConditions(stat)) {
            curse.removalCondition.fulfill(amount);
        }
    }

    private void removeFulfilledCurses() {
        // CREDIT https://stackoverflow.com/a/1196612, then IntelliJ being like hey do this instead
        CONFIG.activeCurses().removeIf(curse -> curse.removalCondition.isFulfilled());
    }

    /**
     * Get active curses with consequences of type clazz
     * @return A new ArrayList
     * @param clazz The type of consequence
     * @param <T> Consequence type
     */
    public <T> ArrayList<Curse> cursesOfConsequence(Class<T> clazz) {
        ArrayList<Curse> matchingCurses = new ArrayList<>();
        for (Curse curse : CONFIG.activeCurses()) {
            if (curse.consequence.callbackType.equals(clazz)) {
                matchingCurses.add(curse);
            }
        }
        return matchingCurses;
    }

    public <T> ArrayList<Curse> cursesOfConditions(Stat<T> stat) {
        ArrayList<Curse> matchingCurses = new ArrayList<>();
        for (Curse curse : CONFIG.activeCurses()) {
            if (curse.removalCondition.getClass().equals(TypedRemovalCondition.class)) { // TODO: Identified removal conditions
                TypedRemovalCondition<?> removalCondition = (TypedRemovalCondition<?>) curse.removalCondition;
                if (removalCondition.statType.equals(stat.getType()) && // Same statType, so values are the same class
                                removalCondition.statValue.equals(stat.getValue())) { // Values are the same (ex Blocks.COBBLESTONE and Blocks.COBBLESTONE)
                    matchingCurses.add(curse);
                }
            }
        }
        return matchingCurses;
    }

    public void activateNewCurse() {
        ArrayList<Curse> newCurses = CONFIG.activeCurses();
        newCurses.add(makeNewCurse());
        CONFIG.activeCurses(newCurses);
    }

    /**
     * Get a new curse, with consequence and removal condition randomly selected
     * @return A new curse
     */
    private Curse makeNewCurse() {
        if (removalConditions.size() == 0) { // Ideally, this should not be reachable without editing the pool manually.
            Utils.log("Tried to make a new curse, but the size of the removal condition pool was 0.", LogLevel.WARN);
            return null;
        }
        if (consequences.size() == 0) { // Ideally, this should not be reachable without editing the pool manually.
            Utils.log("Tried to make a new curse, but the size of the consequences pool was 0.", LogLevel.WARN);
            return null;
        }
        Utils.log("Making a new random curse.", LogLevel.INFO);
        return new Curse(
          consequences.get(MysticalServer.getRANDOM().nextInt(0, consequences.size())),
          removalConditions.get(MysticalServer.getRANDOM().nextInt(0, removalConditions.size()))
        );
    }

    /**
     * Get a new curse, targeting a difficulty (consequence difficulty * removal difficulty).
     * @param difficultyTarget The desired difficulty of the curse. Likely will not be exact.
     * @return A new curse
     */
    private Curse makeNewCurse(int difficultyTarget) {
        return null; // TODO
    }
}
