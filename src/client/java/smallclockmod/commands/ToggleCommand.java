package smallclockmod.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import smallclockmod.ClientMessages;
import smallclockmod.config.ConfigManager;
import smallclockmod.config.Toggles;

public class ToggleCommand implements IClientCommand {
    @Override
    public ClientCmd build() {
        return new ClientCmd(
                ClientCmd.baseCommand
                        .then(ClientCommandManager.literal("toggle")
                                .then(ClientCommandManager.literal(Toggles.CHAT_CLEAR_PROTECTION).executes(this::toggleChatClearProtection))
                        ));
    }

    private int toggleChatClearProtection(CommandContext<FabricClientCommandSource> context) {
        var newValue = ConfigManager.toggle(Toggles.CHAT_CLEAR_PROTECTION);
        if (newValue)
            context.getSource().sendFeedback(ClientMessages.New("Toggled "+Toggles.CHAT_CLEAR_PROTECTION+" on"));
        else
            context.getSource().sendFeedback(ClientMessages.New("Toggled "+Toggles.CHAT_CLEAR_PROTECTION+" off"));

        return 1;
    }
}
