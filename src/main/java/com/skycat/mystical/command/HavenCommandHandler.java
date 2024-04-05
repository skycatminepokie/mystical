package com.skycat.mystical.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.util.Utils;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec2f;

public class HavenCommandHandler {
    private static final SimpleCommandExceptionType ALREADY_HAVENED_EXCEPTION = new SimpleCommandExceptionType(Utils.translatable("text.mystical.command.generic.alreadyHavened"));
    private static final SimpleCommandExceptionType HAVEN_NOT_ENOUGH_POWER_EXCEPTION = new SimpleCommandExceptionType(Utils.translatable("text.mystical.command.mystical.haven.pos.confirm.notEnoughPower"));

    /**
     * /mystical haven
     * Redirects to {@link HavenCommandHandler#havenPos(CommandContext, BlockPos)} with the entity's position
     * Source must be an entity (a player entity for the redirect to pass)
     *
     * @param context The context (fails if source is not ServerPlayerEntity)
     * @return The return of {@link HavenCommandHandler#havenPos(CommandContext, BlockPos)}
     */
    protected static int havenHereCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var entity = context.getSource().getEntity();
        if (entity == null) {
            throw MysticalCommandHandler.EXECUTOR_NOT_PLAYER_SOLUTION_EXCEPTION.create("/mystical haven add");
        }
        BlockPos pos = entity.getBlockPos();
        return havenPos(context, pos);
    }

    /**
     * /mystical info
     * Returns feedback telling if the executing entity is in a haven.
     *
     * @return 1 if the entity is in a haven, 0 otherwise.
     */
    protected static int havenInfoCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntity();
        if (entity == null) {
            throw MysticalCommandHandler.EXECUTOR_NOT_ENTITY_EXCEPTION.create();
        }
        if (Mystical.getHavenManager().isInHaven(entity)) {
            context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.haven.info.inHaven"), false);
            return 1;
        }
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.haven.info.notInHaven"), false);
        return 0;
    }

    /**
     * Sends a chat message prompting /mystical haven x z confirm
     * Note this is not a Command. See {@link #havenPosCommand}
     *
     * @param context Context - fails if source is not ServerPlayerEntity
     * @param block   The position to haven
     * @return 1 When the message is successfully sent
     */
    private static int havenPos(CommandContext<ServerCommandSource> context, BlockPos block) throws CommandSyntaxException {
        var entity = context.getSource().getEntity();
        if (!(entity instanceof ServerPlayerEntity player)) {
            throw MysticalCommandHandler.EXECUTOR_NOT_PLAYER_SOLUTION_EXCEPTION.create("/mystical haven add");
        }

        player.sendMessage(
                Utils.translatable("text.mystical.command.mystical.haven.pos.action", block.getX(), block.getZ(), Mystical.getHavenManager().getHavenCost(block))
                        .append(Utils.translatable("text.mystical.command.mystical.haven.pos.button").setStyle(MysticalCommandHandler.makeClickableCommandStyle("/mystical haven " + block.getX() + " " + block.getZ() + " confirm")))
        );
        return 1;
    }

    /**
     * /mystical haven x z
     *
     * @param context The context. Source must be ServerPlayerEntity
     * @return The return value of {@link #havenPos}.
     */
    protected static int havenPosCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var entity = context.getSource().getEntity();
        if (!(entity instanceof ServerPlayerEntity)) { // Also deals with null entity
            throw MysticalCommandHandler.EXECUTOR_NOT_PLAYER_SOLUTION_EXCEPTION.create("/mystical haven add");
        }
        Vec2f vec = Vec2ArgumentType.getVec2(context, "block");
        return havenPos(context, new BlockPos((int) vec.x, 0, (int) vec.y));
    }

    /**
     * /mystical haven x z confirm - Havens a chunk at the player's expense.
     * This is the end case for havening
     *
     * @param context The context. Source must be a player
     * @return 1 if successful, 0 if unsuccessful
     */
    protected static int havenPosConfirmCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var entity = context.getSource().getEntity();
        // Must be player
        if (!(entity instanceof ServerPlayerEntity player)) { // OK now defining player in an instanceof? That's cool.
            throw MysticalCommandHandler.EXECUTOR_NOT_PLAYER_SOLUTION_EXCEPTION.create("/mystical haven add");
        }

        var vec = Vec2ArgumentType.getVec2(context, "block");
        BlockPos blockPos = new BlockPos((int) vec.x, 0, (int) vec.y);
        if (!Mystical.isClientWorld() && Mystical.getHavenManager().isInHaven(player)) { // Must not already be havened. This check may not be needed, since we've got a ServerCommandSource?
            throw ALREADY_HAVENED_EXCEPTION.create();
        }

        ChunkPos chunk = new ChunkPos(blockPos);
        boolean success = Mystical.getHavenManager().tryHaven(chunk, player);
        if (success) {
            player.sendMessage(Utils.translatable("text.mystical.command.generic.success"));
            return 1;
        } else {
            throw HAVEN_NOT_ENOUGH_POWER_EXCEPTION.create();
        }
    }

    protected static int havenHelpCommand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.haven.help",
                Utils.mutableTextOf("/mystical haven").setStyle(MysticalCommandHandler.MYSTICAL_HAVEN_CLICKABLE),
                Utils.mutableTextOf("/mystical power help").setStyle(MysticalCommandHandler.MYSTICAL_POWER_HELP_CLICKABLE),
                Utils.mutableTextOf("/mystical spell help").setStyle(MysticalCommandHandler.MYSTICAL_SPELL_HELP_CLICKABLE)), false);
        return Command.SINGLE_SUCCESS;
    }
}
