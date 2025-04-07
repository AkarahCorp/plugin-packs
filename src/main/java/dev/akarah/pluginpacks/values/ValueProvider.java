package dev.akarah.pluginpacks.values;

import com.mojang.serialization.Codec;

public interface ValueProvider<T> {
    Codec<ValueProvider<?>> ABSTRACT_CODEC = ValueProviderType.CODEC.dispatch(
            "type",
            ValueProvider::getType,
            type -> ValueProviderRegistry.getInstance().lookup(type)
    );

    Codec<ValueProvider> UNPARAMETERIZED_CODEC = ValueProvider.ABSTRACT_CODEC.xmap(x -> x, x -> x);

    ValueProviderType<?> getType();

    T get();
}
