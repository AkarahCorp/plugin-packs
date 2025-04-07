package dev.akarah.pluginpacks.multientry;

import com.mojang.serialization.Codec;
import dev.akarah.pluginpacks.data.PluginNamespace;

import java.util.HashMap;
import java.util.Optional;

public class MultiTypeRegistry {
    static MultiTypeRegistry INSTANCE = new MultiTypeRegistry();
    HashMap<PluginNamespace<?>, TypeRegistry<?, ?>> instances = new HashMap<>();

    public static MultiTypeRegistry getInstance() {
        return INSTANCE;
    }

    public <E extends TypeRegistrySupported<ET>, ET> TypeRegistry<E, ET> register(PluginNamespace<E> namespace, TypeRegistry<E, ET> typeRegistry) {
        this.instances.put(namespace, typeRegistry);
        return typeRegistry;
    }

    @SuppressWarnings("unchecked")
    public <E extends TypeRegistrySupported<ET>, ET> Optional<TypeRegistry<E, ET>> lookup(PluginNamespace<E> namespace) {
        if(this.instances.containsKey(namespace)) {
            return Optional.of((TypeRegistry<E, ET>) this.instances.get(namespace));
        }
        return Optional.empty();
    }
}
