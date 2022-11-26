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
import java.util.Random;
import java.util.function.Consumer;

import static skycat.mystical.MysticalServer.CONFIG;

public class CurseHandler implements EntitySleepEvents.StartSleeping, PlayerBlockBreakEvents.Before, ServerEntityEvents.EquipmentChange, CustomDamageHandler {
    ArrayList<Curse> activeCurses = new ArrayList<>();
    @SuppressWarnings("rawtypes") ArrayList<CurseConsequence> consequences = new ArrayList<>();
    ArrayList<CurseRemovalCondition> removalConditions = new ArrayList<>();
    Random random = new Random();

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
                new CurseConsequence<CustomDamageHandler>((stack, amount, entity, breakCallback) -> amount * CONFIG.curseDamageMultiplier()),
                new CurseConsequence<ServerEntityEvents.EquipmentChange>(
                        (livingEntity, equipmentSlot, previousStack, currentStack) -> currentStack.damage(CONFIG.curseEquipmentChangeDamage(), MysticalServer.MC_RANDOM, null))
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

    public <T> void onStatIncreased(Stat<T> stat, int amount) {
        Utils.log("stat increased: " + stat.getName() + " amount: " + amount);
        // TODO
        /*
        for (Curse curse : cursesOfConditions(stat)) {
            curse.removalCondition.fulfill(amount);
        }
         */
    }

    private void removeFulfilledCurses() {
        // CREDIT https://stackoverflow.com/a/1196612, then IntelliJ being like hey do this instead
        activeCurses.removeIf(curse -> curse.removalCondition.isFulfilled());
    }

    /**
     * Get active curses with consequences of type clazz
     * @return A new ArrayList
     * @param clazz The type of consequence
     * @param <T> Consequence type
     */
    public <T> ArrayList<Curse> cursesOfConsequence(Class<T> clazz) {
        ArrayList<Curse> matchingCurses = new ArrayList<>();
        for (Curse curse : activeCurses) {
            if (curse.consequence.callback.getClass().equals(clazz)) {
                matchingCurses.add(curse);
            }
        }
        return matchingCurses;
    }

    public <T> ArrayList<Curse> cursesOfConditions(Stat<T> stat) {
        ArrayList<Curse> matchingCurses = new ArrayList<>();
        for (Curse curse : activeCurses) { // TODO: Identified removal conditions

        }
        return matchingCurses;
    }

    public void activateNewCurse() {
        activeCurses.add(makeNewCurse());
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
          consequences.get(random.nextInt(0, consequences.size())),
          removalConditions.get(random.nextInt(0, removalConditions.size()))
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
