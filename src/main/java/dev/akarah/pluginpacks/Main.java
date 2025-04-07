package dev.akarah.pluginpacks;

import dev.akarah.pluginpacks.data.PackRepository;
import dev.akarah.pluginpacks.data.PluginNamespace;
import dev.akarah.pluginpacks.values.ValueProvider;
import dev.akarah.pluginpacks.values.ValueProviderRegistry;
import dev.akarah.pluginpacks.values.number.ConstantNumber;
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

        ValueProviderRegistry.getInstance().register(ConstantNumber.TYPE, ConstantNumber.CODEC);

        PackRepository.getInstance().lazyLoadRegistries();

        PackRepository.getInstance().addRegistry(
                PluginNamespace.create("numbers"),
                PackRepository.RegistryInstance.create(ValueProvider.UNPARAMETERIZED_CODEC, ValueProvider.class)
        );

        System.out.println(
                PackRepository.getInstance().getRegistry(PluginNamespace.create("numbers"))
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
