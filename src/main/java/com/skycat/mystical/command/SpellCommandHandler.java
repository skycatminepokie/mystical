package com.skycat.mystical.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.skycat.mystical.Mystical;
import com.skycat.mystical.util.Utils;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;

public class SpellCommandHandler {
    static final SimpleCommandExceptionType NO_SPELLS_TO_DELETE_EXCEPTION = new SimpleCommandExceptionType(Utils.translatable("text.mystical.command.mystical.spell.delete.noSpells"));
    static final DynamicCommandExceptionType SPELL_DOES_NOT_EXIST_EXCEPTION = new DynamicCommandExceptionType((spellNum) -> Utils.textOf("Spell #" + spellNum + " does not exist (must be from 0 - " + (Mystical.getSpellHandler().getActiveSpells().size() - 1) + ")"));

    static int addPowerPlayerAmountCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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

    static int deleteSpellAllCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (Mystical.getSpellHandler().getActiveSpells().isEmpty()) {
            throw NO_SPELLS_TO_DELETE_EXCEPTION.create();
        }
        int spellsDeleted = Mystical.getSpellHandler().removeAllSpells();
        context.getSource().sendFeedback(Utils.translatableSupplier("text.mystical.command.mystical.spell.delete.success", spellsDeleted), true);
        return Command.SINGLE_SUCCESS;
    }

    static int deleteSpellWithArgCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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

    static int getPowerPlayerCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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
     * @param context The context - must be a ServerPlayerEntity.
     * @return 1 on success, 0 if the sender is not a ServerPlayerEntity.
     */
    static int myPowerCommand(CommandContext<ServerCommandSource> context) {
        if (!(context.getSource().getEntity() instanceof ServerPlayerEntity player)) {
            context.getSource().sendFeedback(Utils.textSupplierOf("This command must be used by a player!"), true);
            return 0;
        }
        player.sendMessage(Utils.textOf("You have " + Mystical.getHavenManager().getPower(player) + " power."));
        return 1;
    }

    static int removePowerPlayerAmountCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
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
}
