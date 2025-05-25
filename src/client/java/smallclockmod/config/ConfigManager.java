package smallclockmod.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import smallclockmod.SmallClockMod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings({"unchecked", "CallToPrintStackTrace"})
public class ConfigManager {
    private static Dictionary<String, String> store;
    private static final AtomicBoolean loaded = new AtomicBoolean(false);
    private static final AtomicBoolean loading = new AtomicBoolean(false);
    private static final AtomicBoolean saving = new AtomicBoolean(false);
    private static Path path;

    private static Path path() {
        if (path == null) {
            path = FabricLoader.getInstance().getConfigDir().resolve(SmallClockMod.MOD_ID + ".json");
        }

        return path;
    }

    private static void save() {

        JsonObject json = new JsonObject();
        try {
            saving.compareAndExchangeAcquire(false, true);
            for (Enumeration<String> s = store.keys(); s.hasMoreElements();) {
                String element = s.nextElement();
                String value = store.get(element);
                json.add(element, JsonParser.parseString(value));
            }
        } catch(Exception ex) {
            System.err.println("Error saving config file");
        }

        try (BufferedWriter fileWriter = Files.newBufferedWriter(path())) {
            String jsonString = json.toString();
            fileWriter.write(jsonString);
        } catch (Exception e) {
            System.err.println("Error saving config file");
            e.printStackTrace();
        }

        saving.compareAndExchangeRelease(true, false);
    }

    private static void load() {
        boolean l = loaded.getAcquire();
        if (loading.compareAndExchangeAcquire(false, true) && !l)
            return;

        try {
            if (!Files.exists(path())) {
                store = (Hashtable<String, String>)ConfigDefaults.getConfigDefault().clone();
                save();
            } else {
                BufferedReader br = Files.newBufferedReader(path());
                JsonObject json = JsonParser.parseReader(br).getAsJsonObject();
                store = new Hashtable<>();

                for (java.util.Map.Entry<String, JsonElement> j : json.entrySet()) {
                    store.put(j.getKey(), j.getValue().getAsString());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading config file");
            e.printStackTrace();
        }

        loading.setRelease(false);
        loaded.setRelease(true);
    }

    public static String get(String key) {
        load();
        return store.get(key);
    }

    public static void set(String key, String value) {
        load();
        store.put(key, value);
        save();
    }
}
