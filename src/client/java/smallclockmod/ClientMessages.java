package smallclockmod;

import net.minecraft.text.Text;

public class ClientMessages {
    public final static String Prefix = "§f[§dSmallClockMod§f]§r ";
    public final static Text CommandCompleteMessage = New("Command Complete");

    public static Text New(String message) {
        return Text.literal(Prefix + "§f" + message + "§r");
    }
}
