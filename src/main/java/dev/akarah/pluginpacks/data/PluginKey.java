package dev.akarah.pluginpacks.data;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jetbrains.annotations.NotNull;

public record PluginKey<T>(String namespace, String value) implements Key {
    public static <T> PluginKey<T> create(String namespace, String value) {
        return new PluginKey<>(namespace, value);
    }

    @KeyPattern.Namespace
    @Override
    public @NotNull String namespace() {
        return this.namespace;
    }

    @KeyPattern.Value
    @Override
    public @NotNull String value() {
        return this.value;
    }

    @Override
    public @NotNull String asString() {
        return this.namespace + ":" + this.value;
    }
}
