package com.github.tacowasa059.settingslocker.server;

import com.github.tacowasa059.settingslocker.SettingsLocker;
import com.github.tacowasa059.settingslocker.networks.NetworkHandler;
import com.github.tacowasa059.settingslocker.networks.SyncYamlPacket;
import com.github.tacowasa059.settingslocker.client.utils.YamlLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = SettingsLocker.MODID)
public class ServerEventListener {
    @SubscribeEvent
    public static void onServerStatingEvent(ServerStartingEvent event){
        MinecraftServer server = event.getServer();
        YamlLoader.loadYaml(server);
    }

    @SubscribeEvent
    public static void onServerJoinEvent(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        if(player instanceof  ServerPlayer serverPlayer){
            SyncYamlPacket packet = new SyncYamlPacket(YamlLoader.get());
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
            player.sendSystemMessage(Component.literal(ChatFormatting.GREEN+"[settingslocker]")
                    .append(Component.translatable("settingslocker.playerjoin.message")));
        }
    }
}
