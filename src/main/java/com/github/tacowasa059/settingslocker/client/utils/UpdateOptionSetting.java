package com.github.tacowasa059.settingslocker.client.utils;

import net.minecraft.client.*;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;

import java.util.HashMap;
import java.util.Map;

public class UpdateOptionSetting {
    private static final Map<String, OptionInstance<?>> OptionMap = new HashMap<>();
    private static final Map<OptionInstance<?>, String> defaultKeys = new HashMap<>();

    private static final Map<OptionInstance<?>, Class<?>> optionTypes = new HashMap<>();
    public static void updateOptionSetting(Map<String, Map<String, String>> yamlData){
        Options options = Minecraft.getInstance().options;

        String name = "options.fov";
        OptionInstance<?> optionInstance = options.fov();
        addToMap(name, optionInstance, Integer.class);

        name = "options.mainHand";
        optionInstance = options.mainHand();
        addToMap(name, optionInstance, HumanoidArm.class);

        for(SoundSource source : SoundSource.values()){
            name = "soundCategory."+source.getName();
            optionInstance = options.getSoundSourceOptionInstance(source);
            addToMap(name, optionInstance, Double.class);
        }

        name = "options.showSubtitles";
        optionInstance = options.showSubtitles();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.directionalAudio";
        optionInstance = options.directionalAudio();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.graphics";
        optionInstance = options.graphicsMode();
        addToMap(name, optionInstance, GraphicsStatus.class);

        name = "options.prioritizeChunkUpdates";
        optionInstance = options.prioritizeChunkUpdates();
        addToMap(name, optionInstance, PrioritizeChunkUpdates.class);

        name = "options.renderDistance";
        optionInstance = options.renderDistance();
        addToMap(name, optionInstance, Integer.class);

        name = "options.ao";
        optionInstance = options.ambientOcclusion();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.simulationDistance";
        optionInstance = options.simulationDistance();
        addToMap(name, optionInstance, Integer.class);

        name = "options.biomeBlendRadius";
        optionInstance = options.biomeBlendRadius();
        addToMap(name, optionInstance, Integer.class);

        name ="options.framerateLimit";
        optionInstance = options.framerateLimit();
        addToMap(name, optionInstance, Integer.class);

        name = "options.vsync";
        optionInstance = options.enableVsync();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.viewBobbing";
        optionInstance = options.bobView();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.guiScale";
        optionInstance = options.guiScale();
        addToMap(name, optionInstance, Integer.class);

        name = "options.attackIndicator";
        optionInstance = options.attackIndicator();
        addToMap(name, optionInstance, AttackIndicatorStatus.class);

        name = "options.gamma";
        optionInstance = options.gamma();
        addToMap(name, optionInstance, Double.class);

        name = "options.renderClouds";
        optionInstance = options.cloudStatus();
        addToMap(name, optionInstance, CloudStatus.class);

        name = "options.fullscreen";
        optionInstance = options.fullscreen();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.particles";
        optionInstance = options.particles();
        addToMap(name, optionInstance, ParticleStatus.class);

        name = "options.mipmapLevels";
        optionInstance = options.mipmapLevels();
        addToMap(name, optionInstance, Integer.class);

        name = "options.entityShadows";
        optionInstance = options.entityShadows();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.screenEffectScale";
        optionInstance = options.screenEffectScale();
        addToMap(name, optionInstance, Double.class);

        name = "options.entityDistanceScaling";
        optionInstance = options.entityDistanceScaling();
        addToMap(name, optionInstance, Double.class);

        name = "options.fovEffectScale";
        optionInstance = options.fovEffectScale();
        addToMap(name, optionInstance, Double.class);

        name = "options.autosaveIndicator";
        optionInstance = options.showAutosaveIndicator();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.glintSpeed";
        optionInstance = options.glintSpeed();
        addToMap(name, optionInstance, Double.class);

        name = "options.glintStrength";
        optionInstance = options.glintStrength();
        addToMap(name, optionInstance, Double.class);

        name = "key.sneak.toggle";
        optionInstance = options.toggleCrouch();
        addToMap(name, optionInstance, Boolean.class);

        name = "key.sprint.toggle";
        optionInstance = options.toggleSprint();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.autoJump";
        optionInstance = options.autoJump();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.operatorItemsTab";
        optionInstance = options.operatorItemsTab();
        addToMap( name, optionInstance, Boolean.class);

        name = "options.sensitivity";
        optionInstance = options.sensitivity();
        addToMap(name, optionInstance, Double.class);

        name = "options.invertMouse";
        optionInstance = options.invertYMouse();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.mouseWheelSensitivity";
        optionInstance = options.mouseWheelSensitivity();
        addToMap(name, optionInstance, Double.class);

        name = "options.discrete_mouse_scroll";
        optionInstance = options.discreteMouseScroll();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.touchscreen";
        optionInstance = options.touchscreen();
        addToMap(name, optionInstance, Boolean.class);

        name ="options.rawMouseInput";
        optionInstance = options.rawMouseInput();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.chat.visibility";
        optionInstance = options.chatVisibility();
        addToMap(name, optionInstance, ChatVisiblity.class);

        name = "options.chat.color";
        optionInstance = options.chatColors();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.chat.links";
        optionInstance = options.chatLinks();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.chat.links.prompt";
        optionInstance = options.chatLinksPrompt();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.chat.scale";
        optionInstance = options.chatScale();
        addToMap(name, optionInstance, Double.class);

        name = "options.chat.width";
        optionInstance = options.chatWidth();
        addToMap(name, optionInstance, Double.class);

        name = "options.chat.height.focused";
        optionInstance = options.chatHeightFocused();
        addToMap(name, optionInstance, Double.class);

        name = "options.chat.height.unfocused";
        optionInstance = options.chatHeightUnfocused();
        addToMap(name, optionInstance, Double.class);

        name = "options.autoSuggestCommands";
        optionInstance = options.autoSuggestions();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.hideMatchedNames";
        optionInstance = options.hideMatchedNames();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.reducedDebugInfo";
        optionInstance = options.reducedDebugInfo();
        addToMap( name, optionInstance, Boolean.class);

        name = "options.onlyShowSecureChat";
        optionInstance = options.onlyShowSecureChat();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.narrator";
        optionInstance = options.narrator();
        addToMap(name, optionInstance, NarratorStatus.class);

        name = "options.accessibility.high_contrast";
        optionInstance = options.highContrast();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.accessibility.text_background_opacity";
        optionInstance = options.textBackgroundOpacity();
        addToMap(name, optionInstance, Double.class);

        name = "options.accessibility.text_background";
        optionInstance = options.backgroundForChatOnly();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.chat.opacity";
        optionInstance = options.chatOpacity();
        addToMap(name, optionInstance, Double.class);

        name = "options.chat.line_spacing";
        optionInstance = options.chatLineSpacing();
        addToMap(name, optionInstance, Double.class);

        name = "options.chat.delay_instant";
        optionInstance = options.chatDelay();
        addToMap(name, optionInstance, Double.class);

        name = "options.notifications.display_time";
        optionInstance = options.notificationDisplayTime();
        addToMap(name, optionInstance, Double.class);

        name = "options.darknessEffectScale";
        optionInstance = options.darknessEffectScale();
        addToMap(name, optionInstance, Double.class);

        name = "options.damageTiltStrength";
        optionInstance = options.damageTiltStrength();
        addToMap(name, optionInstance, Double.class);

        name = "options.hideLightningFlashes";
        optionInstance = options.hideLightningFlash();
        addToMap(name, optionInstance, Boolean.class);

        name = "options.darkMojangStudiosBackgroundColor";
        optionInstance = options.darkMojangStudiosBackground();
        addToMap( name, optionInstance, Boolean.class);

        name = "options.accessibility.panorama_speed";
        optionInstance = options.panoramaSpeed();
        addToMap(name, optionInstance, Double.class);


        if(yamlData==null)return;

        for(String key:yamlData.keySet()){
            OptionInstance<?> optionInstance1= OptionMap.get(key);
            if(optionInstance1==null) continue;

            Map<String,String> map = yamlData.get(key);
            if(map==null) continue;

            if(!Boolean.parseBoolean(map.getOrDefault("unlock", "true"))){
                String value = map.get("value");
                if(value!=null){

                    setToValue(optionInstance1, value);
                }
            }

        }
        options.broadcastOptions();
        options.save();

    }

    private static void setToValue(OptionInstance<?> optionInstance1, String value) {
        Class<?> expectedType = optionTypes.get(optionInstance1);
        if (expectedType == Boolean.class) {
            ((OptionInstance<Boolean>) optionInstance1).set(Boolean.parseBoolean(value));
        }else if (expectedType == Integer.class) {
            try {
                double tmp = Double.parseDouble(value);
                ((OptionInstance<Integer>) optionInstance1).set((int) Math.round(tmp));
            } catch (NumberFormatException e) {
                System.err.println("warning: " + value + " は整数に変換できません。");
            }
        } else if (expectedType == Double.class) {
            try {
                ((OptionInstance<Double>) optionInstance1).set(Double.parseDouble(value));
            } catch (NumberFormatException e) {
                System.err.println("warning: " + value + " は小数に変換できません。");
            }
        } else if (expectedType == HumanoidArm.class) {
            if(value.equalsIgnoreCase("left")){
                ((OptionInstance<HumanoidArm>) optionInstance1).set(HumanoidArm.LEFT);
            }else{
                ((OptionInstance<HumanoidArm>) optionInstance1).set(HumanoidArm.RIGHT);
            }
        } else if (expectedType == GraphicsStatus.class){
            if(value.equalsIgnoreCase("fancy")){
                ((OptionInstance<GraphicsStatus>) optionInstance1).set(GraphicsStatus.FANCY);
            }else if(value.equalsIgnoreCase("fast")){
                ((OptionInstance<GraphicsStatus>) optionInstance1).set(GraphicsStatus.FAST);
            }else{
                ((OptionInstance<GraphicsStatus>) optionInstance1).set(GraphicsStatus.FABULOUS);
            }
        } else if (expectedType == AttackIndicatorStatus.class){
            if(value.equalsIgnoreCase("off")){
                ((OptionInstance<AttackIndicatorStatus>) optionInstance1).set(AttackIndicatorStatus.OFF);
            }else if(value.equalsIgnoreCase("hotbar")){
                ((OptionInstance<AttackIndicatorStatus>) optionInstance1).set(AttackIndicatorStatus.HOTBAR);
            }else{
                ((OptionInstance<AttackIndicatorStatus>) optionInstance1).set(AttackIndicatorStatus.CROSSHAIR);
            }
        }else if (expectedType == CloudStatus.class){
            if(value.equalsIgnoreCase("off")){
                ((OptionInstance<CloudStatus>) optionInstance1).set(CloudStatus.OFF);
            }else if(value.equalsIgnoreCase("fast")){
                ((OptionInstance<CloudStatus>) optionInstance1).set(CloudStatus.FAST);
            }else{
                ((OptionInstance<CloudStatus>) optionInstance1).set(CloudStatus.FANCY);
            }
        } else if (expectedType == ParticleStatus.class){
            if(value.equalsIgnoreCase("DECREASED")){
                ((OptionInstance<ParticleStatus>) optionInstance1).set(ParticleStatus.DECREASED);
            }else if(value.equalsIgnoreCase("MINIMAL")){
                ((OptionInstance<ParticleStatus>) optionInstance1).set(ParticleStatus.MINIMAL);
            }else{
                ((OptionInstance<ParticleStatus>) optionInstance1).set(ParticleStatus.ALL);
            }
        } else if (expectedType == ChatVisiblity.class){
            if(value.equalsIgnoreCase("hidden")){
                ((OptionInstance<ChatVisiblity>) optionInstance1).set(ChatVisiblity.HIDDEN);
            }else if(value.equalsIgnoreCase("system")){
                ((OptionInstance<ChatVisiblity>) optionInstance1).set(ChatVisiblity.SYSTEM);
            }else{
                ((OptionInstance<ChatVisiblity>) optionInstance1).set(ChatVisiblity.FULL);
            }
        } else if (expectedType == NarratorStatus.class){
            if(value.equalsIgnoreCase("all")){
                ((OptionInstance<NarratorStatus>)optionInstance1).set(NarratorStatus.ALL);
            }else if(value.equalsIgnoreCase("system")){
                ((OptionInstance<NarratorStatus>)optionInstance1).set(NarratorStatus.SYSTEM);
            }else if(value.equalsIgnoreCase("chat")){
                ((OptionInstance<NarratorStatus>)optionInstance1).set(NarratorStatus.CHAT);
            }else{
                ((OptionInstance<NarratorStatus>)optionInstance1).set(NarratorStatus.OFF);
            }
        }else if (expectedType == PrioritizeChunkUpdates.class){
            if(value.equalsIgnoreCase("PLAYER_AFFECTED")){
                ((OptionInstance<PrioritizeChunkUpdates>)optionInstance1).set(PrioritizeChunkUpdates.PLAYER_AFFECTED);
            }else if(value.equalsIgnoreCase("NEARBY")){
                ((OptionInstance<PrioritizeChunkUpdates>)optionInstance1).set(PrioritizeChunkUpdates.NEARBY);
            }else{
                ((OptionInstance<PrioritizeChunkUpdates>)optionInstance1).set(PrioritizeChunkUpdates.NONE);
            }
        }
    }

    private static void addToMap(String name, OptionInstance<?> optionInstance, Class<?> value_class) {
        if(!OptionMap.containsKey(name)){
            OptionMap.put(name, optionInstance);
            defaultKeys.put(optionInstance, String.valueOf(optionInstance.get()));
            optionTypes.put(optionInstance, value_class);
        }
    }

    public static void reset(){
        for(OptionInstance<?> optionInstance1 : defaultKeys.keySet()){
            String value = defaultKeys.get(optionInstance1);
            setToValue(optionInstance1, value);
        }
        defaultKeys.clear();
        OptionMap.clear();
        optionTypes.clear();

        Minecraft.getInstance().options.save();
    }
}
