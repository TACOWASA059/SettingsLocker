package com.github.tacowasa059.settingslocker.client;

import com.github.tacowasa059.settingslocker.SettingsLocker;
import com.github.tacowasa059.settingslocker.client.utils.UpdateDebugSetting;
import com.github.tacowasa059.settingslocker.client.utils.UpdateOptionSetting;
import com.github.tacowasa059.settingslocker.client.utils.UpdateKeySetting;
import com.github.tacowasa059.settingslocker.client.utils.YamlLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = SettingsLocker.MODID, value = Dist.CLIENT)
public class ClientEventListener {

    @SubscribeEvent
    public static void onLoggedOut(ClientPlayerNetworkEvent.LoggingOut event){
        YamlLoader.reset();
        UpdateKeySetting.reset();
        UpdateDebugSetting.reset();
        UpdateOptionSetting.reset();
    }
}
