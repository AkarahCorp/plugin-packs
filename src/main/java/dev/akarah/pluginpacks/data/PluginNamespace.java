package dev.akarah.pluginpacks.data;

import net.kyori.adventure.key.KeyPattern;
import org.jetbrains.annotations.NotNull;

public record PluginNamespace<T>(String namespace) {
    public static <T> PluginNamespace<T> create(String namespace) {
        return new PluginNamespace<>(namespace);
    }

    @KeyPattern.Namespace
    @Override
    public @NotNull String namespace() {
        return this.namespace;
    }

    @Override
    public String toString() {
        return this.namespace;
    }
}
