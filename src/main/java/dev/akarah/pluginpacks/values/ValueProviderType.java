package dev.akarah.pluginpacks.values;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;

public record ValueProviderType<P extends ValueProvider<?>>(String name) {
    public static Codec<ValueProviderType<?>> CODEC = PrimitiveCodec.STRING.xmap(ValueProviderType::new, ValueProviderType::name);
}
