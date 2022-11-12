package skycat.mystical.curses;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class CurseHandler implements EntitySleepEvents.StartSleeping, PlayerBlockBreakEvents.Before {
    public final ArrayList<ArrayList> curseArrayLists = new ArrayList<>(); // WARN scuffed as all get out
    public final ArrayList<Curse<EntitySleepEvents.StartSleeping>> startSleepingCurses = new ArrayList<>();
    public final ArrayList<Curse<PlayerBlockBreakEvents.Before>> beforeBreakBlockCurses = new ArrayList<>();

    public CurseHandler() {
        // Initialize curses
        startSleepingCurses.add(new Curse<>(
                EntitySleepEvents.START_SLEEPING,
                (entity, sleepingPos) -> {
                    entity.kill();
                },
                new CurseRemovalCondition<>(Stats.MINED, Blocks.STONE, 1000, 0),
                1.0));
        startSleepingCurses.add(new Curse<>(
                EntitySleepEvents.START_SLEEPING,
                (entity, sleepingPos) -> {
                    entity.wakeUp();
                    if (entity.isPlayer()) {
                        ((PlayerEntity)entity).sendMessage(Text.of("Bruh you need beetroot slurp"), false);
                    }
                },
                new CurseRemovalCondition<>(Stats.CRAFTED, Items.BEETROOT_SOUP, 10, 0),
                1.0)
        );
        beforeBreakBlockCurses.add(
                new Curse<>(PlayerBlockBreakEvents.BEFORE, (world, player, pos, state, blockEntity) -> {
                    if (state.getBlock().equals(Blocks.ANDESITE)) {
                        player.addExhaustion(0.05f);
                        player.sendMessage(Text.of("oof"), true);
                        return false;
                    }
                    return true;
                }, new CurseRemovalCondition<>(Stats.CRAFTED, Items.BARREL, 20), 1.0)
        );
        beforeBreakBlockCurses.add(
                new Curse<>(PlayerBlockBreakEvents.BEFORE, ((world, player, pos, state, blockEntity) -> {
                    if (state.getBlock().equals(Blocks.GRASS_BLOCK)) {
                        player.addExhaustion(1.0f);
                    }
                    return true;
                }
                ), new CurseRemovalCondition<>(Stats.USED, Items.ENDER_EYE, 1), 1.0)
        );

        disableCurses(startSleepingCurses);
        enableRandom(startSleepingCurses);
        disableCurses(beforeBreakBlockCurses);
        enableRandom(beforeBreakBlockCurses);
        curseArrayLists.add(startSleepingCurses);
        curseArrayLists.add(beforeBreakBlockCurses);
    }

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        for (Curse<PlayerBlockBreakEvents.Before> curse : beforeBreakBlockCurses) {
            if (curse.enabled) {
                if (!curse.callback.beforeBlockBreak(world, player, pos, state, blockEntity)) {
                    return false; // Cancel it if something says to
                }
            } // TODO probably delete disabled curses
        }
        return true; // Don't cancel
    }

    private <T extends ArrayList<Curse<S>>,S> void disableCurses(T curseList) {
        for (Curse<?> curse : curseList) {
            curse.disable();
        }
    }

    private <T extends ArrayList<Curse<S>>, S> void enableRandom(T curseList) {
        curseList.get(new Random().nextInt(0, curseList.size())).enable();
    }

    public void doNighttimeEvents() {
        removeFulfilledCurses();
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        for (Curse<EntitySleepEvents.StartSleeping> curse : startSleepingCurses) {
            if (curse.enabled) {
                curse.callback.onStartSleeping(entity, sleepingPos);
            } // TODO probably delete disabled curses
        }
    }

    public <T> void onStatIncreased(Stat<T> stat, int amount) {
        for (ArrayList<Curse<?>> curses : curseArrayLists) { // TODO fix warning
            for (Curse<?> curse : curses) {
                CurseRemovalCondition<?> removalCondition = curse.removalCondition;
                if (removalCondition.statType.equals(stat.getType())) {
                    removalCondition.fulfill(amount);
                }
            }
        }
    }

    public void removeFulfilledCurses() {
        for (ArrayList<Curse<?>> curses : curseArrayLists) { // TODO fix warning
            for (Curse<?> curse : curses) {
                if (curse.removalCondition.isFulfilled()) {
                    curse.disable();
                }
            }
        }
    }

    // Get random curse
    // Get random curse, but weight chances based on difficulty
    // Get random curse, but weight chances to get close to a difficulty
}
