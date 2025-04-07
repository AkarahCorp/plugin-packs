package dev.akarah.pluginpacks;

import dev.akarah.pluginpacks.data.PackRepository;
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

        PackRepository.getInstance().reloadRegistries();
        System.out.println(PackRepository.getInstance());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
