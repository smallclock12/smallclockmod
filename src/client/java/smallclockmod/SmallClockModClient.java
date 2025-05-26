package smallclockmod;

import smallclockmod.commands.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class SmallClockModClient implements ClientModInitializer {

	private final static IClientCommand[] Cmds = {
			new TestCommand(),
			new SetCommand(),
			new ToggleCommand()
	};


	@Override
	public void onInitializeClient() {
		for (IClientCommand cmd : Cmds) {
			ClientCommandRegistrationCallback.EVENT.register((
					(cd, cra) -> cd.register(cmd.build().cmd())
			));
		}
	}
}