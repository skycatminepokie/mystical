package skycat.mystical.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;

public class TestCommand {
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return 0;
    }
}
