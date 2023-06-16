package me.pixelgames.pixelcrack3r.cloudnetjumper;

import com.google.gson.*;
import me.pixelgames.pixelcrack3r.cloudnetjumper.listeners.OnPlayerConnectionListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.*;

public final class CloudNetJumper extends Plugin {

    private static CloudNetJumper plugin;

    private JsonObject config;

    @Override
    public void onEnable() {
        plugin = this;
        this.loadConfig();

        ProxyServer.getInstance().getPluginManager().registerListener(this, new OnPlayerConnectionListener());
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
            File file = new File("./plugins/" + this.getDescription().getName() + "/configuration.json");
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

            PrintWriter writer = new PrintWriter(new FileWriter("./plugins/" + this.getDescription().getName() + "/configuration.json"));
            writer.println(config);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CloudNetJumper getInstance() {
        return plugin;
    }
}
