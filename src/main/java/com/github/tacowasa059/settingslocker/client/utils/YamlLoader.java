package com.github.tacowasa059.settingslocker.client.utils;

import com.github.tacowasa059.settingslocker.SettingsLocker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.fml.loading.FMLPaths;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class YamlLoader {
    private static Map<String, Map<String, String>> processedData = new HashMap<>();

    private static final String CONFIG_FILE_NAME = "settingslocker.yml";
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE_NAME);
    public static void loadYaml(MinecraftServer server) {
        try {

            // `config/settingslocker.yml` が存在しない場合、デフォルトリソースからコピー
            if (!Files.exists(CONFIG_PATH)) {
                copyDefaultConfig(server);
            }


            InputStream inputStream = Files.newInputStream(CONFIG_PATH, StandardOpenOption.READ);
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(reader);

            processedData = data.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> ((Map<String, Object>) entry.getValue()).entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> String.valueOf(e.getValue())
                            ))
            ));

            reader.close();
            inputStream.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveToYaml() {
        // YAMLのフォーマットオプションを設定
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setPrettyFlow(true);

        Yaml yaml = new Yaml(options);
        String yamlContent = yaml.dump(processedData);

        // コメントを追加
        StringBuilder yamlWithComments = new StringBuilder();
        yamlWithComments.append("###############################\n");
        yamlWithComments.append("# unlock: trueのとき、何も変更しない\n");
        yamlWithComments.append("# unlock: falseのとき、keyを変更できなくする\n");
        yamlWithComments.append("#     keyが指定されていれば、指定されたkeyに割り当てる\n");
        yamlWithComments.append("#     keyが指定されていなければ、キー割り当てを変更しない\n");
        yamlWithComments.append("#     keyは大文字・小文字を区別しない\n");
        yamlWithComments.append("# modで追加されたキーを設定したい場合は、ボタンを右クリックする\n");
        yamlWithComments.append("#  or\n");
        yamlWithComments.append("# 対象modのlangファイルなどから対象となるパスを見つけてください。\n\n");
        yamlWithComments.append(yamlContent);

        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile(), StandardCharsets.UTF_8)) {
            writer.write(yamlWithComments.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyDefaultConfig(MinecraftServer server) {
        try {
            ResourceLocation resourceLocation = new ResourceLocation(SettingsLocker.MODID, "settingslocker.yml");
            ResourceManager resourceManager = server.getResourceManager();
            Resource resource = resourceManager.getResource(resourceLocation).orElse(null);

            if (resource != null) {
                try{
                    Files.createDirectories(CONFIG_PATH.getParent());
                }catch (IOException ignored){
                    System.out.println("Making config directory failed.");
                }

                Files.copy(resource.open(), CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Default config copied to: " + CONFIG_PATH);
            } else {
                System.err.println("Default settingslocker.yml not found in mod resources.");
            }
        } catch (IOException e) {
            System.err.println("Failed to copy default config: " + e.getMessage());
        }
    }
    public static Set<String> getKeys() {
        if(processedData==null) return null;
        return processedData.keySet();
    }

    public static boolean contains(String key){
        if(processedData==null) return false;
        return processedData.containsKey(key);
    }
    public static Map<String, String> get(String key){
        if(processedData==null) return null;
        return processedData.get(key);
    }
    public static Map<String, String> getOrDefault(String key, Map<String, String> mp){
        if(processedData==null) return mp;
        Map<String, String> data = processedData.get(key);
        if(data==null) return mp;
        return data;
    }

    public static Map<String, Map<String, String>> get(){
        if(processedData==null) return null;
        return processedData;
    }

    public static void setData(Map<String, Map<String, String>> newData) {
        processedData = newData;
    }

    public static void reset(){
        processedData = null;
    }

    public static boolean addData(String path, String category, String value) {
        processedData.computeIfAbsent(path, k -> new HashMap<>());

        Map<String, String> map = processedData.get(path);
        String oldValue = map.get(category);

        if ("value".equalsIgnoreCase(category)) {
            boolean oldIsNumber = oldValue != null && oldValue.matches("-?\\d+(\\.\\d+)?");
            boolean newIsNumber = value.matches("-?\\d+(\\.\\d+)?");

            if (oldIsNumber && !newIsNumber) {
                return false;
            }
        }


        map.put(category, value);
        return true;
    }


}
