package dev.akarah.pluginpacks.values.number;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import dev.akarah.pluginpacks.values.ValueProvider;
import dev.akarah.pluginpacks.values.ValueProviderType;

public record ConstantNumber(double value) implements ValueProvider<Double> {
    public static ValueProviderType<ConstantNumber> TYPE = new ValueProviderType<>("number");
    public static MapCodec<ConstantNumber> CODEC = PrimitiveCodec.DOUBLE.fieldOf("value").xmap(ConstantNumber::new, ConstantNumber::value);

    @Override
    public ValueProviderType<?> getType() {
        return TYPE;
    }

    @Override
    public Double get() {
        return value;
    }
}
