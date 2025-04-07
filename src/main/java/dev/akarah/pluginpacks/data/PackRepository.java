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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public final class PackRepository {
    private static final PackRepository INSTANCE = new PackRepository();

    ConcurrentHashMap<PluginNamespace<?>, PluginPack> repositories = new ConcurrentHashMap<>();
    ConcurrentHashMap<PluginNamespace<?>, RegistryInstance<?>> instances = new ConcurrentHashMap<>();

    public static PackRepository getInstance() {
        return PackRepository.INSTANCE;
    }

    public void reloadRegistries() {
        this.lazyLoadRegistries();

        synchronized (this) {
            for (var registry : this.instances.entrySet()) {
                registry.getValue().registry.clear();

                for (Map.Entry<String, JsonElement> entry : this.repositories.get(registry.getKey()).map.entrySet()) {
                    try {
                        var finalValue = registry.getValue().codec.decode(JsonOps.INSTANCE, entry.getValue()).getOrThrow().getFirst();
                        registry.getValue().insert(NamespacedKey.fromString(entry.getKey()), finalValue);
                    } catch (IllegalStateException e) {
                        Main.getInstance().getLogger().log(Level.SEVERE, "Failed to load entry " + entry.getKey() + " for registry " + registry.getKey() + ", see error:");
                        Main.getInstance().getLogger().log(Level.SEVERE, e.getMessage());
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <S> void addRegistry(PluginNamespace<S> name, RegistryInstance<S> registryInstance) {
        instances.put(name, registryInstance);

        if (this.repositories.containsKey(name)) {
            for (Map.Entry<String, JsonElement> entry : this.repositories.get(name).map.entrySet()) {
                try {
                    var finalValue = registryInstance.codec.decode(JsonOps.INSTANCE, entry.getValue()).getOrThrow().getFirst();
                    registryInstance.insert(NamespacedKey.fromString(entry.getKey()), finalValue);
                } catch (IllegalStateException e) {
                    Main.getInstance().getLogger().log(Level.SEVERE, "Failed to load entry " + entry.getKey() + " for registry " + name + ", see error:");
                    Main.getInstance().getLogger().log(Level.SEVERE, e.getMessage());
                }
            }
        }
    }

    public void lazyLoadRegistries() {
        var dataFolder = Main.INSTANCE.getDataFolder();
        var pluginPackDirectories = dataFolder.listFiles();

        assert pluginPackDirectories != null;

        for (var pluginPackDir : pluginPackDirectories) {
            if (pluginPackDir.isDirectory()) {
                var pluginPath = pluginPackDir.toPath();

                try (var paths = Files.walk(pluginPath)) {
                    paths.filter(Files::isRegularFile)
                            .filter(path -> path.toString().endsWith(".json"))
                            .forEach(path -> {
                                try {
                                    var relative = pluginPath.relativize(path);

                                    var registry = PluginNamespace.create(relative.getName(0).toString());

                                    System.out.println("registry: " + registry);
                                    System.out.println("relative: " + relative);
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
                                    Main.getInstance().getLogger().log(Level.SEVERE, "An I/O error occurred while loading plugin packs, see below:");
                                    Main.getInstance().getLogger().log(Level.SEVERE, error.toString());
                                }
                            });
                } catch (IOException error) {
                    Main.getInstance().getLogger().log(Level.SEVERE, "An I/O error occurred while loading plugin packs, see below:");
                    Main.getInstance().getLogger().log(Level.SEVERE, error.toString());
                }

                System.out.println(this.repositories);
            }
        }

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

        @Override
        public String toString() {
            return this.registry.toString();
        }
    }
}
