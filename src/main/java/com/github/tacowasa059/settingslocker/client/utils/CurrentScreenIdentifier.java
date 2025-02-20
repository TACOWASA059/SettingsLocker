package com.github.tacowasa059.settingslocker.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.contents.TranslatableContents;

/**
 * 現在のスクリーンの判定用
 */
public class CurrentScreenIdentifier {

    public static boolean isKeyBindScreen(){
        String s = "controls.keybinds.title";
        return IsSameScreenTitle(s);
    }

    public static boolean IsSameScreenTitle(String s){
        Screen screen = Minecraft.getInstance().screen;
        if(screen==null) return false;
        if(screen.getTitle().getContents() instanceof TranslatableContents contents){
            return contents.getKey().equals(s);
        }
        return false;
    }
}
