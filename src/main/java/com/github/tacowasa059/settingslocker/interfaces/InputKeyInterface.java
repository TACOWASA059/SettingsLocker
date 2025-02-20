package com.github.tacowasa059.settingslocker.interfaces;

import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public interface InputKeyInterface {
    Int2ObjectMap<InputConstants.Key> getMap();
}
