package smallclockmod.commands;

import com.mojang.brigadier.context.CommandContext;
import smallclockmod.ClientMessages;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ChatClearTestEmptyCommand implements IClientCommand {

    @Override
    public ClientCmd build() {
        return new ClientCmd(
                ClientCmd.baseCommand()
                        .then(ClientCommandManager.literal("cctest")
                                .then(ClientCommandManager.literal("empty")
                                        .executes(this::execute))));
    }

    private int execute(CommandContext<FabricClientCommandSource> context) {
        for (int i = 0; i < 10; i++)
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(" "));

        context.getSource().sendFeedback(ClientMessages.CommandCompleteMessage);
        return 1;
    }

}
