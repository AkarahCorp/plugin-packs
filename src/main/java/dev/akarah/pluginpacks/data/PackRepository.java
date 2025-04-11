package dev.akarah.pluginpacks.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.akarah.pluginpacks.Main;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class PackRepository {
    private static final PackRepository INSTANCE = new PackRepository();

    ConcurrentHashMap<PluginNamespace<?>, PluginPack> repositories = new ConcurrentHashMap<>();
    ConcurrentHashMap<PluginNamespace<?>, RegistryInstance<?>> instances = new ConcurrentHashMap<>();
    Path dataDirectory;

    public static PackRepository getInstance() {
        return PackRepository.INSTANCE;
    }

    public void dataDirectory(Path path) {
        this.dataDirectory = path;
    }

    public Path getDataDirectory() {
        return this.dataDirectory;
    }

    public void reloadRegistries() {
        Main.logger().info("Reloading plugin pack registries...");
        this.lazyLoadRegistriesFromFiles();
        Main.logger().info("Executing transformations from JSON to data...");

        synchronized (this) {
            for (var registry : this.instances.entrySet()) {
                var repository = this.repositories.get(registry.getKey());
                if (repository == null) {
                    Main.logger().warn("A plugin pack tried to access the registry `{}` which does not exist.", registry.getKey());
                    Main.logger().warn("The data will be skipped.");
                    continue;
                }

                var entries = repository.map.entrySet();
                registry.getValue().registry.clear();

                for (Map.Entry<String, JsonElement> entry : entries) {
                    try {
                        var finalValue = registry.getValue().codec.decode(JsonOps.INSTANCE, entry.getValue()).getOrThrow().getFirst();
                        registry.getValue().insert(NamespacedKey.fromString(entry.getKey()), finalValue);
                    } catch (Exception e) {
                        Main.logger().error("Failed to compute entry {} for registry {}", entry.getKey(), registry.getKey());
                        Main.logger().error("Exception: ", e);
                    }
                }
            }
        }

        Main.logger().info("All valid registry entries are now loaded!");
    }

    public <S> void addRegistry(PluginNamespace<S> name, RegistryInstance<S> registryInstance) {
        instances.put(name, registryInstance);

        if (this.repositories.containsKey(name)) {
            for (Map.Entry<String, JsonElement> entry : this.repositories.get(name).map.entrySet()) {
                try {
                    var finalValue = registryInstance.codec.decode(JsonOps.INSTANCE, entry.getValue()).getOrThrow().getFirst();
                    registryInstance.insert(NamespacedKey.fromString(entry.getKey()), finalValue);
                } catch (Exception e) {
                    Main.logger().error("Failed to load entry {} for registry {}", entry.getKey(), name);
                    Main.logger().error("Exception: ", e);
                }
            }
        }
    }

    public void lazyLoadRegistriesFromFiles() {
        synchronized (this) {
            Main.logger().info("Reloading registry cache from files...");
            this.repositories.clear();

            var dataFolder = this.dataDirectory.toFile();
            var pluginPackDirectories = dataFolder.listFiles();

            if (pluginPackDirectories == null) {
                Main.logger().warn("The plugin-packs directory was not found in plugins folder, please consider creating it to register entries.");
                return;
            }

            for (var pluginPackDir : pluginPackDirectories) {
                if (pluginPackDir.isDirectory()) {
                    Main.logger().info("Loading plugin pack at /{}/...", pluginPackDir.getName());
                    var pluginPath = pluginPackDir.toPath();

                    try (var paths = Files.walk(pluginPath)) {
                        paths.filter(Files::isRegularFile)
                                .filter(path -> path.toString().endsWith(".json"))
                                .forEach(path -> {
                                    try {
                                        var relative = pluginPath.relativize(path);

                                        var registry = PluginNamespace.create(relative.getName(0).toString());

                                        var registryEntryName = relative.getNameCount() > 1
                                                ? relative.subpath(1, relative.getNameCount()).toString().replaceFirst("/", ":")
                                                : path.subpath(2, 3).toString();
                                        registryEntryName = registryEntryName.replace(".json", "");

                                        var contents = Files.readString(path);
                                        var jsonContents = new Gson().fromJson(contents, JsonElement.class);

                                        var pluginPack = this.repositories.computeIfAbsent(registry, key -> new PluginPack());
                                        if (jsonContents != null) {
                                            pluginPack.map.put(registryEntryName, jsonContents);
                                        }
                                    } catch (IOException error) {
                                        Main.logger().error("An I/O error occurred while loading pack entry at `{}`: {}", path, error.toString());
                                    }
                                });
                    } catch (IOException error) {
                        Main.logger().error("An I/O error occurred while loading plugin packs: {}", error.toString());
                    }
                } else {
                    Main.logger().warn("Non-directory file {} found in plugin-packs directory, it will be skipped.", (Object) pluginPackDirectories);
                }
            }
        }
        Main.logger().info("Finished loading registries from files!");
    }

    @SuppressWarnings("unchecked")
    public <S> Optional<RegistryInstance<S>> getRegistry(PluginNamespace<S> key) {
        if (instances.containsKey(key)) {
            return Optional.of((RegistryInstance<S>) instances.get(key));
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return this.instances.toString();
    }

    public static final class RegistryInstance<S> {
        Codec<S> codec;
        ConcurrentHashMap<NamespacedKey, S> registry;
        Class<S> sClass;

        public static <S> @NonNull RegistryInstance<S> create(@NonNull Codec<S> codec, @NonNull Class<S> sClass) {
            var instance = new RegistryInstance<S>();
            instance.codec = codec;
            instance.registry = new ConcurrentHashMap<>();
            instance.sClass = sClass;
            return instance;
        }

        public <T> void insert(NamespacedKey key, @NonNull T object) {
            if (sClass.isInstance(object)) {
                this.registry.put(key, sClass.cast(object));
            } else {
                throw new TypeException("expected non-null instance of " + sClass + " got instance of " + object.getClass());
            }
        }

        public Optional<S> get(NamespacedKey key) {
            if (!registry.containsKey(key)) {
                return Optional.empty();
            }
            return Optional.of(this.registry.get(key));
        }

        public Set<Map.Entry<NamespacedKey, S>> entries() {
            return this.registry.entrySet();
        }

        @Override
        public String toString() {
            return this.registry.toString();
        }
    }
}
