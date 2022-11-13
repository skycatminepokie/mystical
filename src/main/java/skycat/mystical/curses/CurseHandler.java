package skycat.mystical.curses;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class CurseHandler implements EntitySleepEvents.StartSleeping, PlayerBlockBreakEvents.Before {
    ArrayList<Curse> activeCurses = new ArrayList<>();
    ArrayList<CurseConsequence> consequences = new ArrayList<>();
    ArrayList<CurseRemovalCondition> removalConditions = new ArrayList<>();

    public CurseHandler() {
        initializeCurses();
    }

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        return true;
    }

    public void doNighttimeEvents() {
        removeFulfilledCurses();
    }

    private void initializeCurses() {

    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        for (Curse curse : cursesOfConsequence(EntitySleepEvents.StartSleeping.class)) {
            ((EntitySleepEvents.StartSleeping)curse.consequence.callback).onStartSleeping(entity, sleepingPos);
        }
    }

    public <T> void onStatIncreased(Stat<T> stat, int amount) {

    }

    private void removeFulfilledCurses() {

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

    // Get random curse
    // Get random curse, but weight chances based on difficulty
    // Get random curse, but weight chances to get close to a difficulty
}
