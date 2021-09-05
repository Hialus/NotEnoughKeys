package de.morrien.nekeys;

import com.mojang.blaze3d.systems.RenderSystem;
import de.morrien.nekeys.api.NekeysAPI;
import de.morrien.nekeys.gui.BetterButton;
import de.morrien.nekeys.gui.voice.GuiVoiceCommand;
import de.morrien.nekeys.preset.PresetManager;
import de.morrien.nekeys.url.CustomStreamHandlerFactory;
import de.morrien.nekeys.url.ResourceHandler;
import de.morrien.nekeys.voice.VoiceHandler;
import de.morrien.nekeys.voice.command.*;
import de.morrien.nekeys.voice.command.psi.SelectPsiSlotVoiceCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.KeyBindingList;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Timor Morrien
 */
@Mod(Reference.MODID)
public class NotEnoughKeys {
    public static NotEnoughKeys instance;

    public static Logger logger = LogManager.getLogger();
    private static ControlsScreen currentGui;
    private static Button[] guiButtons = new Button[10];
    private static boolean needsInit = true;
    public Path configDirectory;
    public VoiceHandler voiceHandler;
    public PresetManager presetManager;
    private boolean openGUI = false;

    public NotEnoughKeys() {
        instance = this;

        // This is necessary to enable Sphinx4 to load the custom minecraft-dictionary
        try {
            Class<URL> urlClass = URL.class;
            Field field = urlClass.getDeclaredField("factory");
            field.setAccessible(true);
            //URLStreamHandlerFactory oldFactory = (URLStreamHandlerFactory) field.get(null);

            CustomStreamHandlerFactory newFactory = new CustomStreamHandlerFactory("rsc", new ResourceHandler());
            field.set(null, newFactory);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Register Listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void sendLocalizedStatusMessage(String langCode) {
        sendStatusMessage(new TranslationTextComponent(langCode));
    }

    public void sendStatusMessage(String message) {
        sendStatusMessage(new StringTextComponent(message));
    }

    public void sendStatusMessage(ITextComponent message) {
        if (Minecraft.getInstance().player != null)
            Minecraft.getInstance().player.displayClientMessage(message, true);
    }

    @SubscribeEvent
    public void chat(ClientChatEvent event) {
        if (event.getMessage().equals("#voice")) {
            openGUI = true;
            event.setCanceled(true);
        }
    }

    public void setup(FMLClientSetupEvent event) {
        // Setup config
        configDirectory = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve(Reference.MODID).resolve("glfw");//event.getModConfigurationDirectory().toPath().resolve(Reference.MODID);
        try {
            if (!Files.exists(configDirectory)) {
                Files.createDirectories(configDirectory);
            }
        } catch (IOException e) {
            logger.warn("Unable to create config directory");
            logger.warn(e.toString());
        }
        // Initialize the VoiceHandler
        voiceHandler = new VoiceHandler();

        Keybindings.register();

        // Add all VoiceCommands
        NekeysAPI.addVoiceCommand(EmptyVoiceCommand.class, new EmptyVoiceCommand.Factory());
        NekeysAPI.addVoiceCommand(SimpleVoiceKeybind.class, new SimpleVoiceKeybind.Factory());
        NekeysAPI.addVoiceCommand(ToggleVoiceKeybind.class, new ToggleVoiceKeybind.Factory());
        NekeysAPI.addVoiceCommand(ChatVoiceCommand.class, new ChatVoiceCommand.Factory());
        NekeysAPI.addVoiceCommand(OpenGuiVoiceCommand.class, new OpenGuiVoiceCommand.Factory());
        NekeysAPI.addVoiceCommand(SelectPresetVoiceCommand.class, new SelectPresetVoiceCommand.Factory());
        //voiceHandler.bindPopup(VoicePressKey.class, PressKeyPopup.class);

        if (ModList.get().isLoaded("psi")) {
            NekeysAPI.addVoiceCommand(SelectPsiSlotVoiceCommand.class, new SelectPsiSlotVoiceCommand.Factory());
            logger.info("Enabled Psi integration for NotEnoughKeys");
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (openGUI) {
            Minecraft.getInstance().setScreen(new GuiVoiceCommand());
            openGUI = false;
        }
        Keybindings.processKeybindings();
        voiceHandler.tickUpdate();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if (Keybindings.PUSH_TO_TALK.isDown()) {
                Minecraft mc = Minecraft.getInstance();
                int posX = mc.getWindow().getGuiScaledWidth() - 15;
                int posY = mc.getWindow().getGuiScaledHeight() - 23;
                ResourceLocation microphone = new ResourceLocation(Reference.MODID, "textures/gui/microphone.png");

                mc.getTextureManager().bind(microphone);
                RenderSystem.color4f(1, 1, 1, 0.8f);
                mc.gui.blit(event.getMatrixStack(), posX, posY, 0, 0, 10, 18);
                RenderSystem.color4f(1, 1, 1, 1);
            }
        }
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof MainMenuScreen) {
            if (needsInit) {
                voiceHandler.init();
                presetManager = new PresetManager();
                try {
                    presetManager.loadFromConfig();
                } catch (IOException e) {
                    logger.error("Could not load preset config file");
                    logger.error(e.toString());
                }
                needsInit = false;
            }
        }
        if (event.getGui() instanceof ControlsScreen) {
            currentGui = (ControlsScreen) event.getGui();

            final StringTextComponent plus = new StringTextComponent("+");
            final StringTextComponent minus = new StringTextComponent("-");

            Button extenderButton = new BetterButton(currentGui.width / 2 - 5, 18, 10, 20, plus, button -> {
                for (Button guiButton : guiButtons) {
                    guiButton.visible = !guiButton.visible;
                }
                boolean enable = plus.equals(button.getMessage());
                button.setMessage(enable ? minus : plus);
                KeyBindingList keyBindingList = currentGui.controlList;
                keyBindingList.updateSize(
                        keyBindingList.getWidth(),
                        keyBindingList.getHeight(),
                        keyBindingList.getTop() + (enable ? 26 : -26),
                        keyBindingList.getBottom()
                );
                if (keyBindingList.getScrollAmount() > 26)
                    keyBindingList.setScrollAmount(keyBindingList.getScrollAmount() + (enable ? 26 : -26));
            }, (button, matrixStack, mouseX, mouseY) -> {
                currentGui.renderTooltip(matrixStack, Minecraft.getInstance().font.split(new TranslationTextComponent("gui.nekeys.extender.tooltip"), Math.max(currentGui.width / 2 - 43, 170)), mouseX, mouseY);
            });
            extenderButton.setFGColor(0x55FF55);
            event.addWidget(extenderButton);
            for (int i = 1; i <= 10; i++) {
                int presetID = i;
                Button button = new BetterButton(
                        currentGui.width / 2 - 155 + 65 + (i - 1) * 25,
                        42,
                        20,
                        20,
                        new StringTextComponent(String.valueOf(i)),
                        button1 -> {
                            PresetManager.Preset preset = presetManager.getPreset(presetID - 1);
                            preset.load();
                            for (Button guiButton : guiButtons) {
                                guiButton.setFGColor(0xFFFFFFFF);
                            }
                            button1.setFGColor(0x00FF00);
                        });
                guiButtons[i - 1] = button;
                button.visible = false;
                if (presetManager.getPreset(i - 1) == presetManager.getCurrentPreset()) {
                    button.setFGColor(0x00FF00);
                }
                event.addWidget(button);
            }
        } else if (currentGui != null) {
            presetManager.getCurrentPreset().update();
            currentGui = null;
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent event) {
        if (event.getGui() instanceof ControlsScreen) {
            if (guiButtons[0] != null && guiButtons[0].visible)
                Screen.drawString(
                        event.getMatrixStack(),
                        Minecraft.getInstance().font,
                        I18n.get("gui.nekeys.presets"),
                        event.getGui().width / 2 - 150,
                        48,
                        0xFFFFFF
                );
        }
    }
}
