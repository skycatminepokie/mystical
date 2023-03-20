package skycat.mystical.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import skycat.mystical.Mystical;
import skycat.mystical.spell.Spell;
import skycat.mystical.spell.SpellGenerator;
import skycat.mystical.spell.SpellHandler;
import skycat.mystical.util.Utils;

import java.util.ArrayList;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MysticalCommandHandler implements CommandRegistrationCallback {

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                literal("mystical")
                        .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4))
                        .then(literal("spell")
                                .then(literal("new")
                                        .then(argument("spell", StringArgumentType.word())
                                                .suggests(((context, builder) -> CommandSource.suggestMatching(SpellGenerator.getShortNameToFactory().keySet(), builder)))
                                                .executes(this::newSpellCommand))
                                )
                                .then(literal("list")
                                        .executes(this::listSpellsCommand)
                                )
                                .then(literal("delete")
                                        .then(argument("spell", IntegerArgumentType.integer(0))
                                                .executes(this::deleteSpellWithArgCommand))
                                        .executes(this::deleteSpellsCommand)
                                )
                        )
                        .then(literal("reload")
                                .executes(this::reloadCommand)
                        )
        );
    }

    private int deleteSpellWithArgCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int spellNum = context.getArgument("spell", Integer.class);
        if (spellNum > Mystical.SPELL_HANDLER.getActiveSpells().size() || Mystical.SPELL_HANDLER.getActiveSpells().isEmpty()) { // TODO Fix error text for empty list
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
            MutableText spellDescription = spell.getConsequence().getDescriptionText();
            if (showDeleteButton) {
                MutableText deleteButton = Utils.translatable("text.mystical.commands.deleteSpellButton");
                // TODO: Test
                Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/mystical spell delete " + i));
                deleteButton.setStyle(style);
                spellDescription.append(deleteButton);
            }
            // TODO: Give as multiple lines in one message or as multiple messages?
            context.getSource().sendFeedback(spellDescription, false);
        }
        return 1;
    }

    private int newSpellCommand(CommandContext<ServerCommandSource> context) {
        String spell = context.getArgument("spell", String.class);
        Mystical.LOGGER.info("Eyy-o: " + spell); // TODO: Logging // TODO: Config // WARN: Debug
        Mystical.SPELL_HANDLER.activateNewSpellWithConsequence(SpellGenerator.getShortNameToFactory().get(spell));
        context.getSource().sendFeedback(Utils.translatable("text.mystical.commands.newSpell.success"), false); // TODO: Tell what spell was created
        return 1;
    }

    private int reloadCommand(CommandContext<ServerCommandSource> context) {
        Mystical.SPELL_HANDLER = SpellHandler.loadOrNew();
        context.getSource().sendFeedback(Utils.translatable("text.mystical.commands.reload.success"), false);
        return 1;
    }

    int testCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Mystical.SPELL_HANDLER.removeAllSpells();
        Mystical.SPELL_HANDLER.activateNewSpell();
        context.getSource().sendFeedback(Utils.textOf("Removed all spells and added a new one"), false);
        return 0;
    }
}
