package com.github.tacowasa059.settingslocker.server;

import com.github.tacowasa059.settingslocker.SettingsLocker;
import com.github.tacowasa059.settingslocker.client.utils.KeyNameConverter;
import com.github.tacowasa059.settingslocker.client.utils.YamlLoader;
import com.github.tacowasa059.settingslocker.networks.NetworkHandler;
import com.github.tacowasa059.settingslocker.networks.SyncSettingPacket;
import com.github.tacowasa059.settingslocker.networks.SyncYamlPacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;


@Mod.EventBusSubscriber(modid = SettingsLocker.MODID)
public class CommandRegistration {

    private static final Set<String> keySet = KeyNameConverter.getShortNameSet();
    @SubscribeEvent
    public static void commandRegister(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("locksettings")
                .requires(s -> s.hasPermission(2))
                .then(Commands.argument("targets", net.minecraft.commands.arguments.EntityArgument.players())
                    .then(Commands.argument("category", StringArgumentType.word()).suggests(CATEGORY_SUGGESTION)
                        .then(Commands.argument("path", StringArgumentType.word()).suggests(PATH_SUGGESTION)
                            .then(Commands.argument("value", StringArgumentType.word()).suggests(VALUE_SUGGESTION)
                                .executes(ctx -> lockSetting(ctx.getSource(), net.minecraft.commands.arguments.EntityArgument.getPlayers(ctx, "targets"),
                                    StringArgumentType.getString(ctx, "category"),
                                    StringArgumentType.getString(ctx, "path"),
                                    StringArgumentType.getString(ctx, "value"), false))
                            )
                        )
                    )
                )
                // `locksettings config load`
                .then(Commands.literal("config")
                        .then(Commands.literal("reload")
                                .executes(ctx -> {
                                    YamlLoader.loadYaml(ctx.getSource().getServer());
                                    // 全プレイヤーにパケットを送信
                                    NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncYamlPacket(YamlLoader.get()));
                                    ctx.getSource().sendSuccess(() -> Component.literal(ChatFormatting.GREEN+ "[settingslocker]" +ChatFormatting.WHITE+"Configuration reloaded!"), true);
                                    return 1;
                                })
                        )

                        // `locksettings config get <key>`
                        .then(Commands.literal("get")
                                .then(Commands.argument("key", StringArgumentType.word()).suggests(ALL_KEYS_SUGGESTION)
                                        .executes(ctx -> {
                                            String key = StringArgumentType.getString(ctx, "key");
                                            String value = key + " : " + YamlLoader.getOrDefault(key, new HashMap<>()).toString();
                                            ctx.getSource().sendSuccess(() -> Component.literal(ChatFormatting.GREEN+ "[settingslocker] " +ChatFormatting.AQUA+ value), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(Commands.literal("set")
                                .then(Commands.argument("category", StringArgumentType.word()).suggests(CATEGORY_SUGGESTION)
                                    .then(Commands.argument("path", StringArgumentType.word()).suggests(PATH_SUGGESTION)
                                        .then(Commands.argument("value", StringArgumentType.word()).suggests(VALUE_SUGGESTION)
                                            .executes(ctx -> lockSetting(ctx.getSource(), ctx.getSource().getServer().getPlayerList().getPlayers(),
                                                    StringArgumentType.getString(ctx, "category"),
                                                    StringArgumentType.getString(ctx, "path"),
                                                    StringArgumentType.getString(ctx, "value"), true))
                                        )
                                    )
                                )
                        )
                        .then(Commands.literal("save")
                                .executes(ctx -> {
                                    YamlLoader.saveToYaml();
                                    ctx.getSource().sendSuccess(() -> Component.literal(ChatFormatting.GREEN+ "[settingslocker]" +ChatFormatting.WHITE+"Configuration saved!"), true);
                                    return 1;
                                })
                        )
                )
        );
    }

    /**
     * 設定をロック/変更する
     */
    private static int lockSetting(CommandSourceStack src, Collection<ServerPlayer> targets, String category, String path, String value, boolean isGlobal) {
        // プレイヤーごとに変更用のpacketを送信する
        if(category.equalsIgnoreCase("key")){
            boolean hasKey = KeyNameConverter.hasKey(value);
            if(!hasKey) value = "unknown";
        }
        SyncSettingPacket packet = new SyncSettingPacket(path, category, value);
        for (ServerPlayer player : targets) {
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
        }
        String finalValue = value;
        src.sendSuccess(() -> Component.literal(ChatFormatting.GREEN+"[settingslocker] "+"Changed setting for Players " + targets.size() +
                ": "+ ChatFormatting.AQUA + path + " " +category + ChatFormatting.GREEN+" -> " + ChatFormatting.AQUA + finalValue), true);

        // サーバーデータも書き換え
        if(isGlobal){
            YamlLoader.addData(path, category, value);

            if(category.equalsIgnoreCase("key")||category.equalsIgnoreCase("value")){
                YamlLoader.addData(path, "unlock", "false");
            }
        }
        // サーバー側の設定もする
        return targets.size();
    }

    /**
     * カテゴリのサジェスト ("unlock", "key", "value", "active")
     */
    private static final SuggestionProvider<CommandSourceStack> CATEGORY_SUGGESTION = (context, builder) ->
            net.minecraft.commands.SharedSuggestionProvider.suggest(Arrays.asList("unlock", "key", "value", "active"), builder);

    /**
     * パスのサジェスト (カテゴリごと)
     */
    private static final SuggestionProvider<CommandSourceStack> PATH_SUGGESTION = (context, builder) -> {
        String category = StringArgumentType.getString(context, "category");
        Set<String> suggestedPaths = switch (category) {
            case "unlock" -> getKeysContaining("unlock");
            case "key" -> getKeysContaining("key");
            case "value" -> getKeysContaining("value");
            case "active" -> getKeysContaining("active");
            default -> Set.of();
        };
        return net.minecraft.commands.SharedSuggestionProvider.suggest(suggestedPaths, builder);
    };

    /**
     * 値のサジェスト
     */
    private static final SuggestionProvider<CommandSourceStack> VALUE_SUGGESTION = (context, builder) -> {
        String category = StringArgumentType.getString(context, "category");
        if (category.equals("unlock") || category.equals("active")) {
            return net.minecraft.commands.SharedSuggestionProvider.suggest(Arrays.asList("true", "false"), builder);
        } else if (category.equals("key")) {
            return net.minecraft.commands.SharedSuggestionProvider.suggest(keySet, builder);
        }
        return builder.buildFuture(); // "value" の場合はサジェストなし
    };

    private static final SuggestionProvider<CommandSourceStack> ALL_KEYS_SUGGESTION= ((context, builder) -> net.minecraft.commands.SharedSuggestionProvider.suggest(getAllKeys(), builder));

    /**
     * processedData の全てのキーを取得
     */
    public static Set<String> getAllKeys() {
        Map<String, Map<String, String>> map = YamlLoader.get();
        if(map==null) return new HashSet<>();
        return map.keySet();
    }

    /**
     * 指定した key を含む一つ目の key 一覧を取得
     */
    public static Set<String> getKeysContaining(String searchKey) {
        Map<String, Map<String, String>> map = YamlLoader.get();
        if(map==null) return new HashSet<>();
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().containsKey(searchKey))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
