package dev.akarah.pluginpacks.multientry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.util.HashMap;

public class TypeRegistry<E extends TypeRegistrySupported<ET>, ET> {
    HashMap<ET, MapCodec<E>> map = new HashMap<>();
    Codec<ET> entryTypeCodec;
    String dispatchKey;

    public static<E extends TypeRegistrySupported<ET>, ET> TypeRegistry<E, ET> create(Codec<ET> entryTypeCodec) {
        return new TypeRegistry<>(entryTypeCodec);
    }

    public TypeRegistry<E, ET> dispatchKey(String newKey) {
        this.dispatchKey = dispatchKey;
        return this;
    }

    protected TypeRegistry(Codec<ET> entryTypeCodec) {
        this.entryTypeCodec = entryTypeCodec;
        this.dispatchKey = "type";
    }



    public void register(ET entryType, MapCodec<E> entry) {
        this.map.put(entryType, entry);
    }

    public Codec<E> codec() {
        return this.entryTypeCodec.dispatch(
            this.dispatchKey,
                TypeRegistrySupported::getType,
                y -> this.map.get(y)
        );
    }
}
