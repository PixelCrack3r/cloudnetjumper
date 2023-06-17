package me.pixelgames.pixelcrack3r.cloudnetjumper;

import com.google.gson.*;
import dev.derklaro.aerogel.Inject;
import dev.derklaro.aerogel.Singleton;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Dependency;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import me.pixelgames.pixelcrack3r.cloudnetjumper.listeners.OnPlayerConnectionListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.*;

@Singleton
@PlatformPlugin(
        platform = "bungeecord",
        name = "CloudNetJumper",
        pluginFileNames = "bungee.yml",
        version = "2.0",
        authors = "PixelCrack3r",
        description = "A plugin to force players to connect on specific services.",
        dependencies = @Dependency(name = "CloudNet-Bridge")
)
public final class CloudNetJumper implements PlatformEntrypoint {

    private static CloudNetJumper instance;

    private final Plugin plugin;
    private final PluginManager pluginManager;
    private final CloudServiceProvider cloudServiceProvider;

    private JsonObject config;

    @Inject
    public CloudNetJumper(Plugin plugin, PluginManager pluginManager, CloudServiceProvider cloudServiceProvider) {
        instance = this;

        this.plugin = plugin;
        this.pluginManager = pluginManager;
        this.cloudServiceProvider = cloudServiceProvider;
    }

    @Override
    public void onLoad() {
        this.loadConfig();

        this.pluginManager.registerListener(this.plugin, new OnPlayerConnectionListener(this.cloudServiceProvider));
    }

    @Override
    public void onDisable() {}

    public JsonObject getConfig() {
        return this.config;
    }

    private void loadDefaults() {
        this.config.addProperty("enabled", true);
        this.config.addProperty("emergencyEvade", false);
        this.config.addProperty("forceFallbackService", "CustomService");
    }

    public void loadConfig() {
        try {
            File file = new File("./plugins/" + this.plugin.getDescription().getName() + "/configuration.json");
            if(!file.exists()) {
                if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
                file.createNewFile();
                this.config = new JsonObject();
                this.loadDefaults();
                this.saveConfig();
            }
            this.config = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            String config = gson.toJson(this.config);

            PrintWriter writer = new PrintWriter(new FileWriter("./plugins/" + this.plugin.getDescription().getName() + "/configuration.json"));
            writer.println(config);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PluginManager getPluginManager() { return this.pluginManager; }

    public static CloudNetJumper getInstance() {
        return instance;
    }
}
