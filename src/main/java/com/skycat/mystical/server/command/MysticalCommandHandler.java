package com.skycat.mystical.server.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.Spell;
import com.skycat.mystical.common.spell.SpellGenerator;
import com.skycat.mystical.common.spell.SpellHandler;
import com.skycat.mystical.common.util.Utils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MysticalCommandHandler implements CommandRegistrationCallback {

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                literal("mystical")
                        .requires(Permissions.require("mystical.command.mystical", true))
                        .then(literal("spell")
                                .requires(Permissions.require("mystical.command.mystical.spell", true))
                                .then(literal("new")
                                        .requires(Permissions.require("mystical.command.mystical.spell.new", 4))
                                        .then(argument("spell", StringArgumentType.word())
                                                .suggests(((context, builder) -> CommandSource.suggestMatching(SpellGenerator.getShortNameToFactory().keySet(), builder)))
                                                .executes(this::newSpellCommand))
                                        .executes(this::newRandomSpellCommand)
                                )
                                .then(literal("list")
                                        .requires(Permissions.require("mystical.command.mystical.spell.list", true))
                                        .executes(this::listSpellsCommand)
                                )
                                .then(literal("delete")
                                        .requires(Permissions.require("mystical.command.mystical.spell.delete", 4))
                                        .then(argument("spell", IntegerArgumentType.integer(0))
                                                .executes(this::deleteSpellWithArgCommand))
                                        .executes(this::deleteSpellsCommand)
                                )
                        )
                        .then(literal("reload")
                                .requires(Permissions.require("mystical.command.mystical.reload", 4))
                                .executes(this::reloadCommand)
                        )
                        .then(literal("haven")
                                .requires(Permissions.require("mystical.command.mystical.haven.haven", 0))
                                // then pos arg
                                .executes(this::havenCommand)
                        )
        );
    }

    private int havenCommand(CommandContext<ServerCommandSource> context) {
        var entity = context.getSource().getEntity();
        if (!(entity instanceof ServerPlayerEntity)) {
            context.getSource().sendFeedback(Utils.textOf("This can only be called by a player."), false); // TODO: Translate
            return 0;
        }
        if (Mystical.HAVEN_MANAGER.isInHaven(entity)) {
            context.getSource().sendFeedback(Utils.textOf("You're already in a haven!"), false); // TODO: Translate
            return 0;
        }
        ChunkPos pos = entity.getChunkPos();
        ((ServerPlayerEntity) entity).sendMessage(
                Utils.mutableTextOf("Having chunk [" + pos.x + ", " + pos.z + "] for " + Mystical.HAVEN_MANAGER.getHavenCost(pos) + " power. ")
                        .append(Utils.mutableTextOf("[Confirm]").setStyle(
                                Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mystical haven " + pos.x + " " + pos.z + " confirm"))
                                        .withColor(Formatting.GREEN)
                        ))
        ); // TODO: Translate
        return 1;
    }

    private int deleteSpellWithArgCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int spellNum = context.getArgument("spell", Integer.class);
        if (Mystical.SPELL_HANDLER.getActiveSpells().isEmpty()) {
            throw new CommandException(Utils.translatable("text.mystical.command.mystical.spell.delete.noSpells"));
        }
        if (spellNum > Mystical.SPELL_HANDLER.getActiveSpells().size()) { // TODO: Translate
            throw new CommandException(Utils.textOf("Spell #" + spellNum + " does not exist (must be from 0 - " + (Mystical.SPELL_HANDLER.getActiveSpells().size() - 1) + ")"));
        }
        Mystical.SPELL_HANDLER.getActiveSpells().remove(spellNum);
        return Command.SINGLE_SUCCESS;
    }

    private int listSpellsCommand(CommandContext<ServerCommandSource> context) {
        return sendSpellList(context, false);
    }

    private int deleteSpellsCommand(CommandContext<ServerCommandSource> context) {
        return sendSpellList(context, true);
    }

    /**
     * Sends a list of active spells to the command source.
     * @param context The context in which the command is being called
     * @param showDeleteButton Whether to include the delete button (true). This works even if the context has no permission to delete spells.
     * @return Success (1)/failure (0)
     */
    private int sendSpellList(CommandContext<ServerCommandSource> context, boolean showDeleteButton) {
        ArrayList<Spell> activeSpells = Mystical.SPELL_HANDLER.getActiveSpells();
        for (int i = 0; i < activeSpells.size(); i++) {
            Spell spell = activeSpells.get(i);
            MutableText spellDescription = spell.getConsequence().getFactory().getDescriptionText(spell.getConsequence()); // TODO: getDescriptionText should never throw IllegalArgumentException
            if (showDeleteButton) {
                MutableText deleteButton = Utils.translatable("text.mystical.commands.deleteSpellButton");
                Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mystical spell delete " + i));
                deleteButton.setStyle(style);
                spellDescription.append(deleteButton);
            }
            spellDescription.setStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, spell.getCure().getDescription()))); // TODO: Translate
            context.getSource().sendFeedback(spellDescription, false);
        }
        return 1;
    }

    private int newSpellCommand(CommandContext<ServerCommandSource> context) {
        String spell = context.getArgument("spell", String.class); // TODO: Warn if disabled or weight = 0
        Utils.log(Utils.translateString("text.mystical.logging.newSpellCommand"), Mystical.CONFIG.newSpellCommandLogLevel());
        Mystical.SPELL_HANDLER.activateNewSpellWithConsequence(SpellGenerator.getShortNameToFactory().get(spell));
        context.getSource().sendFeedback(Utils.translatable("text.mystical.command.mystical.spell.new.success", spell), Mystical.CONFIG.newSpellCommandBroadcast());
        return 1;
    }

    private int newRandomSpellCommand(CommandContext<ServerCommandSource> context) {
        Mystical.SPELL_HANDLER.activateNewSpell();
        Utils.log(Utils.translateString("text.mystical.logging.newSpellCommand"), Mystical.CONFIG.newSpellCommandLogLevel());
        context.getSource().sendFeedback(Utils.translatable("text.mystical.command.mystical.spell.new.success", "random"), Mystical.CONFIG.newSpellCommandBroadcast());
        return 1;
    }

    private int reloadCommand(CommandContext<ServerCommandSource> context) {
        Mystical.SPELL_HANDLER = SpellHandler.loadOrNew();
        Mystical.EVENT_HANDLER.setNightTimer();
        context.getSource().sendFeedback(Utils.translatable("text.mystical.command.mystical.reload.success"), true);
        return 1;
    }

    int testCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Mystical.SPELL_HANDLER.removeAllSpells();
        Mystical.SPELL_HANDLER.activateNewSpell();
        context.getSource().sendFeedback(Utils.textOf("Removed all spells and added a new one"), false);
        return 0;
    }
}
