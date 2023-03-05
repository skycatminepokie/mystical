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
import skycat.mystical.Mystical;
import skycat.mystical.spell.SpellGenerator;
import skycat.mystical.spell.SpellHandler;
import skycat.mystical.util.Utils;

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
                                ))
                        .then(literal("reload")
                                .executes(this::reloadCommand)
                        )
        );

        // dispatcher.register(mystical);
    }

    private int newSpellCommand(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        String spell = serverCommandSourceCommandContext.getArgument("spell", String.class);
        Mystical.LOGGER.info("Eyy-o: " + spell); // TODO: Logging // TODO: Config // WARN: Debug
        Mystical.SPELL_HANDLER.activateNewSpellWithConsequence(SpellGenerator.consequenceFactoriesHash.get(spell));
        serverCommandSourceCommandContext.getSource().sendFeedback(Utils.translatable("text.mystical.commands.newSpell.success"), false); // TODO: Translate
        return 1;
    }

    private int reloadCommand(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        Mystical.SPELL_HANDLER = SpellHandler.loadOrNew();
        serverCommandSourceCommandContext.getSource().sendFeedback(Utils.translatable("text.mystical.commands.reload.success"), false); // TODO: Translate
        return 1;
    }

    int testCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Mystical.SPELL_HANDLER.removeAllSpells();
        Mystical.SPELL_HANDLER.activateNewSpell();
        context.getSource().sendFeedback(Utils.textOf("Removed all spells and added a new one"), false);
        return 0;
    }
}
