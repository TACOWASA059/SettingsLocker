package com.github.tacowasa059.settingslocker;

import com.github.tacowasa059.settingslocker.networks.NetworkHandler;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SettingsLocker.MODID)
public class SettingsLocker {

    public static final String MODID = "settingslocker";
    public SettingsLocker() {
        NetworkHandler.register();
    }
}
