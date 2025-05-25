package smallclockmod;

import smallclockmod.commands.ChatClearTestEmptyCommand;
import smallclockmod.commands.ChatClearTestNewlineCommand;
import smallclockmod.commands.IClientCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class SmallClockModClient implements ClientModInitializer {

	private final static IClientCommand[] Cmds = { new ChatClearTestNewlineCommand(), new ChatClearTestEmptyCommand() };

	@Override
	public void onInitializeClient() {
		for (IClientCommand cmd : Cmds) {
			ClientCommandRegistrationCallback.EVENT.register((
					(cd, cra) -> cd.register(cmd.build().cmd())
			));
		}
	}
}