package com.skycat.mystical.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.util.Utils;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

@SuppressWarnings("SameReturnValue")
public class PowerCommandHandler {

    protected static int addPowerPlayerAmountCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        int successCount = 0;
        for (ServerPlayerEntity player : players) {
            Mystical.getHavenManager().addPower(player.getUuid(), amount);
            successCount++;
        }
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.power.add.player.amount.success", amount, successCount), true);
        return successCount;
    }

    protected static int getPowerPlayerCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<GameProfile> players = GameProfileArgumentType.getProfileArgument(context, "players");
        int successCount = 0;
        for (GameProfile player : players) {
            int power = Mystical.getHavenManager().getPower(player.getId());
            context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.power.get.player", player.getName(), power), true);
            successCount++;
        }
        return successCount;
    }

    /**
     * /mystical power
     * Sends a message to the source telling them how much power they have.
     *
     * @param context The context - must be a ServerPlayerEntity.
     * @return 1 on success, 0 if the sender is not a ServerPlayerEntity.
     */
    protected static int myPowerCommand(CommandContext<ServerCommandSource> context) {
        if (!(context.getSource().getEntity() instanceof ServerPlayerEntity player)) {
            context.getSource().sendFeedback(Utils.textSupplierOf("This command must be used by a player!"), true);
            return 0;
        }
        player.sendMessage(Utils.textOf("You have " + Mystical.getHavenManager().getPower(player) + " power."));
        return 1;
    }

    protected static int removePowerPlayerAmountCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgumentType.getPlayers(context, "players");
        int amount = IntegerArgumentType.getInteger(context, "amount");
        int successCount = 0;
        for (ServerPlayerEntity player : players) {
            Mystical.getHavenManager().removePower(player.getUuid(), amount);
            successCount++;
        }
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.power.remove.player.amount.success", amount, successCount), true);
        return successCount;
    }

    protected static int powerHelpCommand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.power.help",
                Utils.mutableTextOf("/mystical spell help").setStyle(MysticalCommandHandler.MYSTICAL_SPELL_HELP_CLICKABLE),
                Utils.mutableTextOf("/mystical haven help").setStyle(MysticalCommandHandler.MYSTICAL_HAVEN_HELP_CLICKABLE)), false);
        return Command.SINGLE_SUCCESS;
    }
}
