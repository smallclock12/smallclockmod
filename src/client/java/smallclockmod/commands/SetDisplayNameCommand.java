package smallclockmod.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import smallclockmod.ClientMessages;
import smallclockmod.config.ConfigManager;

public class SetDisplayNameCommand implements IClientCommand {
    @Override
    public ClientCmd build() {
        return new ClientCmd(
                ClientCmd.baseCommand()
                        .then(ClientCommandManager.literal("set")
                                .then(ClientCommandManager.literal("displayname")
                                        .then(ClientCommandManager.argument("value", StringArgumentType.string())
                                            .executes(this::execute)))));
    }

    private int execute(CommandContext<FabricClientCommandSource> context) {
        String value = StringArgumentType.getString(context, "value");
        ConfigManager.set("DisplayName", value);
        context.getSource().sendFeedback(Text.literal("DisplayName updated. You will need to reload for this to take effect."));
        return 1;
    }

}
