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

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@SuppressWarnings("SameReturnValue")
public class MysticalCommandHandler implements CommandRegistrationCallback {
    protected static final SimpleCommandExceptionType EXECUTOR_NOT_ENTITY_EXCEPTION = new SimpleCommandExceptionType(Utils.translatable("text.mystical.command.generic.notAnEntity"));
    protected static final SimpleCommandExceptionType EXECUTOR_NOT_PLAYER_EXCEPTION = new SimpleCommandExceptionType(Utils.translatable("text.mystical.command.generic.notAPlayer"));
    protected static final DynamicCommandExceptionType EXECUTOR_NOT_PLAYER_SOLUTION_EXCEPTION = new DynamicCommandExceptionType((solutionString) -> Utils.translatable("text.mystical.command.generic.notAPlayer.solution", solutionString));

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        var mystical = /*CommandManager.*/literal("mystical")
                .requires(Permissions.require("mystical.command.mystical", true))
                // TODO: send some info
                .build();
        var credits = literal("credits")
                .executes(this::creditsCommand)
                .build();
        var spell = literal("spell")
                .requires(Permissions.require("mystical.command.mystical.spell", true))
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
                .executes(this::reloadCommand)
                .build();
        var haven = literal("haven")
                .requires(Permissions.require("mystical.command.mystical.haven.haven", 0))
                .executes(HavenCommandHandler::havenHereCommand)
                .build();
        var havenBlock = argument("block", Vec2ArgumentType.vec2())
                .requires(Permissions.require("mystical.command.mystical.haven.haven", 0))
                .executes(HavenCommandHandler::havenPosCommand)
                .build();
        var havenBlockConfirm = literal("confirm")
                .requires(Permissions.require("mystical.command.mystical.haven.haven", 0))
                .executes(HavenCommandHandler::havenPosConfirmCommand)
                .build();
        var havenInfo = literal("info")
                .requires(Permissions.require("mystical.command.mystical.haven.info", 0))
                .executes(HavenCommandHandler::havenInfoCommand)
                .build();
        var power = literal("power")
                .requires(Permissions.require("mystical.command.mystical.power", 0))
                .executes(PowerCommandHandler::myPowerCommand)
                .build();
        var powerHelp = literal("help")
                .requires(Permissions.require("mystical.command.mystical.power.help", 0))
                .executes(PowerCommandHandler::powerHelpCommand)
                .build();
        var powerQuestionMark = literal("?")
                .redirect(powerHelp)
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
                // .executes(this::myPowerCommand) // Done by base power command
                .build();
        var powerGetPlayers = argument("players", GameProfileArgumentType.gameProfile())
                .requires(Permissions.require("mystical.command.mystical.power.get", 3))
                .executes(PowerCommandHandler::getPowerPlayerCommand)
                .build();
        //@formatter:off
        dispatcher.getRoot().addChild(mystical);
            mystical.addChild(spell);
                spell.addChild(spellList);
                spell.addChild(spellNew);
                    spellNew.addChild(spellNewSpell);
                spell.addChild(spellDelete);
                    spellDelete.addChild(spellDeleteSpell);
                    spellDelete.addChild(spellDeleteAll);
            mystical.addChild(reload);
            mystical.addChild(power);
                power.addChild(powerHelp);
                power.addChild(powerQuestionMark);
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
                haven.addChild(havenInfo);
                    // TODO: Haven info pos
                haven.addChild(havenBlock);
                    havenBlock.addChild(havenBlockConfirm);
        //@formatter:on

        /*
         TODO: Commands
            /mystical haven info position
            /mystical haven ?
            /mystical haven add
            /mystical haven add position
            /mystical haven remove
            /mystical haven remove position
            /mystical haven remove position refund
        */


    }

    private int creditsCommand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.credits"), false);
        return Command.SINGLE_SUCCESS;
    }

    private int reloadCommand(CommandContext<ServerCommandSource> context) {
        Mystical.EVENT_HANDLER.setNightTimer();
        Mystical.CONFIG.load();
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.reload.success"), true);
        return Command.SINGLE_SUCCESS;
    }
}
