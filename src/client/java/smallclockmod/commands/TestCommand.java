package smallclockmod.commands;

import com.mojang.brigadier.context.CommandContext;
import smallclockmod.ClientMessages;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class TestCommand implements IClientCommand {

    @Override
    public ClientCmd build() {
        return new ClientCmd(
                ClientCmd.baseCommand
                        .then(ClientCommandManager.literal("test")
                                .then(ClientCommandManager.literal("cc")
                                    .then(ClientCommandManager.literal("empty").executes(this::executeEmptyTest))
                                    .then(ClientCommandManager.literal("newline").executes(this::executeNewLineTest))
                        ))
        );
    }

    private int executeEmptyTest(CommandContext<FabricClientCommandSource> context) {
        for (int i = 0; i < 10; i++)
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal(" "));

        context.getSource().sendFeedback(ClientMessages.New("Command Executed"));
        return 1;
    }

    private int executeNewLineTest(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("\n\n\n\n\n\n\n"));
        context.getSource().sendFeedback(ClientMessages.New("Command Executed"));
        return 1;
    }


}
