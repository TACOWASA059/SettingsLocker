package com.github.tacowasa059.settingslocker.mixin;

import com.github.tacowasa059.settingslocker.client.utils.YamlLoader;
import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.*;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private long debugCrashKeyTime = -1L;
    @Shadow
    private long debugCrashKeyReportedTime = -1L;
    @Shadow
    private long debugCrashKeyReportedCount = -1L;
    @Shadow
    private boolean handledDebugKey;
    @Shadow
    protected abstract boolean handleDebugKeys(int p_90933_);
    @Shadow
    protected abstract void debugFeedbackComponent(Component p_167823_);
    @Shadow
    protected abstract void debugFeedbackTranslated(String p_90914_, Object... p_90915_);

    @Shadow
    public abstract void setClipboard(String p_90912_);
    @Shadow
    public abstract void copyRecreateCommand(boolean p_90929_, boolean p_90930_);

    @Inject(method="keyPress",at=@At("HEAD"),cancellable = true)
    public void keyPress(long p_90894_, int key, int p_90896_, int p_90897_, int p_90898_, CallbackInfo ci) {
        if (p_90894_ == this.minecraft.getWindow().getWindow()) {
            if (this.debugCrashKeyTime > 0L) {
                if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 67) || !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292)) {
                    this.debugCrashKeyTime = -1L;
                }
            } else if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 67) && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292)) {
                this.handledDebugKey = true;
                this.debugCrashKeyTime = Util.getMillis();
                this.debugCrashKeyReportedTime = Util.getMillis();
                this.debugCrashKeyReportedCount = 0L;
            }

            Screen screen = this.minecraft.screen;
            if (screen != null) {
                switch (key) {
                    case 258:
                        this.minecraft.setLastInputType(InputType.KEYBOARD_TAB);
                    case 259:
                    case 260:
                    case 261:
                    default:
                        break;
                    case 262:
                    case 263:
                    case 264:
                    case 265:
                        this.minecraft.setLastInputType(InputType.KEYBOARD_ARROW);
                }
            }

            if (p_90897_ == 1 && (!(this.minecraft.screen instanceof KeyBindsScreen) || ((KeyBindsScreen)screen).lastKeySelection <= Util.getMillis() - 20L)) {
                if (this.minecraft.options.keyFullscreen.matches(key, p_90896_)) {
                    this.minecraft.getWindow().toggleFullScreen();
                    this.minecraft.options.fullscreen().set(this.minecraft.getWindow().isFullscreen());
                    ci.cancel();
                    return;
                }

                if (this.minecraft.options.keyScreenshot.matches(key, p_90896_)) {
                    if (Screen.hasControlDown()) {
                    }

                    Screenshot.grab(this.minecraft.gameDirectory, this.minecraft.getMainRenderTarget(), (p_90917_) -> {
                        this.minecraft.execute(() -> {
                            this.minecraft.gui.getChat().addMessage(p_90917_);
                        });
                    });
                    ci.cancel();
                    return;
                }
            }

            if (this.minecraft.getNarrator().isActive()) { //narrator
                boolean flag = screen == null || !(screen.getFocused() instanceof EditBox) || !((EditBox)screen.getFocused()).canConsumeInput();
                if (p_90897_ != 0 && key == 66 && Screen.hasControlDown() && flag) {
                    boolean flag1 = this.minecraft.options.narrator().get() == NarratorStatus.OFF;
                    this.minecraft.options.narrator().set(NarratorStatus.byId(this.minecraft.options.narrator().get().getId() + 1));
                    if (screen instanceof SimpleOptionsSubScreen) {
                        ((SimpleOptionsSubScreen)screen).updateNarratorButton();
                    }

                    if (flag1 && screen != null) {
                        screen.narrationEnabled();
                    }
                }
            }

            if (screen != null) {
                boolean[] aboolean = new boolean[]{false};
                Screen.wrapScreenError(() -> {
                    if (p_90897_ != 1 && p_90897_ != 2) {
                        if (p_90897_ == 0) {
                            aboolean[0] = net.minecraftforge.client.ForgeHooksClient.onScreenKeyReleasedPre(screen, key, p_90896_, p_90898_);
                            if (!aboolean[0]) aboolean[0] = screen.keyReleased(key, p_90896_, p_90898_);
                            if (!aboolean[0]) aboolean[0] = net.minecraftforge.client.ForgeHooksClient.onScreenKeyReleasedPost(screen, key, p_90896_, p_90898_);
                        }
                    } else {
                        screen.afterKeyboardAction();
                        aboolean[0] = net.minecraftforge.client.ForgeHooksClient.onScreenKeyPressedPre(screen, key, p_90896_, p_90898_);
                        if (!aboolean[0]) aboolean[0] = screen.keyPressed(key, p_90896_, p_90898_);
                        if (!aboolean[0]) aboolean[0] = net.minecraftforge.client.ForgeHooksClient.onScreenKeyPressedPost(screen, key, p_90896_, p_90898_);
                    }

                }, "keyPressed event handler", screen.getClass().getCanonicalName());
                if (aboolean[0]) {
                    ci.cancel();
                    return;
                }
            }

            if (this.minecraft.screen == null) {
                InputConstants.Key inputconstants$key = InputConstants.getKey(key, p_90896_);
                if (p_90897_ == 0) {
                    KeyMapping.set(inputconstants$key, false);
                    if (key == 292) {
                        if (this.handledDebugKey) {
                            this.handledDebugKey = false;
                        }
                        else {
                            String name = "key.debugF3";
                            Map<String, String> map = YamlLoader.get(name);
                            if (map == null || Boolean.parseBoolean(map.getOrDefault("unlock", "true"))) {
                                this.minecraft.options.renderDebug = !this.minecraft.options.renderDebug;
                                this.minecraft.options.renderDebugCharts = this.minecraft.options.renderDebug && Screen.hasShiftDown();
                                this.minecraft.options.renderFpsChart = this.minecraft.options.renderDebug && Screen.hasAltDown();
                            } //キーがないもしくはactive: trueのときにそのまま
                            else{
                                sendRestrictionFeedback();
                            }
                        }
                    }
                } else {
                    if (key == 293 && this.minecraft.gameRenderer != null) {
                        this.minecraft.gameRenderer.togglePostEffect();
                    }

                    boolean flag3 = false;
                    if (key == 256) {
                        boolean flag2 = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292);
                        this.minecraft.pauseGame(flag2);
                    }

                    flag3 = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 292) && this.handleDebugKeys(key);
                    this.handledDebugKey |= flag3;
                    if (key == 290) { //GUIの表示/非表示
                        //
                        String name = "key.debugF1";
                        Map<String, String> map = YamlLoader.get(name);
                        if (map == null || Boolean.parseBoolean(map.getOrDefault("unlock", "true"))) {
                            this.minecraft.options.hideGui = !this.minecraft.options.hideGui;
                        }else{
                            sendRestrictionFeedback();
                        }

                    }

                    if (flag3) {
                        KeyMapping.set(inputconstants$key, false);
                    } else {
                        KeyMapping.set(inputconstants$key, true);
                        KeyMapping.click(inputconstants$key);
                    }

                    if (this.minecraft.options.renderDebugCharts && key >= 48 && key <= 57) {
                        this.minecraft.debugFpsMeterKeyPress(key - 48);
                    }
                }
            }
            net.minecraftforge.client.ForgeHooksClient.onKeyInput(key, p_90896_, p_90897_, p_90898_);
        }
        ci.cancel();
    }

    @Inject(method="handleDebugKeys", at=@At("HEAD"),cancellable = true)
    private void handleDebugKeys(int p_90933_, CallbackInfoReturnable<Boolean> cir) {
        if (this.debugCrashKeyTime > 0L && this.debugCrashKeyTime < Util.getMillis() - 100L) {
            cir.setReturnValue(true);
            cir.cancel();
            return;
        } else {
            switch (p_90933_) {
                case 65:
                    this.minecraft.levelRenderer.allChanged();
                    this.debugFeedbackTranslated("debug.reload_chunks.message");
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 66:
                    String name = "key.debugF3+B";
                    Map<String, String> map = YamlLoader.get(name);
                    if (map == null || Boolean.parseBoolean(map.getOrDefault("unlock", "true"))) {
                        boolean flag = !this.minecraft.getEntityRenderDispatcher().shouldRenderHitBoxes();
                        this.minecraft.getEntityRenderDispatcher().setRenderHitBoxes(flag);
                        this.debugFeedbackTranslated(flag ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
                    }else{
                        sendRestrictionFeedback();
                    }

                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 67:
                    if (this.minecraft.player.isReducedDebugInfo()) {
                        cir.setReturnValue(false);
                        cir.cancel();
                        return;
                    } else {
                        ClientPacketListener clientpacketlistener = this.minecraft.player.connection;
                        if (clientpacketlistener == null) {
                            cir.setReturnValue(false);
                            cir.cancel();
                            return;
                        }
                        name = "key.debugF3+C";
                        map = YamlLoader.get(name);
                        if (map == null || Boolean.parseBoolean(map.getOrDefault("active", "true"))) {
                            this.debugFeedbackTranslated("debug.copy_location.message");
                            this.setClipboard(String.format(Locale.ROOT, "/execute in %s run tp @s %.2f %.2f %.2f %.2f %.2f", this.minecraft.player.level().dimension().location(), this.minecraft.player.getX(), this.minecraft.player.getY(), this.minecraft.player.getZ(), this.minecraft.player.getYRot(), this.minecraft.player.getXRot()));
                        }else{
                            sendRestrictionFeedback();
                        }
                        cir.setReturnValue(true);
                        cir.cancel();
                        return;
                    }
                case 68:
                    if (this.minecraft.gui != null) {
                        this.minecraft.gui.getChat().clearMessages(false);
                    }

                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 71:
                    name = "key.debugF3+G";
                    map = YamlLoader.get(name);
                    if (map == null || Boolean.parseBoolean(map.getOrDefault("active", "true"))) {
                        boolean flag1 = this.minecraft.debugRenderer.switchRenderChunkborder();
                        this.debugFeedbackTranslated(flag1 ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
                    }else{
                        sendRestrictionFeedback();
                    }
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 72:
                    name = "key.debugF3+H";
                    map = YamlLoader.get(name);
                    if (map == null || Boolean.parseBoolean(map.getOrDefault("unlock", "true"))) {
                        this.minecraft.options.advancedItemTooltips = !this.minecraft.options.advancedItemTooltips;
                        this.debugFeedbackTranslated(this.minecraft.options.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
                        this.minecraft.options.save();
                    }else{
                        sendRestrictionFeedback();
                    }

                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 73:
                    if (!this.minecraft.player.isReducedDebugInfo()) {
                        name = "key.debugF3+I";
                        map = YamlLoader.get(name);
                        if (map == null || Boolean.parseBoolean(map.getOrDefault("active", "true"))) {
                            this.copyRecreateCommand(this.minecraft.player.hasPermissions(2), !Screen.hasShiftDown());
                        }else{
                            sendRestrictionFeedback();
                        }
                    }

                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 76:
                    if (this.minecraft.debugClientMetricsStart(this::debugFeedbackComponent)) {
                        this.debugFeedbackTranslated("debug.profiling.start", 10);
                    }

                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 78:
                    if (!this.minecraft.player.hasPermissions(2)) {
                        this.debugFeedbackTranslated("debug.creative_spectator.error");
                    } else if (!this.minecraft.player.isSpectator()) {
                        this.minecraft.player.connection.sendUnsignedCommand("gamemode spectator");
                    } else {
                        this.minecraft.player.connection.sendUnsignedCommand("gamemode " + MoreObjects.firstNonNull(this.minecraft.gameMode.getPreviousPlayerMode(), GameType.CREATIVE).getName());
                    }

                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 80:
                    this.minecraft.options.pauseOnLostFocus = !this.minecraft.options.pauseOnLostFocus;
                    this.minecraft.options.save();
                    this.debugFeedbackTranslated(this.minecraft.options.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off");
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 81:
                    this.debugFeedbackTranslated("debug.help.message");
                    ChatComponent chatcomponent = this.minecraft.gui.getChat();
                    chatcomponent.addMessage(Component.translatable("debug.reload_chunks.help"));
                    chatcomponent.addMessage(Component.translatable("debug.show_hitboxes.help"));
                    chatcomponent.addMessage(Component.translatable("debug.copy_location.help"));
                    chatcomponent.addMessage(Component.translatable("debug.clear_chat.help"));
                    chatcomponent.addMessage(Component.translatable("debug.chunk_boundaries.help"));
                    chatcomponent.addMessage(Component.translatable("debug.advanced_tooltips.help"));
                    chatcomponent.addMessage(Component.translatable("debug.inspect.help"));
                    chatcomponent.addMessage(Component.translatable("debug.profiling.help"));
                    chatcomponent.addMessage(Component.translatable("debug.creative_spectator.help"));
                    chatcomponent.addMessage(Component.translatable("debug.pause_focus.help"));
                    chatcomponent.addMessage(Component.translatable("debug.help.help"));
                    chatcomponent.addMessage(Component.translatable("debug.dump_dynamic_textures.help"));
                    chatcomponent.addMessage(Component.translatable("debug.reload_resourcepacks.help"));
                    chatcomponent.addMessage(Component.translatable("debug.pause.help"));
                    chatcomponent.addMessage(Component.translatable("debug.gamemodes.help"));
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 83:
                    Path path = this.minecraft.gameDirectory.toPath().toAbsolutePath();
                    Path path1 = TextureUtil.getDebugTexturePath(path);
                    this.minecraft.getTextureManager().dumpAllSheets(path1);
                    Component component = Component.literal(path.relativize(path1).toString()).withStyle(ChatFormatting.UNDERLINE).withStyle((p_276097_) -> p_276097_.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, path1.toFile().toString())));
                    this.debugFeedbackTranslated("debug.dump_dynamic_textures", component);
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 84:
                    this.debugFeedbackTranslated("debug.reload_resourcepacks.message");
                    this.minecraft.reloadResourcePacks();
                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                case 293:
                    if (!this.minecraft.player.hasPermissions(2)) {
                        this.debugFeedbackTranslated("debug.gamemodes.error");
                    } else {
                        this.minecraft.setScreen(new GameModeSwitcherScreen());
                    }

                    cir.setReturnValue(true);
                    cir.cancel();
                    return;
                default:
                    cir.setReturnValue(false);
                    cir.cancel();
            }
        }
    }

    @Unique
    private void sendRestrictionFeedback(){
        if(this.minecraft.player!=null){
            this.minecraft.player.sendSystemMessage(Component.literal(ChatFormatting.RED+"[settingslocker]")
                    .append(Component.translatable("settingslocker.restricted.message")));
        }
    }
}
