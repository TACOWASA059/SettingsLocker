package com.github.tacowasa059.settingslocker.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

import java.util.HashMap;
import java.util.Map;

public class UpdateDebugSetting {

    private static final Map<String, Boolean> defaultKeys = new HashMap<>();

    public static void updateDebugSetting(Map<String, Map<String, String>> yamlData){
        Minecraft minecraft = Minecraft.getInstance();
        Options options = minecraft.options;

        String name = "key.debugF1";
        if (!defaultKeys.containsKey(name)) {
            defaultKeys.put(name, options.hideGui);
        }
        options.hideGui = updateDebugSetting(yamlData, name, options.hideGui);

        name = "key.debugF3";
        if (!defaultKeys.containsKey(name)) {
            defaultKeys.put(name, options.renderDebug);
        }
        options.renderDebug = updateDebugSetting(yamlData, name, options.renderDebug);

        name = "key.debugF3+B";
        boolean default_value = minecraft.getEntityRenderDispatcher().shouldRenderHitBoxes();
        if(!defaultKeys.containsKey(name)){
            defaultKeys.put(name, default_value);
        }
        minecraft.getEntityRenderDispatcher().setRenderHitBoxes(updateDebugSetting(yamlData, name, default_value));

        name = "key.debugF3+H";
        default_value = options.advancedItemTooltips;
        if(!defaultKeys.containsKey(name)){
            defaultKeys.put(name, default_value);
        }
        options.advancedItemTooltips = updateDebugSetting(yamlData, name, options.advancedItemTooltips);
    }

    public static void reset() {
        Minecraft minecraft = Minecraft.getInstance();
        Options options = minecraft.options;

        String name = "key.debugF1";
        options.hideGui = defaultKeys.getOrDefault(name, options.hideGui);

        name = "key.debugF3";
        options.renderDebug = defaultKeys.getOrDefault(name, options.renderDebug);
        options.renderDebugCharts = false;
        options.renderFpsChart = false;

        name = "key.debugF3+B";
        minecraft.getEntityRenderDispatcher().setRenderHitBoxes(defaultKeys.getOrDefault(name, minecraft.getEntityRenderDispatcher().shouldRenderHitBoxes()));

        name = "key.debugF3+H";
        options.advancedItemTooltips = defaultKeys.getOrDefault(name, options.advancedItemTooltips);

        defaultKeys.clear();
    }

    private static boolean updateDebugSetting(Map<String, Map<String, String>> yamlData, String name, boolean default_value){
        if(yamlData.containsKey(name)){
            Map<String, String> stringMap = yamlData.get(name);
            if(stringMap.containsKey("unlock")){
                boolean unlocked = Boolean.parseBoolean(stringMap.get("unlock"));
                if(!unlocked){
                    if(stringMap.containsKey("value")){
                        return Boolean.parseBoolean(stringMap.get("value"));

                    }
                }
            }
        }
        return default_value;
    }
}
