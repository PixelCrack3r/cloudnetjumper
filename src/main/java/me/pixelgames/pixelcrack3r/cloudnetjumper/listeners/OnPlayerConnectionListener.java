package me.pixelgames.pixelcrack3r.cloudnetjumper.listeners;

import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.bungee.event.BungeePlayerFallbackEvent;
import me.pixelgames.pixelcrack3r.cloudnetjumper.CloudNetJumper;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class OnPlayerConnectionListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(BungeePlayerFallbackEvent e) {
        if(!CloudNetJumper.getInstance().getConfig().get("enabled").getAsBoolean()) return;
        ServiceInfoSnapshot fallback = e.getDriver().getCloudServiceProvider().getCloudServiceByName(CloudNetJumper.getInstance().getConfig().get("forceFallbackService").getAsString());
        if(fallback != null) {
            e.setFallback(fallback);
        } else if(!CloudNetJumper.getInstance().getConfig().get("emergencyEvade").getAsBoolean()) {
            e.setFallback((ServiceInfoSnapshot) null);
        }
    }
}
