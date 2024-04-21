package com.skycat.mystical;

import com.skycat.mystical.accessor.MinecraftServerTimerAccess;
import com.skycat.mystical.spell.SpellHandler;
import com.skycat.mystical.util.Utils;
import lombok.Getter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.Stack;

public class MysticalEventHandler implements ServerWorldEvents.Load, ServerLifecycleEvents.ServerStopping {
    @Getter private MinecraftServer server;
    private MinecraftServerTimerAccess timerAccess;
    public static final int NIGHT_TIME = 18000;

    /**
     * Do all the nighttime events - changing spells and setting the night timer.
     */
    public void doNighttimeEvents(MinecraftServer server) {
        SpellHandler spellHandler = Mystical.getSpellHandler();
        spellHandler.decaySpells();
        int spellsCured = spellHandler.removeCuredSpells(server);
        Stack<Text> messageStack = new Stack<>(); // Done this way because it seemed best to get the spell changing message out on top
        if (spellsCured == 1) {
            messageStack.push(Utils.translatable("text.mystical.events.cureSpell"));
        }
        if (spellsCured > 1) {
            messageStack.push(Utils.translatable("text.mystical.events.cureSpells", spellsCured));
        }
        int newSpells = 0;
        if (spellHandler.getActiveSpells().size() < Mystical.CONFIG.spellMaxHard()) { // Make sure we don't go past the max number of spells
            spellHandler.activateNewSpell();
            newSpells++;
        }
        while (spellHandler.getActiveSpells().size() < Mystical.CONFIG.spellMinHard()) { // Make sure we have the minimum number of spells
            spellHandler.activateNewSpell();
            newSpells++;
        }
        if (newSpells == 1) {
            messageStack.push(Utils.translatable("text.mystical.events.newSpell"));
        }
        if (newSpells > 1) {
            messageStack.push(Utils.translatable("text.mystical.events.newSpells", newSpells));
        }
        if (!messageStack.isEmpty()) {
            messageStack.push(Utils.translatable("text.mystical.events.spellsChange"));
            while (!messageStack.isEmpty()) {
                server.getPlayerManager().broadcast(messageStack.pop(), false);
            }
        }
        setNightTimer();
    }

    @Override
    public void onServerStopping(MinecraftServer server) {
        Mystical.CONFIG.save();
    }

    @Override
    public void onWorldLoad(MinecraftServer server, ServerWorld world) {
        this.server = server;
        timerAccess = ((MinecraftServerTimerAccess) server);
        setNightTimer();
        Utils.log(Utils.translateString("text.mystical.logging.timeOfDayAtStartup", server.getOverworld().getTimeOfDay()), Mystical.CONFIG.timeOfDayAtStartupLogLevel());
    }

    /**
     * Sets the nighttime timer to the next night
     * @return Number of ticks until night, or -1 on failure
     */
    public long setNightTimer() {
        if (server == null) {
            throw new NullPointerException(Utils.translateString("text.mystical.logging.failedToSetNightTimer", "server was null."));
        }
        if (timerAccess == null) {
            throw new NullPointerException(Utils.translateString("text.mystical.logging.failedToSetNightTimer", "timerAccess was null."));
        }
        long timerLength;
        long currentTime = server.getOverworld().getTimeOfDay() % 24000;
        if (currentTime > NIGHT_TIME) { // If we've passed NIGHT_TIME
            timerLength = (24000 - currentTime) + NIGHT_TIME; // (time left in this day) + (time from morning til NIGHT_TIME) = time until tomorrow's NIGHT_TIME
        } else {
            if (currentTime == 0) { // It's midnight
                timerLength = 24000;
            } else { // It's before midnight
                timerLength = NIGHT_TIME - currentTime; // NIGHT_TIME - (current time) = time until midnight
            }
        }

        timerAccess.mystical_setTimer(timerLength);
        return timerLength;
    }
}
