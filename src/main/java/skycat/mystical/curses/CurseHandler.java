package skycat.mystical.curses;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skycat.mystical.LogLevel;
import skycat.mystical.Mystical;
import skycat.mystical.util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

import static skycat.mystical.Mystical.GSON;

public class CurseHandler implements EntitySleepEvents.StartSleeping, PlayerBlockBreakEvents.Before, ServerEntityEvents.EquipmentChange, CustomDamageHandler, PlayerBlockBreakEvents.After {
    private static final CurseConsequenceEnum[] consequenceEnums = CurseConsequenceEnum.values();
    private static final CurseRemovalConditionEnum[] removalConditionEnums = CurseRemovalConditionEnum.values();
    private static final File SAVE_FILE = new File("config/curseHandler.json");
    ArrayList<Curse> activeCurses = new ArrayList<>();

    public CurseHandler() {
    }

    public static CurseHandler loadOrNew() {
        try (Scanner scanner = new Scanner(SAVE_FILE)) {
            return GSON.fromJson(scanner.nextLine(), CurseHandler.class);
        } catch (IOException e) {
            // TODO: Logging
            return new CurseHandler();
        }
    }

    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        cursesOfConsequence(PlayerBlockBreakEvents.After.class).forEach(curse -> ((PlayerBlockBreakEvents.After) curse.getConsequence().callback).afterBlockBreak(world, player, pos, state, blockEntity));
    }

    public void save() {
        try (PrintWriter pw = new PrintWriter(SAVE_FILE)) {
            pw.println(GSON.toJson(this));
        } catch (IOException e) {
            // TODO: Logging
        }
    }

    public void activateNewCurse() {
        activeCurses.add(makeNewCurse());
    }

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        for (Curse curse : cursesOfConsequence(PlayerBlockBreakEvents.Before.class)) {
            boolean cancel = !((PlayerBlockBreakEvents.Before) curse.getConsequence().callback).beforeBlockBreak(world, player, pos, state, blockEntity);
            if (cancel) {
                return false;
            }
        }
        return true;
    }

    public <T> ArrayList<Curse> cursesOfConditions(Stat<T> stat) {
        ArrayList<Curse> matchingCurses = new ArrayList<>();
        for (Curse curse : activeCurses) {
            if (curse.getRemovalCondition().getClass().equals(TypedRemovalCondition.class)) { // TODO: Identified removal conditions
                TypedRemovalCondition<?> removalCondition = (TypedRemovalCondition<?>) curse.getRemovalCondition();
                if (removalCondition.statType.equals(stat.getType()) && // Same statType, so values are the same class
                    removalCondition.statValue.equals(stat.getValue())) { // Values are the same (ex Blocks.COBBLESTONE and Blocks.COBBLESTONE)
                    matchingCurses.add(curse);
                }
            }
        }
        return matchingCurses;
    }

    /**
     * Get active curses with consequences of type clazz
     *
     * @param clazz The type of consequence
     * @param <T>   Consequence type
     * @return A new ArrayList
     */
    public <T> ArrayList<Curse> cursesOfConsequence(Class<T> clazz) {
        ArrayList<Curse> matchingCurses = new ArrayList<>();
        for (Curse curse : activeCurses) {
            if (curse.getConsequence().callbackType.equals(clazz)) {
                matchingCurses.add(curse);
            }
        }
        return matchingCurses;
    }

    @Override
    public int damage(ItemStack stack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback) {
        int maxDamage = 0;
        for (Curse curse : cursesOfConsequence(CustomDamageHandler.class)) {
            int newDamage = ((CustomDamageHandler) curse.getConsequence().callback).damage(stack, amount, entity, breakCallback);
            if (newDamage > maxDamage) {
                maxDamage = newDamage;
            }
        }
        return maxDamage;
    }

    public void doNighttimeEvents() {
        removeFulfilledCurses();
    }

    /**
     * Get a new curse, with consequence and removal condition randomly selected
     *
     * @return A new curse
     */
    private Curse makeNewCurse() {
        if (removalConditionEnums.length == 0) { // Ideally, this should not be reachable without editing the pool manually.
            Utils.log("Tried to make a new curse, but the size of the removal condition pool was 0.", LogLevel.WARN);
            return null;
        }
        if (consequenceEnums.length == 0) { // Ideally, this should not be reachable without editing the pool manually.
            Utils.log("Tried to make a new curse, but the size of the consequences pool was 0.", LogLevel.WARN);
            return null;
        }
        Utils.log("Making a new random curse.", LogLevel.INFO);
        return new Curse(
                consequenceEnums[Mystical.getRANDOM().nextInt(0, consequenceEnums.length)],
                removalConditionEnums[Mystical.getRANDOM().nextInt(0, removalConditionEnums.length)]
        );
    }

    /**
     * Get a new curse, targeting a difficulty (consequence difficulty * removal difficulty).
     *
     * @param difficultyTarget The desired difficulty of the curse. Likely will not be exact.
     * @return A new curse
     */
    private Curse makeNewCurse(int difficultyTarget) {
        return null; // TODO
    }

    @Override
    public void onChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        for (Curse curse : cursesOfConsequence(ServerEntityEvents.EquipmentChange.class)) {
            ((ServerEntityEvents.EquipmentChange) curse.getConsequence().callback).onChange(livingEntity, equipmentSlot, previousStack, currentStack);
        }
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        for (Curse curse : cursesOfConsequence(EntitySleepEvents.StartSleeping.class)) {
            ((EntitySleepEvents.StartSleeping) curse.getConsequence().callback).onStartSleeping(entity, sleepingPos);
        }
    }

    @SuppressWarnings("unused") // Used by StatHandlerMixin
    public <T> void onStatIncreased(Stat<T> stat, int amount) {
        // Utils.log("stat increased: " + stat.getName() + " amount: " + amount);
        for (Curse curse : cursesOfConditions(stat)) {
            curse.getRemovalCondition().fulfill(amount);
        }
    }

    public int removeDisabledCurses() {
        return 0; // TODO
    }

    private void removeFulfilledCurses() {
        // CREDIT https://stackoverflow.com/a/1196612, then IntelliJ being like hey do this instead
        activeCurses.removeIf(curse -> curse.getRemovalCondition().isFulfilled());
    }
}
