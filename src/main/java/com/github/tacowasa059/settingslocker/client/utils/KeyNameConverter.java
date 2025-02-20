package com.github.tacowasa059.settingslocker.client.utils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Set;

/**
 * キーに短縮名を振る
 */
public class KeyNameConverter {
    private static final BiMap<String, String> SHORT_NAMES = HashBiMap.create();

    static {
        // アルファベット・数字
        for (char c = 'a'; c <= 'z'; c++) registerKey("key.keyboard." + c, String.valueOf(Character.toUpperCase(c)));
        for (char c = '0'; c <= '9'; c++) registerKey("key.keyboard." + c, String.valueOf(c));

        // ファンクションキー
        for (int i = 1; i <= 25; i++) registerKey("key.keyboard.f" + i, "F" + i);

        // キーパッド
        for (int i = 0; i <= 9; i++) registerKey("key.keyboard.keypad." + i, "Num" + i);
        registerKey("key.keyboard.keypad.add", "Num+");
        registerKey("key.keyboard.keypad.subtract", "Num-");
        registerKey("key.keyboard.keypad.multiply", "Num*");
        registerKey("key.keyboard.keypad.divide", "Num/");
        registerKey("key.keyboard.keypad.decimal", "Num.");
        registerKey("key.keyboard.keypad.enter", "NumEnter");
        registerKey("key.keyboard.keypad.equal", "Num=");

        // 修飾キー
        registerKey("key.keyboard.left.shift", "LShift");
        registerKey("key.keyboard.right.shift", "RShift");
        registerKey("key.keyboard.left.control", "LCtrl");
        registerKey("key.keyboard.right.control", "RCtrl");
        registerKey("key.keyboard.left.alt", "LAlt");
        registerKey("key.keyboard.right.alt", "RAlt");
        registerKey("key.keyboard.left.win", "LWin");
        registerKey("key.keyboard.right.win", "RWin");

        // 矢印キー
        registerKey("key.keyboard.up", "Up");
        registerKey("key.keyboard.down", "Down");
        registerKey("key.keyboard.left", "Left");
        registerKey("key.keyboard.right", "Right");

        // 特殊キー
        registerKey("key.keyboard.space", "Space");
        registerKey("key.keyboard.enter", "Enter");
        registerKey("key.keyboard.tab", "Tab");
        registerKey("key.keyboard.backspace", "Backspace");
        registerKey("key.keyboard.delete", "Delete");
        registerKey("key.keyboard.insert", "Insert");
        registerKey("key.keyboard.escape", "Esc");
        registerKey("key.keyboard.page.up", "PageUp");
        registerKey("key.keyboard.page.down", "PageDown");
        registerKey("key.keyboard.home", "Home");
        registerKey("key.keyboard.end", "End");
        registerKey("key.keyboard.pause", "Pause");
        registerKey("key.keyboard.print.screen", "PrintScreen");
        registerKey("key.keyboard.num.lock", "NumLock");
        registerKey("key.keyboard.scroll.lock", "ScrollLock");
        registerKey("key.keyboard.caps.lock", "CapsLock");

        // 記号キー
        registerKey("key.keyboard.apostrophe", "'");
        registerKey("key.keyboard.backslash", "\\");
        registerKey("key.keyboard.comma", ",");
        registerKey("key.keyboard.equal", "=");
        registerKey("key.keyboard.grave.accent", "`");
        registerKey("key.keyboard.left.bracket", "[");
        registerKey("key.keyboard.right.bracket", "]");
        registerKey("key.keyboard.minus", "-");
        registerKey("key.keyboard.period", ".");
        registerKey("key.keyboard.semicolon", ";");
        registerKey("key.keyboard.slash", "/");

        // マウスボタン
        registerKey("key.mouse.left", "MouseLeft");
        registerKey("key.mouse.right", "MouseRight");
        registerKey("key.mouse.middle", "MouseMiddle");
        registerKey("key.mouse.4", "Mouse4");
        registerKey("key.mouse.5", "Mouse5");
        registerKey("key.mouse.6", "Mouse6");
        registerKey("key.mouse.7", "Mouse7");
        registerKey("key.mouse.8", "Mouse8");

        // その他
        registerKey("key.keyboard.unknown", "Unknown");
        registerKey("key.keyboard.world.1", "WorldKey1");
        registerKey("key.keyboard.world.2", "WorldKey2");
        registerKey("key.keyboard.menu", "Menu");
    }
    private static void registerKey(String fullKeyName, String shortKeyName) {
        SHORT_NAMES.put(fullKeyName.toLowerCase(), shortKeyName.toLowerCase());
    }


    /**
     * 短縮名をフルキー名に変換
     */
    public static String toFullName(String shortKeyName) {
        return SHORT_NAMES.inverse().getOrDefault(shortKeyName.toLowerCase(), "key.keyboard.unknown");
    }

    /**
     * 短縮キー名称の一覧を取得
     * @return 短縮キーのset
     */
    public static Set<String> getShortNameSet(){
        return SHORT_NAMES.values();
    }

    public static boolean hasKey(String name){
        return SHORT_NAMES.containsValue(name.toLowerCase());
    }
}
