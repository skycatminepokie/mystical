package com.skycat.mystical.server.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.common.spell.Spell;
import com.skycat.mystical.common.spell.Spells;
import com.skycat.mystical.common.spell.consequence.ConsequenceFactory;
import com.skycat.mystical.common.util.Utils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec2f;
import java.util.ArrayList;
import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MysticalCommandHandler implements CommandRegistrationCallback {
    private static final SimpleCommandExceptionType NO_SPELLS_TO_DELETE_EXCEPTION = new SimpleCommandExceptionType(Utils.translatable("text.mystical.command.mystical.spell.delete.noSpells"));
    private static final DynamicCommandExceptionType SPELL_DOES_NOT_EXIST_EXCEPTION = new DynamicCommandExceptionType((spellNum) -> Utils.textOf("Spell #" + spellNum + " does not exist (must be from 0 - " + (Mystical.getSpellHandler().getActiveSpells().size() - 1) + ")"));
    private static final SimpleCommandExceptionType EXECUTOR_NOT_ENTITY_EXCEPTION = new SimpleCommandExceptionType(Utils.textOf("This must be called by an entity.")); // TODO: Translate

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        var mystical = /*CommandManager.*/literal("mystical")
                .requires(Permissions.require("mystical.command.mystical", true))
                // TODO: send some info
                .build();
        var spell = literal("spell")
                .requires(Permissions.require("mystical.command.mystical.spell", true))
                .build();
        var spellNew = literal("new")
                .requires(Permissions.require("mystical.command.mystical.spell.new", 4))
                .executes(this::newRandomSpellCommand)
                .build();
        var spellNewSpell = argument("spell", StringArgumentType.word())
                .suggests(((context, builder) -> CommandSource.suggestMatching(Spells.getShortNameToFactory().keySet(), builder)))
                .executes(this::newSpellCommand)
                .build();
        var spellList = literal("list")
                .requires(Permissions.require("mystical.command.mystical.spell.list", true))
                .executes(this::listSpellsCommand)
                .build();
        var spellDelete = literal("delete")
                .requires(Permissions.require("mystical.command.mystical.spell.delete", 4))
                .executes(this::deleteSpellsCommand)
                .build();
        var spellDeleteSpell = argument("spell", IntegerArgumentType.integer(0))
                .executes(this::deleteSpellWithArgCommand)
                .build();
        var reload = literal("reload")
                .requires(Permissions.require("mystical.command.mystical.reload", 4))
                .executes(this::reloadCommand)
                .build();
        var haven = literal("haven")
                .requires(Permissions.require("mystical.command.mystical.haven.haven", 0))
                .executes(this::havenHereCommand)
                .build();
        var havenBlock = argument("block", Vec2ArgumentType.vec2())
                .requires(Permissions.require("mystical.command.mystical.haven.haven", 0))
                .executes(this::havenPosCommand)
                .build();
        var havenBlockConfirm = literal("confirm")
                .requires(Permissions.require("mystical.command.mystical.haven.haven", 0))
                .executes(this::havenPosConfirmCommand)
                .build();
        var havenInfo = literal("info")
                .requires(Permissions.require("mystical.command.mystical.haven.info", 0))
                .executes(this::havenInfoCommand)
                .build();
        var power = literal("power")
                .requires(Permissions.require("mystical.command.mystical.power", 0))
                .executes(this::myPowerCommand)
                .build();
        var powerAdd = literal("add")
                .requires(Permissions.require("mystical.command.mystical.power.add", 4))
                .build(); // TODO: Usage info
        var powerAddPlayers = argument("players", EntityArgumentType.players())
                .build();
        var powerAddPlayersAmount = argument("amount", IntegerArgumentType.integer(1))
                .requires(Permissions.require("mystical.command.mystical.power.add", 4))
                .executes(this::addPowerPlayerAmountCommand)
                .build();
        var powerRemove = literal("remove")
                .requires(Permissions.require("mystical.command.mystical.power.remove", 4))
                .build();
        var powerRemovePlayers = argument("players", EntityArgumentType.players())
                .requires(Permissions.require("mystical.command.mystical.power.remove", 4))
                .build();
        var powerRemovePlayersAmount = argument("amount", IntegerArgumentType.integer(1))
                .requires(Permissions.require("mystical.command.mystical.power.remove", 4))
                .executes(this::removePowerPlayerAmountCommand)
                .build();
        var powerGet = literal("get")
                .requires(Permissions.require("mystical.command.mystical.power.get", 3))
                // .executes(this::myPowerCommand) // Done by base power command
                .build();
        var powerGetPlayers = argument("players", EntityArgumentType.players())
                .requires(Permissions.require("mystical.command.mystical.power.get", 3))
                .executes(this::getPowerPlayerCommand)
                .build();
        //@formatter:off
        dispatcher.getRoot().addChild(mystical);
            mystical.addChild(spell);
                spell.addChild(spellList);
                spell.addChild(spellNew);
                    spellNew.addChild(spellNewSpell);
                spell.addChild(spellDelete);
                    spellDelete.addChild(spellDeleteSpell);
            mystical.addChild(reload);
            mystical.addChild(power);
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
                    // TODO: Haven pos
                haven.addChild(havenBlock);
                    havenBlock.addChild(havenBlockConfirm);
        //@formatter:on

        // TODO: Haven add, haven remove, haven info pos

        /*
         TODO: Commands
            /mystical power help
            /mystical power ?
            /mystical haven info position
            /mystical haven ?
            /mystical haven add
            /mystical haven add position
            /mystical haven remove
            /mystical haven remove position
            /mystical haven remove position refund
        */


    }

    private int getPowerPlayerCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        int successCount = 0;
        for (ServerPlayerEntity player : players) {
            int power = Mystical.getHavenManager().getPower(player);
            context.getSource().sendFeedback(() -> player.getDisplayName().copy().append(Utils.textOf(" has " + power + " power.")), true); // TODO: Translate TODO: Config
            successCount++;
        }
        return successCount;
    }

    private int removePowerPlayerAmountCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        int successCount = 0;
        for (ServerPlayerEntity player : players) {
            Mystical.getHavenManager().removePower(player.getUuid(), amount);
            successCount++;
        }
        context.getSource().sendFeedback(Utils.textSupplierOf("Successfully removed " + amount + " power from " + successCount + " players."), true); // TODO: Translate
        return successCount;
    }

    private int addPowerPlayerAmountCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        int successCount = 0;
        for (ServerPlayerEntity player : players) {
            Mystical.getHavenManager().addPower(player.getUuid(), amount);
            successCount++;
        }
        context.getSource().sendFeedback(Utils.textSupplierOf("Successfully added " + amount + " power to " + successCount + " players."), true); // TODO: Translate
        return successCount;
    }

    /**
     * /mystical power
     * Sends a message to the source telling them how much power they have.
     * @param context The context - must be a ServerPlayerEntity.
     * @return 1 on success, 0 if the sender is not a ServerPlayerEntity.
     */
    private int myPowerCommand(CommandContext<ServerCommandSource> context) {
        if (!(context.getSource().getEntity() instanceof ServerPlayerEntity player)) {
            context.getSource().sendFeedback(Utils.textSupplierOf("This command must be used by a player!"), true);
            return 0;
        }
        player.sendMessage(Utils.textOf("You have " + Mystical.getHavenManager().getPower(player) + " power."));
        return 1;
    }

    /**
     * /mystical info
     * Returns feedback telling if the executing entity is in a haven.
     * @return 1 if the entity is in a haven, 0 otherwise.
     */
    private int havenInfoCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntity();
        if (entity == null) {
            throw EXECUTOR_NOT_ENTITY_EXCEPTION.create();
        }
        if (!Mystical.isClientWorld() && Mystical.getHavenManager().isInHaven(entity)) { // TODO: Translate, make better
            context.getSource().sendFeedback(Utils.textSupplierOf("This is in a haven"), false);
            return 1;
        }
        context.getSource().sendFeedback(Utils.textSupplierOf("This chunk is not havened"), false);
        return 0;
    }

    /**
     * /mystical haven
     * Redirects to {@link MysticalCommandHandler#havenPos(CommandContext, BlockPos)} with the entity's position
     * Source must be an entity (a player entity for the redirect to pass)
     *
     * @param context The context (fails if source is not ServerPlayerEntity)
     * @return The return of {@link MysticalCommandHandler#havenPos(CommandContext, BlockPos)}
     */
    private int havenHereCommand(CommandContext<ServerCommandSource> context) {
        var entity = context.getSource().getEntity();
        if (entity == null) {
            context.getSource().sendFeedback(Utils.textSupplierOf("Only a player entity can haven this way! Try /mystical haven add."), true); // This shouldn't be possible by regular players // TODO: Translate
            return 0;
        }
        BlockPos pos = entity.getBlockPos();
        return havenPos(context, pos);
    }

    /**
     * Sends a chat message prompting /mystical haven x z confirm
     * Note this is not a Command. See {@link #havenPosCommand}
     *
     * @param context Context - fails if source is not ServerPlayerEntity
     * @param block   The position to haven
     * @return 1 When the message is successfully send
     */
    private int havenPos(CommandContext<ServerCommandSource> context, BlockPos block) {
        var entity = context.getSource().getEntity();
        if (!(entity instanceof ServerPlayerEntity player)) {
            context.getSource().sendFeedback(Utils.textSupplierOf("Only a player can haven this way! Try /mystical haven add."), true); // This shouldn't be possible by regular players // TODO: Translate
            return 0;
        }
        player.sendMessage(
                Utils.mutableTextOf("Having chunk at [" + block.getX() + ", " + block.getZ() + "] for " + Mystical.getHavenManager().getHavenCost(block) + " power. ")
                        .append(Utils.mutableTextOf("[Confirm]").setStyle(
                                Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mystical haven " + block.getX() + " " + block.getZ() + " confirm"))
                                        .withColor(Formatting.GREEN)
                        ))
        ); // TODO: Translate
        return 1;
    }

    /**
     * /mystical haven x z
     *
     * @param context The context. Source must be ServerPlayerEntity
     * @return The return value of {@link #havenPos}.
     */
    private int havenPosCommand(CommandContext<ServerCommandSource> context) {
        var entity = context.getSource().getEntity();
        if (!(entity instanceof ServerPlayerEntity)) { // Also deals with null entity
            context.getSource().sendFeedback(Utils.textSupplierOf("Only a player entity can haven this way! Try /mystical haven add."), true); // This shouldn't be possible by regular players // TODO: Translate
            return 0;
        }
        var vec = Vec2ArgumentType.getVec2(context, "block");
        return havenPos(context, new BlockPos((int) vec.x, 0, (int) vec.y));
    }

    /**
     * /mystical haven x z confirm - Havens a chunk at the player's expense.
     * This is the end case for havening
     *
     * @param context The context. Source must be a player
     * @return 1 if successful, 0 if unsuccessful
     */
    private int havenPosConfirmCommand(CommandContext<ServerCommandSource> context) {
        var entity = context.getSource().getEntity();
        // Must be player
        if (!(entity instanceof ServerPlayerEntity player)) { // OK now defining player in an instanceof? That's cool.
            context.getSource().sendFeedback(Utils.textSupplierOf("This can only be called by a player."), false); // TODO: Translate
            return 0;
        }

        var vec = Vec2ArgumentType.getVec2(context, "block");
        BlockPos blockPos = new BlockPos((int) vec.x, 0, (int) vec.y);
        if (!Mystical.isClientWorld() && Mystical.getHavenManager().isInHaven(player)) { // Must not already be havened. This check may not be needed, since we've got a ServerCommandSource?
            context.getSource().sendFeedback(Utils.textSupplierOf("That location is already havened."), false); // TODO: Translate
            return 0;
        }

        ChunkPos chunk = new ChunkPos(blockPos);
        boolean success = Mystical.getHavenManager().tryHaven(chunk, player);
        if (success) {
            player.sendMessage(Utils.textOf("Success!")); // TODO: Translate
            return 1;
        } else {
            player.sendMessage(Utils.textOf("You tried as hard as you could, but you couldn't haven the area. Do you have enough power?")); // TODO: Translate
            return 0;
        }
    }


    private int deleteSpellWithArgCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int spellNum = context.getArgument("spell", Integer.class);
        if (Mystical.getSpellHandler().getActiveSpells().isEmpty()) {
            throw NO_SPELLS_TO_DELETE_EXCEPTION.create();
        }
        if (spellNum > Mystical.getSpellHandler().getActiveSpells().size()) {
            throw SPELL_DOES_NOT_EXIST_EXCEPTION.create(spellNum);
        }
        Mystical.getSpellHandler().getActiveSpells().remove(spellNum);
        Mystical.saveUpdated();
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

    private int newSpellCommand(CommandContext<ServerCommandSource> context) {
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

    private int newRandomSpellCommand(CommandContext<ServerCommandSource> context) {
        Mystical.getSpellHandler().activateNewSpell();
        Utils.log(Utils.translateString("text.mystical.logging.newSpellCommand"), Mystical.CONFIG.newSpellCommandLogLevel());
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.spell.new.success", "random"), Mystical.CONFIG.newSpellCommandBroadcast());
        return Command.SINGLE_SUCCESS;
    }

    private int reloadCommand(CommandContext<ServerCommandSource> context) {
        Mystical.EVENT_HANDLER.setNightTimer();
        Mystical.CONFIG.load();
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.reload.success"), true);
        return Command.SINGLE_SUCCESS;
    }
}
