package com.github.tacowasa059.settingslocker.mixin;

import com.github.tacowasa059.settingslocker.interfaces.OptionInstanceInterface;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OptionInstance.class)
public class OptionInstanceMixin implements OptionInstanceInterface {
    @Final
    @Shadow Component caption;

    @Override
    public Component getCaption() {
        return caption;
    }
}
