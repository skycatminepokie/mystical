package com.skycat.mystical.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.Spell;
import com.skycat.mystical.spell.Spells;
import com.skycat.mystical.spell.consequence.ConsequenceFactory;
import com.skycat.mystical.util.Utils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;

import java.util.ArrayList;

public class SpellCommandHandler {
    protected static final SimpleCommandExceptionType NO_SPELLS_TO_DELETE_EXCEPTION = new SimpleCommandExceptionType(Utils.translatable("text.mystical.command.mystical.spell.delete.noSpells"));
    protected static final DynamicCommandExceptionType SPELL_DOES_NOT_EXIST_EXCEPTION = new DynamicCommandExceptionType((spellNum) -> Utils.textOf("Spell #" + spellNum + " does not exist (must be from 0 - " + (Mystical.getSpellHandler().getActiveSpells().size() - 1) + ")"));

    protected static int deleteSpellAllCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (Mystical.getSpellHandler().getActiveSpells().isEmpty()) {
            throw NO_SPELLS_TO_DELETE_EXCEPTION.create();
        }
        int spellsDeleted = Mystical.getSpellHandler().removeAllSpells();
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.spell.delete.success", spellsDeleted), true);
        return Command.SINGLE_SUCCESS;
    }

    protected static int deleteSpellWithArgCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int spellNum = context.getArgument("spell", Integer.class);
        if (Mystical.getSpellHandler().getActiveSpells().isEmpty()) {
            throw NO_SPELLS_TO_DELETE_EXCEPTION.create();
        }
        if (spellNum > Mystical.getSpellHandler().getActiveSpells().size()) {
            throw SPELL_DOES_NOT_EXIST_EXCEPTION.create(spellNum);
        }
        Mystical.getSpellHandler().removeSpell(spellNum);
        Mystical.saveUpdated();
        return Command.SINGLE_SUCCESS;
    }

    protected static int deleteSpellsCommand(CommandContext<ServerCommandSource> context) {
        return sendSpellList(context, true);
    }

    protected static int listSpellsCommand(CommandContext<ServerCommandSource> context) {
        return sendSpellList(context, false);
    }

    protected static int newRandomSpellCommand(CommandContext<ServerCommandSource> context) {
        Mystical.getSpellHandler().activateNewSpell();
        Utils.log(Utils.translateString("text.mystical.logging.newSpellCommand"), Mystical.CONFIG.newSpellCommandLogLevel());
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.spell.new.success", "random"), Mystical.CONFIG.newSpellCommandBroadcast());
        return Command.SINGLE_SUCCESS;
    }

    protected static int newSpellCommand(CommandContext<ServerCommandSource> context) {
        String spell = context.getArgument("spell", String.class);
        Utils.log(Utils.translateString("text.mystical.logging.newSpellCommand"), Mystical.CONFIG.newSpellCommandLogLevel());
        ConsequenceFactory<?> factory = Spells.getShortNameToFactory().get(spell);
        if (factory.getWeight() == 0) {
            context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.spell.new.spell.warnDisabled"), false);
        }
        Mystical.getSpellHandler().activateNewSpellWithConsequence(factory);
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.spell.new.success", spell), Mystical.CONFIG.newSpellCommandBroadcast());
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Sends a list of active spells to the command source.
     *
     * @param context          The context in which the command is being called
     * @param showDeleteButton Whether to include the delete button (true). This works even if the context has no permission to delete spells.
     * @return Success (1)/failure (0)
     */
    private static int sendSpellList(CommandContext<ServerCommandSource> context, boolean showDeleteButton) {
        ArrayList<Spell> activeSpells = Mystical.getSpellHandler().getActiveSpells();
        if (activeSpells.size() == 0) {
            context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.spell.list.noSpells"), false);
            return 1;
        }
        for (int i = 0; i < activeSpells.size(); i++) {
            Spell spell = activeSpells.get(i);
            MutableText spellDescription = spell.getConsequence().getFactory().getDescriptionText(spell.getConsequence()); // getDescriptionText should never throw IllegalArgumentException if consequence has a factory that makes itself (should be so for all)
            if (showDeleteButton) {
                MutableText deleteButton = Utils.translatable("text.mystical.command.mystical.spell.delete.deleteButton");
                Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mystical spell delete " + i));
                deleteButton.setStyle(style);
                spellDescription.append(deleteButton);
            }
            spellDescription.setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, spell.getCure().getDescription())));
            context.getSource().sendFeedback(() -> spellDescription, false);
        }
        return Command.SINGLE_SUCCESS;
    }

    protected static int helpCommand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.spell.help",
                Utils.mutableTextOf("/mystical spell list").setStyle(MysticalCommandHandler.MYSTICAL_SPELL_LIST_CLICKABLE),
                Utils.mutableTextOf("/mystical power help").setStyle(MysticalCommandHandler.MYSTICAL_POWER_HELP_CLICKABLE)), false);
        return Command.SINGLE_SUCCESS;
    }
}
