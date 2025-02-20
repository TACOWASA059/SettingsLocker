package com.github.tacowasa059.settingslocker.mixin;

import com.github.tacowasa059.settingslocker.client.utils.YamlLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(KeyBindsList.KeyEntry.class)
public class KeyEntryMixin {
    @Shadow @Final private Component name;
    @Shadow @Final private Button changeButton;
    @Shadow @Final private Button resetButton;


    /**
     * ボタンの有効化/無効化の設定
     * @param p_193915_
     * @param p_193916_
     * @param component
     * @param ci
     */
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void onCreate(KeyBindsList p_193915_, KeyMapping p_193916_, Component component, CallbackInfo ci)
    {

        changeKeyBindingButton();
    }

    @Inject(method = "refreshEntry", at=@At(value = "TAIL"))
    private void onUpdate(CallbackInfo ci)
    {
        changeKeyBindingButton();
    }

    @Unique
    private void changeKeyBindingButton() {
        if (name.getContents() instanceof TranslatableContents translatableContents) {
            String key = translatableContents.getKey();
            if(YamlLoader.contains(key)){
                Map<String, String> stringMap = YamlLoader.get(key);
                if(stringMap!=null && stringMap.containsKey("unlock")){
                    boolean unlock = Boolean.parseBoolean(stringMap.get("unlock"));
                    if(!unlock){
                        changeButton.active = false;
                        resetButton.active = false;
                    }
                }
            }
        }
    }

}
