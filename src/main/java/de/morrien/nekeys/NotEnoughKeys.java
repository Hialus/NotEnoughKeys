package de.morrien.nekeys;

import com.mojang.blaze3d.systems.RenderSystem;
import de.morrien.nekeys.api.NekeysAPI;
import de.morrien.nekeys.gui.KeyBindsScreenInjector;
import de.morrien.nekeys.gui.voice.GuiVoiceCommand;
import de.morrien.nekeys.preset.PresetManager;
import de.morrien.nekeys.url.NekeysURLStreamHandler;
import de.morrien.nekeys.voice.VoiceHandler;
import de.morrien.nekeys.voice.command.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;

/**
 * Created by Timor Morrien
 */
@Mod(Reference.MODID)
public class NotEnoughKeys {
    public static NotEnoughKeys instance;

    public static Logger logger = LogManager.getLogger();
    private static boolean needsInit = true;
    public Path configDirectory;
    public VoiceHandler voiceHandler;
    public PresetManager presetManager;
    private boolean openGUI = false;

    public NotEnoughKeys() {
        instance = this;

        // Register Listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(KeyBindsScreenInjector.class);

        try {
            Field handlersField = URL.class.getDeclaredField("handlers");
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            final Unsafe unsafe = (Unsafe) unsafeField.get(null);
            final Object staticFieldBase = unsafe.staticFieldBase(handlersField);
            final long staticFieldOffset = unsafe.staticFieldOffset(handlersField);

            var handlers = (Hashtable<String, URLStreamHandler>) unsafe.getObject(staticFieldBase, staticFieldOffset);
            handlers.put("rsc", new NekeysURLStreamHandler());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

        //if (ModList.get().isLoaded("psi")) {
        //    NekeysAPI.addVoiceCommand(SelectPsiSlotVoiceCommand.class, new SelectPsiSlotVoiceCommand.Factory());
        //    logger.info("Enabled Psi integration for NotEnoughKeys");
        //}
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

                RenderSystem.setShaderTexture(0, microphone);
                RenderSystem.setShaderColor(1, 1, 1, 0.8f);
                mc.gui.blit(event.getMatrixStack(), posX, posY, 0, 0, 10, 18);
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }
        }
    }

    @SubscribeEvent
    public void onGuiInit(ScreenEvent.InitScreenEvent.Post event) {
        if (event.getScreen() instanceof TitleScreen) {
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
    }
}
