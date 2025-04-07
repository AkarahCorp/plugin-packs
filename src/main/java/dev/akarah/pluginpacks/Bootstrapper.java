package dev.akarah.pluginpacks;

import dev.akarah.pluginpacks.data.PackRepository;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import org.jetbrains.annotations.NotNull;

public class Bootstrapper implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
        PackRepository.getInstance().dataDirectory(bootstrapContext.getDataDirectory());
        PackRepository.getInstance().reloadRegistries();
    }
}
