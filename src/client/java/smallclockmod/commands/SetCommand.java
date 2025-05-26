package smallclockmod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import smallclockmod.ClientMessages;
import smallclockmod.config.ConfigManager;
import smallclockmod.config.Keys;

public class SetCommand implements IClientCommand {
    @Override
    public ClientCmd build() {
        return new ClientCmd(
                ClientCmd.baseCommand
                        .then(ClientCommandManager.literal("set")
                                .then(ClientCommandManager.literal(Keys.DISPLAY_NAME)
                                        .then(ClientCommandManager.argument("value", StringArgumentType.string())
                                                .executes(this::executeSetDisplayName)))
                        )
        );
    }

    private int executeSetDisplayName(CommandContext<FabricClientCommandSource> context) {
        String value = StringArgumentType.getString(context, "value");
        ConfigManager.set(Keys.DISPLAY_NAME, value);
        context.getSource().sendFeedback(ClientMessages.New(Keys.DISPLAY_NAME+" set to: " + value));
        return 1;
    }

}
