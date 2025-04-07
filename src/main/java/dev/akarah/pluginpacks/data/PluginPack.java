package dev.akarah.pluginpacks.data;

import com.google.gson.JsonElement;

import java.util.HashMap;

public class PluginPack {
    public HashMap<String, JsonElement> map = new HashMap<>();

    @Override
    public String toString() {
        return this.map.toString();
    }
}
