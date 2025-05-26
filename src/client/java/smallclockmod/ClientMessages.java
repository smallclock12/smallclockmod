package smallclockmod;

import net.minecraft.text.Text;
import smallclockmod.config.ConfigManager;
import smallclockmod.config.Keys;

public class ClientMessages {

    public static Text New(String message) {
        return Text.literal(Prefix() + "§" + ConfigManager.get(Keys.PRIMARY_COLOUR) + message + "§r");
    }

    public static String Prefix() {
        var p = ConfigManager.get(Keys.PRIMARY_COLOUR);
        var s = ConfigManager.get(Keys.SECONDARY_COLOUR);
        var d = ConfigManager.get(Keys.DISPLAY_NAME);
        return String.format("§%s[§%s%s§%s]§r ", p, s, d, p);
    }
}
