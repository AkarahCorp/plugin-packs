package dev.akarah.pluginpacks;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Main INSTANCE;

    public static Main getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        INSTANCE = this;


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
