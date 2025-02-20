package com.github.tacowasa059.settingslocker.mixin;

import com.github.tacowasa059.settingslocker.client.utils.YamlLoader;
import com.github.tacowasa059.settingslocker.interfaces.AbstractWidgetAccessor;
import com.github.tacowasa059.settingslocker.interfaces.OptionInstanceInterface;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Consumer;

@Mixin(OptionInstance.OptionInstanceSliderButton.class)
public abstract class OptionInstanceSliderButtonMixin {


    @Inject(method = "<init>", at = @At("RETURN"))
    private void onCreate(Options options, int x, int y, int width, int height, OptionInstance<?> instance,
                          OptionInstance.SliderableValueSet<?> values, OptionInstance.TooltipSupplier<?> tooltipSupplier, Consumer<?> onValueChanged, CallbackInfo ci) {
        OptionInstanceInterface instanceInterface = (OptionInstanceInterface) (Object)instance;

        Component component = instanceInterface.getCaption();
        if (component.getContents() instanceof TranslatableContents translatableContents) {
            String key = translatableContents.getKey();
            Map<String, String> map = YamlLoader.get(key);
            if((!key.startsWith("key.")) && map!=null && !Boolean.parseBoolean(map.getOrDefault("unlock", "true"))){
                OptionInstance.OptionInstanceSliderButton optionInstanceSliderButton = (OptionInstance.OptionInstanceSliderButton)(Object)this;
                optionInstanceSliderButton.active = false;
            }

            AbstractWidgetAccessor abstractWidgetAccessor = (AbstractWidgetAccessor) this;
            abstractWidgetAccessor.settingsLocker$setStringKey(key);

//            System.out.println("OptionSlider:"+key);
        }



    }
}
