package dev.akarah.pluginpacks.values;

import com.mojang.serialization.MapCodec;

import java.util.HashMap;

public class ValueProviderRegistry {
    static ValueProviderRegistry INSTANCE = new ValueProviderRegistry();
    HashMap<ValueProviderType<?>, MapCodec<? extends ValueProvider<?>>> map = new HashMap<>();

    public static ValueProviderRegistry getInstance() {
        return INSTANCE;
    }

    public void register(ValueProviderType<?> type, MapCodec<? extends ValueProvider<?>> codec) {
        this.map.put(type, codec);
    }

    public MapCodec<? extends ValueProvider<?>> lookup(ValueProviderType<?> type) {
        return this.map.get(type);
    }
}
