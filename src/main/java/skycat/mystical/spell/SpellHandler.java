package skycat.mystical.spell;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import skycat.mystical.Mystical;
import skycat.mystical.spell.cure.StatBackedSpellCure;
import skycat.mystical.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static skycat.mystical.Mystical.GSON;

public class SpellHandler implements EntitySleepEvents.StartSleeping, PlayerBlockBreakEvents.After, PlayerBlockBreakEvents.Before {
    private static final File SAVE_FILE = new File("config/spellHandler.json");
    private final ArrayList<Spell> activeSpells = new ArrayList<>();

    public static SpellHandler loadOrNew() {
        try (Scanner scanner = new Scanner(SAVE_FILE)) {
            return GSON.fromJson(scanner.nextLine(), SpellHandler.class);
        }
        catch (FileNotFoundException e) {
            Utils.log(Utils.translateString("text.mystical.spellHandler.loadFailed"), Mystical.CONFIG.failedToLoadSpellHandlerLogLevel());
            return new SpellHandler();
        }
    }

    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        spellsOfHandler(PlayerBlockBreakEvents.After.class).forEach((spell -> ((PlayerBlockBreakEvents.After) spell.getConsequence()).afterBlockBreak(world, player, pos, state, blockEntity)));
    }

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        // TODO Make options for when there are collisions
        boolean shouldCancel = false;
        for (Spell spell : spellsOfHandler(PlayerBlockBreakEvents.Before.class)) {
            // Keep these in order. This way, the consequence triggers, even if shouldCancel is true. Otherwise, it gets short-circuited.
            shouldCancel = ((PlayerBlockBreakEvents.Before) spell.getConsequence()).beforeBlockBreak(world, player, pos, state, blockEntity) || shouldCancel;
        }
        return shouldCancel;
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        for (Spell spell : spellsOfHandler(EntitySleepEvents.StartSleeping.class)) {
            ((EntitySleepEvents.StartSleeping) spell.getConsequence()).onStartSleeping(entity, sleepingPos);
        }
    }

    public void save() {
        try (PrintWriter pw = new PrintWriter(SAVE_FILE)) {
            pw.println(GSON.toJson(this));
        } catch (IOException e) {
            // TODO: Logging
        }
    }

    public void activateNewSpell() {
        activeSpells.add(SpellGenerator.get());
    }

    /**
     * Get all active spells that are using a specified handler
     * @param clazz The event handler to search for
     * @return An ArrayList of spells that have matching consequences
     * @param <T> The type of handlers to return
     */
    public <T> ArrayList<Spell> spellsOfHandler(Class<T> clazz) {
        ArrayList<Spell> results = new ArrayList<>();
        for (Spell spell : activeSpells) {
            if (spell.getConsequence().getCallbackType().equals(clazz)) {
                results.add(spell);
            }
        }
        return results;
    }

    public <T> ArrayList<Spell> spellsOfStatCure(Stat<T> stat) {
        ArrayList<Spell> results = new ArrayList<>();
        for (Spell spell : activeSpells) {
            if (spell.getCure() instanceof StatBackedSpellCure backedSpellCure) {
                if (backedSpellCure.getStatType().equals(stat.getType()) && backedSpellCure.getStat().getValue().equals(stat.getValue())) {
                    results.add(spell);
                }
            }
        }
        return results;
    }

    public <T> void onStatIncreased(PlayerEntity player, Stat<T> stat, int amount) {
        // Utils.log("stat increased: " + stat.getName() + " amount: " + amount);
        for (Spell spell : spellsOfStatCure(stat)) {
            spell.getCure().contribute(player.getUuid(), amount);
        }
    }
}
