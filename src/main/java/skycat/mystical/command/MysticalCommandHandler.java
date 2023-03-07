package skycat.mystical.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
                        .then(literal("spell")
                                .then(literal("new")
                                        .then(argument("spell", StringArgumentType.word())
                                                .suggests(((context, builder) -> CommandSource.suggestMatching(SpellGenerator.consequenceFactoriesHash.keySet(), builder)))
                                                .executes(this::newSpellCommand))
                                )
                                .then(literal("list")
                                        // .then(argument()
                                        //         .executes())
                                        .executes(this::listSpellsCommand)
                                )
                                .then(literal("delete")
                                        .executes(this::deleteSpellsCommand)
                                )
                        )
                        .then(literal("reload")
                                .executes(this::reloadCommand)
                        )
        );
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
            MutableText spellDescription = spell.getConsequence().getDescription();
            if (showDeleteButton) {
                MutableText deleteButton = Utils.translatable("text.mystical.commandHandler.deleteSpellButton");
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
        Mystical.SPELL_HANDLER.activateNewSpellWithConsequence(SpellGenerator.consequenceFactoriesHash.get(spell));
        context.getSource().sendFeedback(Utils.translatable("text.mystical.commands.newSpell.success"), false); // TODO: Translate
        return 1;
    }

    private int reloadCommand(CommandContext<ServerCommandSource> context) {
        Mystical.SPELL_HANDLER = SpellHandler.loadOrNew();
        context.getSource().sendFeedback(Utils.translatable("text.mystical.commands.reload.success"), false); // TODO: Translate
        return 1;
    }

    int testCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Mystical.SPELL_HANDLER.removeAllSpells();
        Mystical.SPELL_HANDLER.activateNewSpell();
        context.getSource().sendFeedback(Utils.textOf("Removed all spells and added a new one"), false);
        return 0;
    }
}
