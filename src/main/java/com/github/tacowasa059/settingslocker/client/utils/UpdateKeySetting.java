package com.github.tacowasa059.settingslocker.client.utils;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

import java.util.HashMap;
import java.util.Map;

public class UpdateKeySetting {
    private static final Map<KeyMapping, InputConstants.Key> defaultKeys = new HashMap<>();
    public static void updateKeyMappings(Map<String, Map<String, String>> yamlData){
        Options options = Minecraft.getInstance().options;
        KeyMapping[] keyMappings = options.keyMappings;

        for(KeyMapping keyMapping : keyMappings){
            String name = keyMapping.getName();
            if(yamlData.containsKey(name)){
                InputConstants.Key default_key = keyMapping.getKey();

                if(!defaultKeys.containsKey(keyMapping)){
                    defaultKeys.put(keyMapping, default_key);
                } //lockの結果によらず保存

                Map<String, String> stringMap = yamlData.get(name);
                if(stringMap.containsKey("unlock")){
                    boolean unlocked = Boolean.parseBoolean(stringMap.get("unlock"));
                    if(!unlocked){

                        if(stringMap.containsKey("key")){
                            String keyName = stringMap.get("key");
                            String keyFullName = KeyNameConverter.toFullName(keyName);
                            InputConstants.Key inputKey = InputConstants.getKey(keyFullName);
                            keyMapping.setKey(inputKey);
                            KeyMapping.resetMapping();
                        }

                        if(name.equalsIgnoreCase("key.togglePerspective")){
                            String cameraType = stringMap.getOrDefault("cameraType", null);
                            if(cameraType!=null){
                                if(cameraType.equalsIgnoreCase("THIRD_PERSON_FRONT")){
                                    options.setCameraType(CameraType.THIRD_PERSON_FRONT);
                                }else if(cameraType.equalsIgnoreCase("THIRD_PERSON_BACK")){
                                    options.setCameraType(CameraType.THIRD_PERSON_BACK);
                                }else{
                                    options.setCameraType(CameraType.FIRST_PERSON);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void reset(){
        for(KeyMapping keyMapping : defaultKeys.keySet()){
            InputConstants.Key key = defaultKeys.get(keyMapping);
            keyMapping.setKey(key);
            KeyMapping.resetMapping();
        }
        defaultKeys.clear();
    }
}
