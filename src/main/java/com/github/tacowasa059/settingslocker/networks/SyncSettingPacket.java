package com.github.tacowasa059.settingslocker.networks;

import com.github.tacowasa059.settingslocker.client.utils.UpdateDebugSetting;
import com.github.tacowasa059.settingslocker.client.utils.UpdateKeySetting;
import com.github.tacowasa059.settingslocker.client.utils.UpdateOptionSetting;
import com.github.tacowasa059.settingslocker.client.utils.YamlLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class SyncSettingPacket {
    private final String path;
    private final String category;
    private final String value;

    public SyncSettingPacket(String path, String category, String value) {
        this.path = path;
        this.category = category;
        this.value = value;
    }

    public static void encode(SyncSettingPacket packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.path);
        buf.writeUtf(packet.category);
        buf.writeUtf(packet.value);
    }

    public static SyncSettingPacket decode(FriendlyByteBuf buf) {
        return new SyncSettingPacket(buf.readUtf(), buf.readUtf(), buf.readUtf());
    }

    public static void handle(SyncSettingPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft.getInstance().execute(() -> {

                YamlLoader.addData(packet.path, packet.category, packet.value);
                if(packet.category.equalsIgnoreCase("key")||packet.category.equalsIgnoreCase("value")){
                    YamlLoader.addData(packet.path, "unlock", "false");
                }

                Map<String, Map<String, String>> map = YamlLoader.get();
                UpdateKeySetting.updateKeyMappings(map);
                UpdateDebugSetting.updateDebugSetting(map);
                UpdateOptionSetting.updateOptionSetting(map);

            });
        });
        ctx.get().setPacketHandled(true);
    }
}

