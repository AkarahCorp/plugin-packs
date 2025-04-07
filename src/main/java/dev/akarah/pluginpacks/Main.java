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

//        var rg = MultiTypeRegistry.getInstance().register(PluginNamespace.<Example>create("examples"),
//                TypeRegistry.create(ExampleType.CODEC));
//        rg.register(ExInstance.TYPE, ExInstance.CODEC.xmap(x -> x, x -> (ExInstance) x));
//
//        PackRepository.getInstance().lazyLoadRegistries();
//
//        var examplesCodec = rg.codec();
//        PackRepository.getInstance().addRegistry(
//                PluginNamespace.create("examples"),
//                PackRepository.RegistryInstance.create(examplesCodec, Example.class)
//        );
//
//        System.out.println(
//                PackRepository.getInstance().getRegistry(PluginNamespace.create("examples"))
//        );
        PackRepository.getInstance().lazyLoadRegistries();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }
}
