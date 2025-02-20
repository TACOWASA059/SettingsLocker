package com.github.tacowasa059.settingslocker.mixin;

import com.github.tacowasa059.settingslocker.interfaces.InputKeyInterface;
import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InputConstants.Type.class)
public class TypeMixin implements InputKeyInterface {

    @Final
    @Shadow
    private Int2ObjectMap<InputConstants.Key> map;

    @Override
    public Int2ObjectMap<InputConstants.Key> getMap() {
        return map;
    }
}
