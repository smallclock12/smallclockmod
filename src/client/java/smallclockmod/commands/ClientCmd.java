package smallclockmod.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public record ClientCmd(LiteralArgumentBuilder<FabricClientCommandSource> cmd) {
    public static final LiteralArgumentBuilder<FabricClientCommandSource> baseCommand = ClientCommandManager.literal("scm");
}
