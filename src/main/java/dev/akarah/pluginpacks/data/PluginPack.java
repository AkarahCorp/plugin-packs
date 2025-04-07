package dev.akarah.pluginpacks.data;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class PluginPack {
    public ConcurrentHashMap<String, JsonElement> map = new ConcurrentHashMap<>();

    @Override
    public String toString() {
        return this.map.toString();
    }
}
