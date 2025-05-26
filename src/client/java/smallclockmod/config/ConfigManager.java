package smallclockmod.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import smallclockmod.SmallClockMod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigManager {

    private static Path path;
    private static final AtomicBoolean loaded = new AtomicBoolean(false);
    private static final AtomicBoolean loading = new AtomicBoolean(false);
    private static final AtomicBoolean saving = new AtomicBoolean(false);
    private static final Gson GSON = new Gson();

    private final static Map<String, String> configDefaults = Map.of(
            Keys.DISPLAY_NAME, "SmallClockMod",
            Keys.PRIMARY_COLOUR, "f",
            Keys.SECONDARY_COLOUR, "d",
            combine(Keys.TOGGLE, Toggles.CHAT_CLEAR_PROTECTION), "1"
    );

    private final static Map<String, String> config = new HashMap<>(configDefaults);

    public static String combine(String... keys) {
        return String.join(Keys.SEPARATOR, keys);
    }

    public static boolean isToggled(String key) {
        var value = get(combine(Keys.TOGGLE, key));
        return value.equals("1");
    }

    public static boolean toggle(String key) {
        var toggled = isToggled(key);
        var newValue = "0";
        if (!toggled)
            newValue = "1";

        set(combine(Keys.TOGGLE, key), newValue);
        return !toggled;
    }

    public static String get(String key) {
        load();
        return config.get(key);
    }

    public static void set(String key, String value) {
        load();
        config.put(key, value);
        save();
    }

    private static Path path() {
        if (path == null) {
            path = FabricLoader.getInstance().getConfigDir().resolve(SmallClockMod.MOD_ID + ".json");
        }

        return path;
    }

    private static void save() {
        var json = new JsonObject();
        try {
            saving.compareAndExchangeAcquire(false, true);
            for (String key : config.keySet()) {
                var value = config.get(key);
                json.add(key, JsonParser.parseString(value));
            }
        } catch(Exception ex) {
            System.err.println("Error saving config file");
        }

        try (BufferedWriter fileWriter = Files.newBufferedWriter(path())) {
            String jsonString = json.toString();
            fileWriter.write(jsonString);
        } catch (Exception e) {
            System.err.println("Error saving config file");
            System.err.print(e.getMessage());
        }

        saving.compareAndExchangeRelease(true, false);
    }

    private static void load() {
        boolean l = loaded.getAcquire();
        if (loading.compareAndExchangeAcquire(false, true) && !l)
            return;

        try {
            if (!Files.exists(path())) {
                save();
            } else {
                BufferedReader br = Files.newBufferedReader(path());
                var json = JsonParser.parseReader(br).getAsJsonObject();
                var map = new HashMap<String, String>();
                for (var j : json.entrySet()) {
                    map.put(j.getKey(), j.getValue().getAsString());
                }
                config.putAll(map);
            }
        } catch (Exception e) {
            System.err.println("Error loading config file");
            System.err.print(e.getMessage());
        }

        loading.setRelease(false);
        loaded.setRelease(true);
    }
}
