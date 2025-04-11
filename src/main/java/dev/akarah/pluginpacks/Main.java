package dev.akarah.pluginpacks;

import dev.akarah.pluginpacks.data.PackRepository;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public final class Main extends JavaPlugin {
    public static Main INSTANCE;
    public static Logger LOGGER;

    public static Main getInstance() {
        return INSTANCE;
    }

    public static Logger logger() {
        return LOGGER;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        INSTANCE = this;
        LOGGER = this.getSLF4JLogger();

        PackRepository.getInstance().reloadRegistries();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
