package smallclockmod;

import net.minecraft.text.Text;
import smallclockmod.config.ConfigManager;

public class ClientMessages {
    private static final String DisplayName = ConfigManager.get("DisplayName");
    public final static String Prefix = "§f[§d"+DisplayName+"§f]§r ";
    public final static Text CommandCompleteMessage = New("Command Complete");

    public static Text New(String message) {
        return Text.literal(Prefix + "§f" + message + "§r");
    }
}
