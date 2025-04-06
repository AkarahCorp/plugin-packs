package dev.akarah.pluginpacks.data;

import com.mojang.serialization.Codec;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Optional;

public final class PackRepository {
    private static final PackRepository INSTANCE = new PackRepository();
    HashMap<PluginKey<Object>, RegistryInstance<Object>> instances = new HashMap<>();

    public static PackRepository getInstance() {
        return PackRepository.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <S> void addRegistry(PluginKey<S> name, RegistryInstance<S> registryInstance) {
        instances.put((PluginKey<Object>) name, (RegistryInstance<Object>) registryInstance);
    }

    @SuppressWarnings("unchecked")
    public <S> Optional<RegistryInstance<S>> getRegistry(PluginKey<S> key) {
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
        HashMap<NamespacedKey, S> registry;
        Class<S> sClass;

        public static <S> @NonNull RegistryInstance<S> create(@NonNull Codec<S> codec, @NonNull Class<S> sClass) {
            var instance = new RegistryInstance<S>();
            instance.codec = codec;
            instance.registry = new HashMap<>();
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

        public <T> Optional<T> get(NamespacedKey key, Class<T> tClass) {
            if (!registry.containsKey(key)) {
                return Optional.empty();
            }

            return Optional.of(tClass.cast(this.registry.get(key)));
        }
    }
}
