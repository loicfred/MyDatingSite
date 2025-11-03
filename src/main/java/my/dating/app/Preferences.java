package my.dating.app;

import my.dating.app.config.AppConfig;
import my.utilities.json.JSONItem;

import java.io.FileReader;
import java.nio.charset.StandardCharsets;

public class Preferences extends JSONItem {
    public static AppConfig ApplicationConfiguration = new AppConfig();

    public String DatabaseURL = "N/A";
    public String DatabaseUsername = "N/A";
    public String DatabasePassword = "N/A";

    public static Preferences Load() {
        try (FileReader reader = new FileReader("./defaults.json", StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, Preferences.class);
        } catch (Exception e) {
            return new Preferences().Save();
        }
    }
    public Preferences Save() {
        System.out.println("[Default] Saving the defaults...");
        Save("./defaults.json");
        return this;
    }

}
