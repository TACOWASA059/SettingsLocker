package com.github.tacowasa059.settingslocker.mixin;

import com.github.tacowasa059.settingslocker.client.utils.CurrentScreenIdentifier;
import com.github.tacowasa059.settingslocker.client.utils.YamlLoader;
import com.github.tacowasa059.settingslocker.interfaces.AbstractWidgetAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AbstractWidget.class)
public abstract class AbstractWidgetMixin implements AbstractWidgetAccessor {

    @Unique
    private String settingsLocker$StringKey = "";

    @Shadow public boolean active;
    @Shadow protected abstract boolean clicked(double p_93681_, double p_93682_);

    @Inject(method = "<init>", at=@At("TAIL"))
    public void onInit(int p_93629_, int p_93630_, int p_93631_, int p_93632_, Component component, CallbackInfo ci){
        if(!component.equals(Component.empty())){
            if (component.getContents() instanceof TranslatableContents translatableContents) {
                String key = translatableContents.getKey();
                String generic_string = "options.generic_value";
                if(key.equals(generic_string)){
                    Object[] args = translatableContents.getArgs();
                    if (args[0] instanceof Component componentArg) {
                        if (componentArg.getContents() instanceof TranslatableContents translatableContents1) {
                            key = translatableContents1.getKey();
                        }
                    }
                }

                // キーバインディングでは使用しない
                if(!CurrentScreenIdentifier.isKeyBindScreen()){
                    Map<String, String> map = YamlLoader.get(key);
                    if((!key.startsWith("key.")) && map!=null && !Boolean.parseBoolean(map.getOrDefault("unlock", "true"))){
                        active = false;
                    }else if(key.startsWith("key.")){
                        if(!key.endsWith(".toggle")) key += ".toggle"; //toggleで終わらないときは足す
                        map = YamlLoader.get(key);
                        if(map!=null && !Boolean.parseBoolean(map.getOrDefault("unlock", "true"))){
                            active = false;
                        }
                    }
//                        System.out.println("Abstract Widget:"+key);
                }
                settingsLocker$StringKey = key;
            }
        }
    }

    @Override
    public String settingsLocker$getStringKey() {
        return settingsLocker$StringKey;
    }
    @Override
    public void settingsLocker$setStringKey(String key){
        settingsLocker$StringKey = key;
    }

    @Inject(method = "mouseClicked",at=@At("HEAD"),cancellable = true)
    public void mouseClicked(double p_93641_, double p_93642_, int p_93643_, CallbackInfoReturnable<Boolean> cir) {
        AbstractWidget abstractWidget = (AbstractWidget) (Object)this;
        if (this.active && abstractWidget.visible) {
            if (p_93643_ == 0) {
                boolean flag = clicked(p_93641_, p_93642_);
                if (flag) {
                    abstractWidget.playDownSound(Minecraft.getInstance().getSoundManager());
                    abstractWidget.onClick(p_93641_, p_93642_);
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
            }else if(p_93643_ == 1){
                boolean flag = clicked(p_93641_, p_93642_);
                if (flag && !settingsLocker$StringKey.equalsIgnoreCase("")) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.NOTE_BLOCK_BELL, 1.0f));
                    Minecraft.getInstance().keyboardHandler.setClipboard(settingsLocker$StringKey);
                    abstractWidget.setFocused(false);
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                }
            }

            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(false);
        }
        cir.cancel();
    }
}
