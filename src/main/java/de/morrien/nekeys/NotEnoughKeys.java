package de.morrien.nekeys;

import de.morrien.nekeys.api.NekeysAPI;
import de.morrien.nekeys.gui.BetterButton;
import de.morrien.nekeys.gui.voice.GuiVoiceCommand;
import de.morrien.nekeys.preset.PresetManager;
import de.morrien.nekeys.url.CustomStreamHandlerFactory;
import de.morrien.nekeys.url.ResourceHandler;
import de.morrien.nekeys.voice.VoiceHandler;
import de.morrien.nekeys.voice.command.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent;
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
    private static GuiControls currentGui;
    private static GuiButton[] guiButtons = new GuiButton[10];
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onTick);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRenderGameOverlay);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onGuiInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onGuiDraw);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void sendLocalizedStatusMessage(String langCode) {
        sendStatusMessage(new TextComponentTranslation(langCode));
    }

    public void sendStatusMessage(String message) {
        sendStatusMessage(new TextComponentString(message));
    }

    public void sendStatusMessage(ITextComponent message) {

        if (Minecraft.getInstance().player != null)
            Minecraft.getInstance().player.sendStatusMessage(message, true);
    }

    @SubscribeEvent
    public void chat(ClientChatEvent event) {
        if (event.getMessage().equals("#voice")) {
            openGUI = true;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void setup(FMLClientSetupEvent event) {
        // Setup config
        configDirectory = Minecraft.getInstance().gameDir.toPath().resolve("config").resolve(Reference.MODID).resolve("glfw");//event.getModConfigurationDirectory().toPath().resolve(Reference.MODID);
        try {
            if (!Files.exists(configDirectory)) {
                Files.createDirectory(configDirectory);
            }
        } catch (IOException e) {
            logger.warn("Unable to create config directory");
            logger.warn(e.toString());
        }
        // Initialize the VoiceHandler
        voiceHandler = new VoiceHandler();

        // Add all VoiceCommands
        NekeysAPI.addVoiceCommand(EmptyVoiceCommand.class, new EmptyVoiceCommand.Factory());
        NekeysAPI.addVoiceCommand(SimpleVoiceKeybind.class, new SimpleVoiceKeybind.Factory());
        NekeysAPI.addVoiceCommand(ToggleVoiceKeybind.class, new ToggleVoiceKeybind.Factory());
        NekeysAPI.addVoiceCommand(ChatVoiceCommand.class, new ChatVoiceCommand.Factory());
        NekeysAPI.addVoiceCommand(OpenGuiVoiceCommand.class, new OpenGuiVoiceCommand.Factory());
        NekeysAPI.addVoiceCommand(SelectPresetVoiceCommand.class, new SelectPresetVoiceCommand.Factory());
        //voiceHandler.bindPopup(VoicePressKey.class, PressKeyPopup.class);

        // Add VoiceCommands for mods. Not active until mods are also ported to 1.13
        //if (Loader.isModLoaded("psi")) {
        //    NekeysAPI.addVoiceCommand(SelectPsiSlotVoiceCommand.class, new SelectPsiSlotVoiceCommand.Factory());
        //}
        //if (Loader.isModLoaded("thaumcraft")) {
        //    NekeysAPI.addVoiceCommand(SelectFocusVoiceCommand.class, new SelectFocusVoiceCommand.Factory());
        //}
        //
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (openGUI) {
            Minecraft.getInstance().displayGuiScreen(new GuiVoiceCommand());
            openGUI = false;
        }
        Keybindings.processKeybindings();
        voiceHandler.tickUpdate();
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            if (Keybindings.PUSH_TO_TALK.isKeyDown()) {
                Minecraft mc = Minecraft.getInstance();
                int posX = mc.mainWindow.getScaledWidth() - 15;
                int posY = mc.mainWindow.getScaledHeight() - 23;
                ResourceLocation microphone = new ResourceLocation(Reference.MODID, "textures/gui/microphone.png");

                mc.getTextureManager().bindTexture(microphone);
                GlStateManager.color4f(1, 1, 1, 0.8f);
                mc.ingameGUI.drawTexturedModalRect(posX, posY, 0, 0, 10, 18);
            }
        }
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiMainMenu) {
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
        if (event.getGui() instanceof GuiControls) {
            currentGui = (GuiControls) event.getGui();

            GuiButton extenderButton = new BetterButton(1010100, currentGui.width / 2 - 5, 18 + 24, 10, 20, "+") {

                @Override
                public void onClick(double mouseX, double mouseY) {
                    super.onClick(mouseX, mouseY);
                    for (GuiButton guiButton : guiButtons) {
                        guiButton.visible = !guiButton.visible;
                    }
                    boolean enable = "+".equals(displayString);
                    displayString = enable ? "-" : "+";
                    GuiKeyBindingList keyBindingList = ObfuscationReflectionHelper.getPrivateValue(GuiControls.class, (GuiControls) event.getGui(), "field_146494_r"); // "keyBindingList", "t",
                    keyBindingList.top += enable ? 26 : -26;
                }
            };
            extenderButton.packedFGColor = 0x55FF55;
            event.addButton(extenderButton);
            for (int i = 1; i <= 10; i++) {
                GuiButton button = new BetterButton(1010100 + i, currentGui.width / 2 - 155 + 65 + (i - 1) * 25, 66, 20, 20, String.valueOf(i)) {
                    @Override
                    public void onClick(double mouseX, double mouseY) {
                        super.onClick(mouseX, mouseY);
                        int presetID = id - 1010101;
                        PresetManager.Preset preset = presetManager.getPreset(presetID);
                        preset.load();
                        for (GuiButton guiButton : guiButtons) {
                            if (presetManager.getPreset(guiButton.id - 1010101) == presetManager.getCurrentPreset()) {
                                guiButton.packedFGColor = 0x00FF00;
                            } else {
                                guiButton.packedFGColor = 0xFFFFFFFF;
                            }
                        }
                    }
                };
                guiButtons[i - 1] = button;
                button.visible = false;
                if (presetManager.getPreset(i - 1) == presetManager.getCurrentPreset()) {
                    button.packedFGColor = 0x00FF00;
                }
                event.addButton(button);
            }
        } else if (currentGui != null) {
            presetManager.getCurrentPreset().update();
            currentGui = null;
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent event) {
        if (event.getGui() instanceof GuiControls) {
            if (guiButtons[0] != null && guiButtons[0].visible)
                event.getGui().drawString(Minecraft.getInstance().fontRenderer, I18n.format("gui.nekeys.presets"), event.getGui().width / 2 - 150, 72, 0xFFFFFF);
        }
    }
}
