package com.skycat.mystical.server;

import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.util.Utils;
import lombok.Getter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.util.Stack;

public class MysticalEventHandler implements ServerLifecycleEvents.ServerStarted, ServerLifecycleEvents.ServerStopping {
    @Getter private MinecraftServer server;
    private MinecraftServerTimerAccess timerAccess;

    @Override
    public void onServerStarted(MinecraftServer server) {
        this.server = server;
        timerAccess = ((MinecraftServerTimerAccess) server);
        setNightTimer();
        Utils.log(Utils.translateString("text.mystical.logging.timeOfDayAtStartup", server.getOverworld().getTimeOfDay()), Mystical.CONFIG.timeOfDayAtStartupLogLevel());
    }

    /**
     * Do all the nighttime events - changing spells and setting the night timer.
     * @deprecated Use {@link MysticalEventHandler#doNighttimeEvents(MinecraftServer)} instead.
     */
    @Deprecated(forRemoval = true)
    public void doNighttimeEvents() {
        Mystical.getSpellHandler().removeCuredSpells();
        if (Mystical.getSpellHandler().getActiveSpells().size() < Mystical.CONFIG.spellMaxHard()) { // Make sure we don't go past the max number of spells
            Mystical.getSpellHandler().activateNewSpell();
        }
        while (Mystical.getSpellHandler().getActiveSpells().size() < Mystical.CONFIG.spellMinHard()) { // Make sure we have the minimum number of spells
            Mystical.getSpellHandler().activateNewSpell();
        }
        setNightTimer();
    }

    /**
     * Do all the nighttime events - changing spells and setting the night timer.
     */
    public void doNighttimeEvents(MinecraftServer server) {
        int spellsCured = Mystical.getSpellHandler().removeCuredSpells();
        Stack<Text> messageStack = new Stack<>(); // Done this way because it seemed best to get the spell changing message out on top
        if (spellsCured == 1) {
            messageStack.push(Utils.translatable("text.mystical.events.cureSpell"));
        }
        if (spellsCured > 1) {
            messageStack.push(Utils.translatable("text.mystical.events.cureSpells", spellsCured));
        }
        int newSpells = 0;
        if (Mystical.getSpellHandler().getActiveSpells().size() < Mystical.CONFIG.spellMaxHard()) { // Make sure we don't go past the max number of spells
            Mystical.getSpellHandler().activateNewSpell();
            newSpells++;
        }
        while (Mystical.getSpellHandler().getActiveSpells().size() < Mystical.CONFIG.spellMinHard()) { // Make sure we have the minimum number of spells
            Mystical.getSpellHandler().activateNewSpell();
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
                server.sendMessage(messageStack.pop());
            }
        }
        setNightTimer();
    }

    @Override
    public void onServerStopping(MinecraftServer server) {
        Mystical.CONFIG.save();
    }

    /**
     * Sets the nighttime timer to the next night
     * @return Number of ticks until night, or -1 on failure
     */
    public long setNightTimer() {
        // CREDIT: Daomephsta#0044 for help on fabric discord
        if (server == null) {
            throw new NullPointerException(Utils.translateString("text.mystical.logging.failedToSetNightTimer", "server was null."));
        }
        if (timerAccess == null) {
            throw new NullPointerException(Utils.translateString("text.mystical.logging.failedToSetNightTimer", "timerAccess was null."));
        }
        long timerLength;
        long currentTime = server.getOverworld().getTimeOfDay() % 24000;
        if (currentTime > 18000) { // If we've passed midnight
            timerLength = (24000 - currentTime) + 18000; // (time left in this day) + (time from morning til midnight) = time until tomorrow's midnight
        } else { // It's before midnight
             timerLength = 18000 - currentTime; // midnight - (current time) = time until midnight
        }

        if (timerLength == 0) { // So that we don't get repeating events if this is fired twice in a tick.
            timerLength = 18000;
        }
        timerAccess.mystical_setTimer(timerLength);
        return timerLength;
    }
}
