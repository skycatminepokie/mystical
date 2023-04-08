package com.skycat.mystical.spell;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import com.skycat.mystical.spell.consequence.SpellConsequence;
import com.skycat.mystical.spell.cure.StatBackedSpellCure;
import com.skycat.mystical.util.Utils;
import lombok.Getter;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

import static com.skycat.mystical.Mystical.GSON;

public class SpellHandler implements EntitySleepEvents.StartSleeping,
        EntitySleepEvents.StopSleeping,
        PlayerBlockBreakEvents.Before,
        PlayerBlockBreakEvents.After,
        ServerPlayerEvents.AfterRespawn,
        ServerEntityCombatEvents.AfterKilledOtherEntity {
    private static final File SAVE_FILE = new File("config/spellHandler.json");
    @Getter private final ArrayList<Spell> activeSpells = new ArrayList<>();

    public static SpellHandler loadOrNew() {
        try (Scanner scanner = new Scanner(SAVE_FILE)) {
            return GSON.fromJson(scanner.nextLine(), SpellHandler.class);
        } catch (FileNotFoundException e) {
            Utils.log(Utils.translateString("text.mystical.spellHandler.loadFailed"), Mystical.CONFIG.failedToLoadSpellHandlerLogLevel());
            return new SpellHandler();
        }
    }

    public boolean isConsequenceActive(Class<? extends SpellConsequence> consequence) {
        return !spellsOfConsequenceType(consequence).isEmpty();
    }

    /**
     * Used for finding active spells with a particular consequence type.
     * This is not the same as a handler.
     * @param clazz The class to check for
     * @return An ArrayList of matching spells
     * @see SpellHandler#spellsOfHandler
     */
    public ArrayList<Spell> spellsOfConsequenceType(Class<? extends SpellConsequence> clazz) {
        ArrayList<Spell> results = new ArrayList<>();
        for (Spell spell : activeSpells) {
            if (clazz.isAssignableFrom(spell.getConsequence().getClass())) {
                results.add(spell);
            }
        }
        return results;
    }

    public void activateNewSpell() {
        activeSpells.add(SpellGenerator.get());
    }

    public void activateNewSpellWithConsequence(ConsequenceFactory<?> consequenceFactory) {
        activeSpells.add(SpellGenerator.getWithConsequence(consequenceFactory));
    }

    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        for (Spell spell : spellsOfHandler(PlayerBlockBreakEvents.After.class)) {
            ((PlayerBlockBreakEvents.After) spell.getConsequence()).afterBlockBreak(world, player, pos, state, blockEntity);
        }
    }

    @Override
    public void afterKilledOtherEntity(ServerWorld world, Entity entity, LivingEntity killedEntity) {
        for (Spell spell : spellsOfHandler(ServerEntityCombatEvents.AfterKilledOtherEntity.class)) {
            ((ServerEntityCombatEvents.AfterKilledOtherEntity) spell.getConsequence()).afterKilledOtherEntity(world, entity, killedEntity);
        }
    }

    /**
     * Applies spells of type {@link ServerPlayerEvents.AfterRespawn}
     * @param oldPlayer the old player
     * @param newPlayer the new player
     * @param alive whether the old player is still alive
     */
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        for (Spell spell : spellsOfHandler(ServerPlayerEvents.AfterRespawn.class)) {
            ((ServerPlayerEvents.AfterRespawn) spell.getConsequence()).afterRespawn(oldPlayer, newPlayer, alive);
        }
    }

    /**
     * Applies spells of type {@link PlayerBlockBreakEvents.Before}
     * @param world the world in which the block is broken
     * @param player the player breaking the block
     * @param pos the position at which the block is broken
     * @param state the block state <strong>before</strong> the block is broken
     * @param blockEntity the block entity <strong>before</strong> the block is broken, can be {@code null}
     * @return {@code false} to cancel the breaking, {@code true} to leave it alone.
     * @implNote All spells will be run, even if it is cancelled in one of them.
     */
    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        // TODO Make options for when there are collisions
        boolean returnValue = true;
        for (Spell spell : spellsOfHandler(PlayerBlockBreakEvents.Before.class)) {
            // Keep these in order. This way, the consequence triggers, even if returnValue is false. Otherwise, it gets short-circuited.
            returnValue = ((PlayerBlockBreakEvents.Before) spell.getConsequence()).beforeBlockBreak(world, player, pos, state, blockEntity) && returnValue  ;
        }
        return returnValue;
    }

    @Override
    public void onStartSleeping(LivingEntity entity, BlockPos sleepingPos) {
        for (Spell spell : spellsOfHandler(EntitySleepEvents.StartSleeping.class)) {
            ((EntitySleepEvents.StartSleeping) spell.getConsequence()).onStartSleeping(entity, sleepingPos);
        }
    }

    public <T> void onStatIncreased(PlayerEntity player, Stat<T> stat, int amount) {
        for (Spell spell : spellsOfStatCure(stat)) {
            spell.getCure().contribute(player.getUuid(), amount);
        }
    }

    @Override
    public void onStopSleeping(LivingEntity entity, BlockPos sleepingPos) {
        for (Spell spell : spellsOfHandler(EntitySleepEvents.StopSleeping.class)) {
            ((EntitySleepEvents.StopSleeping) spell.getConsequence()).onStopSleeping(entity, sleepingPos);
        }
    }

    public void removeAllSpells() {
        activeSpells.clear();
    }

    public void save() {
        try (PrintWriter pw = new PrintWriter(SAVE_FILE)) {
            pw.println(GSON.toJson(this));
        } catch (IOException e) {
            // TODO: Logging
        }
    }

    /**
     * Get all active spells that are using a specified handler
     *
     * @param clazz The event handler to search for
     * @param <T>   The type of handlers to return
     * @return An ArrayList of spells that have matching consequences
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

    /**
     * Removes all spells that have met their cure condition
     * @return The number of spells cured
     */
    public int removeCuredSpells() {
        ListIterator<Spell> li = activeSpells.listIterator();
        int removed = 0;
        while (li.hasNext()) {
            if (li.next().getCure().isSatisfied()) {
                li.remove();
                removed ++;
            }
        }
        return removed;
    }
}