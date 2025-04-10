package dev.akarah.pluginpacks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.List;

public class Codecs {
    public static Codec<Material> MATERIAL = PrimitiveCodec.STRING.xmap(x -> Material.matchMaterial(x.toUpperCase()),
            Material::name);
    public static Codec<NamespacedKey> NAMESPACED_KEY = PrimitiveCodec.STRING.xmap(NamespacedKey::fromString,
            NamespacedKey::asString);
    public static Codec<Color> COLOR = PrimitiveCodec.INT.listOf(3, 3).xmap(
            x -> Color.fromRGB(x.getFirst(), x.get(1), x.get(2)),
            x -> List.of(x.getRed(), x.getGreen(), x.getBlue()));
    public static Codec<Component> COMPONENT = PrimitiveCodec.STRING
            .xmap(
                    str -> MiniMessage.miniMessage().deserialize(str),
                    comp -> MiniMessage.miniMessage().serialize(comp)
            );
}
