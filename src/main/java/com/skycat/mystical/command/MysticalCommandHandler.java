package com.skycat.mystical.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.spell.Spells;
import com.skycat.mystical.util.Utils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@SuppressWarnings("SameReturnValue")
public class MysticalCommandHandler implements CommandRegistrationCallback {
    protected static final SimpleCommandExceptionType EXECUTOR_NOT_ENTITY_EXCEPTION = new SimpleCommandExceptionType(Utils.translatable("text.mystical.command.generic.notAnEntity"));
    protected static final SimpleCommandExceptionType EXECUTOR_NOT_PLAYER_EXCEPTION = new SimpleCommandExceptionType(Utils.translatable("text.mystical.command.generic.notAPlayer"));
    protected static final DynamicCommandExceptionType EXECUTOR_NOT_PLAYER_SOLUTION_EXCEPTION = new DynamicCommandExceptionType((solutionString) -> Utils.translatable("text.mystical.command.generic.notAPlayer.solution", solutionString));
    private static final Style CLICKABLE_TEMPLATE_STYLE = Style.EMPTY
            .withHoverEvent(HoverEvent.Action.SHOW_TEXT.buildHoverEvent(Utils.translatable("text.mystical.command.generic.clickToRunTheCommand")))
            .withColor(Formatting.GREEN);
    protected static final Style MYSTICAL_HAVEN_HELP_CLICKABLE = makeClickableCommandStyle("/mystical haven help");
    protected static final Style MYSTICAL_SPELL_HELP_CLICKABLE = makeClickableCommandStyle("/mystical spell help");
    protected static final Style MYSTICAL_POWER_HELP_CLICKABLE =  makeClickableCommandStyle("/mystical power help");
    protected static final Style MYSTICAL_SPELL_LIST_CLICKABLE = makeClickableCommandStyle("/mystical spell list");
    protected static final Style MYSTICAL_HAVEN_CLICKABLE = makeClickableCommandStyle("/mystical haven");

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        var mystical = /*CommandManager.*/literal("mystical")
                .requires(Permissions.require("mystical.command.mystical.help", true))
                .executes(MysticalCommandHandler::helpCommand)
                .build();
        var help = literal("help")
                .requires(Permissions.require("mystical.command.mystical.help", true))
                .executes(MysticalCommandHandler::helpCommand)
                .build();
        var credits = literal("credits")
                .executes(MysticalCommandHandler::creditsCommand)
                .build();
        var spellHelp = literal("help")
                .requires(Permissions.require("mystical.command.mystical.spell.help", true))
                .executes(SpellCommandHandler::helpCommand)
                .build();
        var spell = literal("spell")
                .executes(SpellCommandHandler::helpCommand)
                .build();
        var spellNew = literal("new")
                .requires(Permissions.require("mystical.command.mystical.spell.new", 4))
                .executes(SpellCommandHandler::newRandomSpellCommand)
                .build();
        var spellNewSpell = argument("spell", StringArgumentType.word())
                .suggests(((context, builder) -> CommandSource.suggestMatching(Spells.getShortNameToFactory().keySet(), builder)))
                .executes(SpellCommandHandler::newSpellCommand)
                .build();
        var spellList = literal("list")
                .requires(Permissions.require("mystical.command.mystical.spell.list", true))
                .executes(SpellCommandHandler::listSpellsCommand)
                .build();
        var spellDelete = literal("delete")
                .requires(Permissions.require("mystical.command.mystical.spell.delete", 4))
                .executes(SpellCommandHandler::deleteSpellsCommand)
                .build();
        var spellDeleteSpell = argument("spell", IntegerArgumentType.integer(0))
                .executes(SpellCommandHandler::deleteSpellWithArgCommand)
                .build();
        var spellDeleteAll = literal("all")
                .executes(SpellCommandHandler::deleteSpellAllCommand)
                .build();
        var reload = literal("reload")
                .requires(Permissions.require("mystical.command.mystical.reload", 4))
                .executes(MysticalCommandHandler::reloadCommand)
                .build();
        var haven = literal("haven")
                .requires(Permissions.require("mystical.command.mystical.haven.haven", true))
                .executes(HavenCommandHandler::havenHereCommand)
                .build();
        var havenHelp = literal("help")
                .requires(Permissions.require("mystical.command.mystical.haven.help", true))
                .executes(HavenCommandHandler::havenHelpCommand)
                .build();
        var havenBlock = argument("block", Vec2ArgumentType.vec2())
                .requires(Permissions.require("mystical.command.mystical.haven.haven", true))
                .executes(HavenCommandHandler::havenPosCommand)
                .build();
        var havenBlockConfirm = literal("confirm")
                .requires(Permissions.require("mystical.command.mystical.haven.haven", true))
                .executes(HavenCommandHandler::havenPosConfirmCommand)
                .build();
        var havenInfo = literal("info")
                .requires(Permissions.require("mystical.command.mystical.haven.info", true))
                .executes(HavenCommandHandler::havenInfoCommand)
                .build();
        var power = literal("power")
                .requires(Permissions.require("mystical.command.mystical.power", true))
                .executes(PowerCommandHandler::myPowerCommand)
                .build();
        var powerHelp = literal("help")
                .requires(Permissions.require("mystical.command.mystical.power.help", true))
                .executes(PowerCommandHandler::powerHelpCommand)
                .build();
        var powerAdd = literal("add")
                .requires(Permissions.require("mystical.command.mystical.power.add", 4))
                .build(); // TODO: Usage info
        var powerAddPlayers = argument("players", EntityArgumentType.players())
                .build();
        var powerAddPlayersAmount = argument("amount", IntegerArgumentType.integer(1))
                .requires(Permissions.require("mystical.command.mystical.power.add", 4))
                .executes(PowerCommandHandler::addPowerPlayerAmountCommand)
                .build();
        var powerRemove = literal("remove")
                .requires(Permissions.require("mystical.command.mystical.power.remove", 4))
                .build();
        var powerRemovePlayers = argument("players", EntityArgumentType.players())
                .requires(Permissions.require("mystical.command.mystical.power.remove", 4))
                .build();
        var powerRemovePlayersAmount = argument("amount", IntegerArgumentType.integer(1))
                .requires(Permissions.require("mystical.command.mystical.power.remove", 4))
                .executes(PowerCommandHandler::removePowerPlayerAmountCommand)
                .build();
        var powerGet = literal("get")
                .requires(Permissions.require("mystical.command.mystical.power.get", 3))
                .build();
        var powerGetPlayers = argument("players", GameProfileArgumentType.gameProfile())
                .requires(Permissions.require("mystical.command.mystical.power.get", 3))
                .executes(PowerCommandHandler::getPowerPlayerCommand)
                .build();
        //@formatter:off
        dispatcher.getRoot().addChild(mystical);
            mystical.addChild(help);
            mystical.addChild(credits);
            mystical.addChild(spell);
                spell.addChild(spellHelp);
                spell.addChild(spellList);
                spell.addChild(spellNew);
                    spellNew.addChild(spellNewSpell);
                spell.addChild(spellDelete);
                    spellDelete.addChild(spellDeleteSpell);
                    spellDelete.addChild(spellDeleteAll);
            mystical.addChild(reload);
            mystical.addChild(power);
                power.addChild(powerHelp);
                power.addChild(powerAdd);
                    powerAdd.addChild(powerAddPlayers);
                        powerAddPlayers.addChild(powerAddPlayersAmount);
                power.addChild(powerRemove);
                    powerRemove.addChild(powerRemovePlayers);
                        powerRemovePlayers.addChild(powerRemovePlayersAmount);
                power.addChild(powerGet);
                    powerGet.addChild(powerGetPlayers);
            mystical.addChild(haven);
                // TODO: Haven add, haven remove
                haven.addChild(havenHelp);
                haven.addChild(havenInfo);
                    // TODO: Haven info pos
                haven.addChild(havenBlock);
                    havenBlock.addChild(havenBlockConfirm);
        //@formatter:on

        /*
         TODO: Commands
            /mystical haven info position
            /mystical haven add
            /mystical haven add position
            /mystical haven remove
            /mystical haven remove position
            /mystical haven remove position refund
        */


    }

    private static int helpCommand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.help",
                Utils.mutableTextOf("/mystical spell help").setStyle(MysticalCommandHandler.MYSTICAL_SPELL_HELP_CLICKABLE),
                Utils.mutableTextOf("/mystical haven help").setStyle(MysticalCommandHandler.MYSTICAL_HAVEN_HELP_CLICKABLE),
                Utils.mutableTextOf("/mystical power help")), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int creditsCommand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.credits"), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int reloadCommand(CommandContext<ServerCommandSource> context) {
        Mystical.EVENT_HANDLER.setNightTimer();
        Mystical.CONFIG.load();
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.reload.success"), true);
        return Command.SINGLE_SUCCESS;
    }

    /**
     * Make a clickable command style.
     * @param command The command to run, including "/"
     * @return A new style.
     */
    public static Style makeClickableCommandStyle(String command) {
        return CLICKABLE_TEMPLATE_STYLE.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
    }
}
