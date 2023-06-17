package me.pixelgames.pixelcrack3r.cloudnetjumper;

import eu.cloudnetservice.ext.platforminject.loader.PlatformInjectSupportLoader;
import java.lang.Override;
import net.md_5.bungee.api.plugin.Plugin;

public class GeneratedBungeecordCloudNetJumperEntrypoint extends Plugin {
  public GeneratedBungeecordCloudNetJumperEntrypoint() {
  }

  @Override
  public void onEnable() {
    PlatformInjectSupportLoader.loadPlugin("bungeecord", CloudNetJumper.class, this, this.getClass().getClassLoader().getParent());
  }

  @Override
  public void onDisable() {
    PlatformInjectSupportLoader.disablePlugin("bungeecord", this);
  }
}
