package dev.akarah.pluginpacks;

import dev.akarah.pluginpacks.commands.ReloadCommand;
import dev.akarah.pluginpacks.data.PackRepository;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.NotNull;

public class Bootstrapper implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
        Main.LOGGER = bootstrapContext.getLogger();

        PackRepository.getInstance().dataDirectory(bootstrapContext.getDataDirectory());
        PackRepository.getInstance().reloadRegistries();

        bootstrapContext.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> ReloadCommand.register(event.registrar()));
    }
}
