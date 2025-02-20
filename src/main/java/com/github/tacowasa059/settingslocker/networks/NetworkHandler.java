package com.github.tacowasa059.settingslocker.networks;

import com.github.tacowasa059.settingslocker.SettingsLocker;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new net.minecraft.resources.ResourceLocation(SettingsLocker.MODID, "settings_locker_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, SyncYamlPacket.class, SyncYamlPacket::encode, SyncYamlPacket::decode, SyncYamlPacket::handle);
        CHANNEL.registerMessage(id++, SyncSettingPacket.class, SyncSettingPacket::encode, SyncSettingPacket::decode, SyncSettingPacket::handle);
    }
}