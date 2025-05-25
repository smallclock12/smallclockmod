package smallclockmod.config;

import java.util.Hashtable;

public class ConfigDefaults {
    private static Hashtable<String, String> configDefaults;

    public static Hashtable<String, String> getConfigDefault() {
        if (configDefaults == null) {
            configDefaults = new Hashtable<String, String>();
            configDefaults.put("DisplayName", "SmallClockMod");
        }
        return configDefaults;
    }
}
