package dev.akarah.pluginpacks.example;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ExampleData(String str, int num) {
    public static Codec<ExampleData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    PrimitiveCodec.STRING.fieldOf("str").forGetter(ExampleData::str),
                    PrimitiveCodec.INT.fieldOf("num").forGetter(ExampleData::num)
            ).apply(instance, ExampleData::new)
    );
}
