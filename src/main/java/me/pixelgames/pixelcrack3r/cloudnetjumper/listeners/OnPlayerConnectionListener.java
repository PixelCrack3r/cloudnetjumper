package me.pixelgames.pixelcrack3r.cloudnetjumper.listeners;

import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot;
import me.pixelgames.pixelcrack3r.cloudnetjumper.CloudNetJumper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class OnPlayerConnectionListener implements Listener {

    private final CloudServiceProvider cloudServiceProvider;

    public OnPlayerConnectionListener(CloudServiceProvider cloudServiceProvider) {
        this.cloudServiceProvider = cloudServiceProvider;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(ServerConnectEvent e) {
        if(!CloudNetJumper.getInstance().getConfig().get("enabled").getAsBoolean()) return;
        if(e.getReason() != ServerConnectEvent.Reason.JOIN_PROXY && e.getReason() != ServerConnectEvent.Reason.LOBBY_FALLBACK) return;
        ServiceInfoSnapshot fallback = this.cloudServiceProvider.serviceByName(CloudNetJumper.getInstance().getConfig().get("forceFallbackService").getAsString());
        if(fallback != null) {
            ProxyServer.getInstance().getLogger().info("Forcing player " + e.getPlayer().getName() + " to join on " + fallback.name());
            ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(fallback.name());
            if(serverInfo == null) {
                ProxyServer.getInstance().getLogger().warning("No matching fallback for " + fallback.name() + " found.");
                this.evadeIfSet(e);
                return;
            }

            e.setTarget(serverInfo);
            return;
        }
        this.evadeIfSet(e);

    }

    private void evadeIfSet(ServerConnectEvent e) {
        if(!CloudNetJumper.getInstance().getConfig().get("emergencyEvade").getAsBoolean()) {
            e.getPlayer().disconnect(TextComponent.fromLegacyText("§cWe are sorry, but we couldn't connect you to a valid fallback"));
            e.setCancelled(true);
        }
    }
}
