package de.morrien.nekeys.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.morrien.nekeys.NotEnoughKeys;
import de.morrien.nekeys.Reference;
import de.morrien.nekeys.gui.voice.GuiVoiceCommand;
import de.morrien.nekeys.preset.PresetManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class KeyBindsScreenInjector {
    private static final TextComponent plus = new TextComponent("+");
    private static final TextComponent minus = new TextComponent("-");
    private static final Button[] guiButtons = new Button[10];
    private static KeyBindsScreen currentKeyBindsScreen;

    @SubscribeEvent
    public static void onGuiInit(ScreenEvent.InitScreenEvent.Post event) {
        var presetManager = NotEnoughKeys.instance.presetManager;

        if (event.getScreen() instanceof KeyBindsScreen) {
            currentKeyBindsScreen = (KeyBindsScreen) event.getScreen();

            Button extenderButton = createExtenderButton();
            event.addListener(extenderButton);
            for (int i = 1; i <= 10; i++) {
                Button button = createPresetButton(presetManager, i);
                guiButtons[i - 1] = button;
                button.visible = false;
                if (presetManager.getPreset(i - 1) == presetManager.getCurrentPreset()) {
                    button.setFGColor(0x00FF00);
                }
                event.addListener(button);
            }

            var voiceCommandsButton = createVoiceCommandsButton();
            event.addListener(voiceCommandsButton);
        } else if (currentKeyBindsScreen != null) {
            presetManager.getCurrentPreset().update();
            currentKeyBindsScreen = null;
        }
    }

    @NotNull
    private static Button createPresetButton(PresetManager presetManager, int presetID) {
        return new ScalableButton(
                currentKeyBindsScreen.width / 2 - 155 + 71 + (presetID - 1) * 28,
                21,
                20,
                16,
                new TextComponent(String.valueOf(presetID)),
                button1 -> {
                    PresetManager.Preset preset = presetManager.getPreset(presetID - 1);
                    preset.load();
                    for (Button guiButton : guiButtons) {
                        guiButton.setFGColor(0xFFFFFFFF);
                    }
                    button1.setFGColor(0x00FF00);
                });
    }

    @NotNull
    private static Button createExtenderButton() {
        Button extenderButton = new ScalableButton(currentKeyBindsScreen.width / 2 - 151, 5, 14, 14, plus, KeyBindsScreenInjector::togglePresetButtons, KeyBindsScreenInjector::renderExtenderButtonTooltip);
        extenderButton.setFGColor(0x55FF55);
        return extenderButton;
    }

    private static void togglePresetButtons(Button button) {
        for (Button guiButton : guiButtons) {
            guiButton.visible = !guiButton.visible;
        }
        boolean enable = plus.equals(button.getMessage());
        button.setMessage(enable ? minus : plus);
        KeyBindsList keyBindingList = currentKeyBindsScreen.keyBindsList;
        keyBindingList.updateSize(
                keyBindingList.getWidth(),
                keyBindingList.getHeight(),
                keyBindingList.getTop() + (enable ? 20 : -20),
                keyBindingList.getBottom()
        );
        if (keyBindingList.getScrollAmount() > 20) {
            keyBindingList.setScrollAmount(keyBindingList.getScrollAmount() + (enable ? 26 : -26));
        }
    }

    private static void renderExtenderButtonTooltip(Button button, PoseStack matrixStack, int mouseX, int mouseY) {
        currentKeyBindsScreen.renderTooltip(
                matrixStack,
                Minecraft.getInstance().font.split(
                        new TranslatableComponent("gui.nekeys.extender.tooltip"),
                        Math.max(currentKeyBindsScreen.width / 2 - 43, 170)),
                mouseX,
                mouseY + 10
        );
    }

    @NotNull
    private static Button createVoiceCommandsButton() {
        var text = new TranslatableComponent("gui.nekeys.voice_commands.title");

        return new ScalableButton(currentKeyBindsScreen.width / 2 + 88, 5, 100, 14, text, KeyBindsScreenInjector::toggleVoiceCommandsButtons, KeyBindsScreenInjector::renderVoiceCommandsButtonTooltip);
    }

    private static void toggleVoiceCommandsButtons(Button button) {
        Minecraft.getInstance().setScreen(new GuiVoiceCommand(currentKeyBindsScreen));
    }

    private static void renderVoiceCommandsButtonTooltip(Button button, PoseStack matrixStack, int mouseX, int mouseY) {
        currentKeyBindsScreen.renderTooltip(
                matrixStack,
                Minecraft.getInstance().font.split(
                        new TranslatableComponent("gui.nekeys.voice_commands.popup"),
                        Math.max(currentKeyBindsScreen.width / 2 - 43, 170)),
                mouseX,
                mouseY + 10
        );
    }

    @SubscribeEvent
    public static void onGuiDraw(ScreenEvent.DrawScreenEvent event) {
        if (event.getScreen() instanceof KeyBindsScreen) {
            if (guiButtons[0] != null && guiButtons[0].visible)
                Screen.drawString(
                        event.getPoseStack(),
                        Minecraft.getInstance().font,
                        I18n.get("gui.nekeys.presets"),
                        event.getScreen().width / 2 - 150,
                        25,
                        0xFFFFFF
                );
        }
    }
}
