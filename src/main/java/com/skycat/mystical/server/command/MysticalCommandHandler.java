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

import java.util.ArrayList;
import java.util.Collection;

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
                                .then(
                                        argument("block", Vec2ArgumentType.vec2())
                                                .requires(Permissions.require("mystical.command.mystical.haven.haven", 0))
                                                .executes(this::havenPosCommand)
                                                .then(literal("confirm")
                                                        .requires(Permissions.require("mystical.command.mystical.haven.haven", 0))
                                                        .executes(this::havenPosConfirmCommand)
                                                )
                                )
                                .then(literal("info")
                                        .requires(Permissions.require("mystical.command.mystical.haven.info", 0))
                                        .executes(this::havenInfoCommand)
                                ) // TODO: Haven add, haven remove, haven info pos
                                .executes(this::havenHereCommand)
                        )
                        .then(literal("power")
                                .requires(Permissions.require("mystical.command.mystical.power", 0))
                                .then(literal("add")
                                        .requires(Permissions.require("mystical.command.mystical.power.add", 4))
                                        .then(argument("players", EntityArgumentType.players())
                                                .requires(Permissions.require("mystical.command.mystical.power.add", 4))
                                                .then(argument("amount", IntegerArgumentType.integer(1))
                                                        .requires(Permissions.require("mystical.command.mystical.power.add", 4))
                                                        .executes(this::addPowerPlayerAmountCommand)
                                                )
                                        )
                                )
                                .then(literal("remove")
                                        .requires(Permissions.require("mystical.command.mystical.power.remove", 4))
                                        .then(argument("players", EntityArgumentType.players())
                                                .requires(Permissions.require("mystical.command.mystical.power.remove",4))
                                                .then(argument("amount", IntegerArgumentType.integer(1))
                                                        .requires(Permissions.require("mystical.command.mystical.power.remove", 4))
                                                        .executes(this::removePowerPlayerAmountCommand)
                                                )
                                        )
                                )
                                .executes(this::myPowerCommand)
                        )
        );
        /*
         TODO: Commands
            /mystical power get player
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

    private int removePowerPlayerAmountCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        int successCount = 0;
        for (ServerPlayerEntity player : players) {
            Mystical.HAVEN_MANAGER.removePower(player.getUuid(), amount);
            successCount++;
        }
        context.getSource().sendFeedback(Utils.textOf("Successfully removed " + amount + " power from " + successCount + " players."), true); // TODO: Translate
        return successCount;
    }

    private int addPowerPlayerAmountCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        int successCount = 0;
        for (ServerPlayerEntity player : players) {
            Mystical.HAVEN_MANAGER.addPower(player.getUuid(), amount);
            successCount++;
        }
        context.getSource().sendFeedback(Utils.textOf("Successfully added " + amount + " power to " + successCount + " players."), true); // TODO: Translate
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
            context.getSource().sendFeedback(Utils.textOf("This command must be used by a player!"), true);
            return 0;
        }
        player.sendMessage(Utils.textOf("You have " + Mystical.HAVEN_MANAGER.getPower(player) + " power."));
        return 1;
    }

    private int havenInfoCommand(CommandContext<ServerCommandSource> context) {
        Entity entity = context.getSource().getEntity();
        if (entity == null) {
            throw new CommandException(Utils.textOf("/mystical haven info must be called by an entity."));
        }
        if (Mystical.HAVEN_MANAGER.isInHaven(entity)) { // TODO: Translate, make better
            context.getSource().sendFeedback(Utils.textOf("This is in a haven"), false);
            return 1;
        }
        context.getSource().sendFeedback(Utils.textOf("This chunk is not havened"), false);
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
            context.getSource().sendFeedback(Utils.textOf("Only a player entity can haven this way! Try /mystical haven add."), true); // This shouldn't be possible by regular players // TODO: Translate
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
            context.getSource().sendFeedback(Utils.textOf("Only a player can haven this way! Try /mystical haven add."), true); // This shouldn't be possible by regular players // TODO: Translate
            return 0;
        }
        player.sendMessage(
                Utils.mutableTextOf("Having chunk at [" + block.getX() + ", " + block.getZ() + "] for " + Mystical.HAVEN_MANAGER.getHavenCost(block) + " power. ")
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
            context.getSource().sendFeedback(Utils.textOf("Only a player entity can haven this way! Try /mystical haven add."), true); // This shouldn't be possible by regular players // TODO: Translate
            return 0;
        }
        var vec = Vec2ArgumentType.getVec2(context, "block");
        return havenPos(context, new BlockPos(vec.x, 0, vec.y));
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
            context.getSource().sendFeedback(Utils.textOf("This can only be called by a player."), false); // TODO: Translate
            return 0;
        }

        var vec = Vec2ArgumentType.getVec2(context, "block");
        BlockPos blockPos = new BlockPos(vec.x, 0, vec.y);
        if (Mystical.HAVEN_MANAGER.isInHaven(player)) { // Must not already be havened
            context.getSource().sendFeedback(Utils.textOf("That location is already havened."), false); // TODO: Translate
            return 0;
        }

        ChunkPos chunk = new ChunkPos(blockPos);
        boolean success = Mystical.HAVEN_MANAGER.tryHaven(chunk, player);
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
