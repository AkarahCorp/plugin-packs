package dev.akarah.pluginpacks;

import dev.akarah.pluginpacks.data.PackRepository;
import dev.akarah.pluginpacks.data.PluginKey;
import dev.akarah.pluginpacks.example.ExampleData;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.println(
                PackRepository.getInstance()
        );

        PackRepository.getInstance().addRegistry(
                PluginKey.create("minecraft", "example"),
                PackRepository.RegistryInstance.create(ExampleData.CODEC, ExampleData.class)
        );

        System.out.println(
                PackRepository.getInstance()
        );

        System.out.println(
                PackRepository.getInstance().getRegistry(PluginKey.create("minecraft", "example"))
                        .flatMap(registry -> registry.get(NamespacedKey.fromString("hello"), ExampleData.class))
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
