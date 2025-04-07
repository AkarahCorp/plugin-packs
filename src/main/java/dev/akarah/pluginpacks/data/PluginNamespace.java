package dev.akarah.pluginpacks.data;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jetbrains.annotations.NotNull;

public record PluginNamespace<T>(String namespace) implements Key {
    public static <T> PluginNamespace<T> create(String namespace) {
        return new PluginNamespace<>(namespace);
    }

    @KeyPattern.Namespace
    @Override
    public @NotNull String namespace() {
        return this.namespace;
    }

    @KeyPattern.Value
    @Override
    public @NotNull String value() {
        return this.namespace;
    }

    @Override
    public @NotNull String asString() {
        return this.namespace;
    }
}
