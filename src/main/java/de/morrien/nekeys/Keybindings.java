package de.morrien.nekeys;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

/**
 * Created by Timor Morrien
 */
public class Keybindings {

    public static final KeyBinding PUSH_TO_TALK = new KeyBinding("key.nekeys.push_to_talk", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.nekeys");
    public static final KeyBinding PRESET_1 = new KeyBinding("key.nekeys.preset_1", GLFW.GLFW_KEY_KP_1, "key.categories.nekeys");
    public static final KeyBinding PRESET_2 = new KeyBinding("key.nekeys.preset_2", GLFW.GLFW_KEY_KP_2, "key.categories.nekeys");
    public static final KeyBinding PRESET_3 = new KeyBinding("key.nekeys.preset_3", GLFW.GLFW_KEY_KP_3, "key.categories.nekeys");
    public static final KeyBinding PRESET_4 = new KeyBinding("key.nekeys.preset_4", GLFW.GLFW_KEY_KP_4, "key.categories.nekeys");
    public static final KeyBinding PRESET_5 = new KeyBinding("key.nekeys.preset_5", GLFW.GLFW_KEY_KP_5, "key.categories.nekeys");
    public static final KeyBinding PRESET_6 = new KeyBinding("key.nekeys.preset_6", GLFW.GLFW_KEY_KP_6, "key.categories.nekeys");
    public static final KeyBinding PRESET_7 = new KeyBinding("key.nekeys.preset_7", GLFW.GLFW_KEY_KP_7, "key.categories.nekeys");
    public static final KeyBinding PRESET_8 = new KeyBinding("key.nekeys.preset_8", GLFW.GLFW_KEY_KP_8, "key.categories.nekeys");
    public static final KeyBinding PRESET_9 = new KeyBinding("key.nekeys.preset_9", GLFW.GLFW_KEY_KP_9, "key.categories.nekeys");
    public static final KeyBinding PRESET_10 = new KeyBinding("key.nekeys.preset_10", GLFW.GLFW_KEY_KP_0, "key.categories.nekeys");

    public static void register() {
        register(PUSH_TO_TALK);
        register(PRESET_1);
        register(PRESET_2);
        register(PRESET_3);
        register(PRESET_4);
        register(PRESET_5);
        register(PRESET_6);
        register(PRESET_7);
        register(PRESET_8);
        register(PRESET_9);
        register(PRESET_10);
    }

    private static void register(KeyBinding keyBinding) {
        ClientRegistry.registerKeyBinding(keyBinding);
    }

    public static void processKeybindings() {
        if (PRESET_1.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(0).load();
        if (PRESET_2.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(1).load();
        if (PRESET_3.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(2).load();
        if (PRESET_4.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(3).load();
        if (PRESET_5.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(4).load();
        if (PRESET_6.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(5).load();
        if (PRESET_7.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(6).load();
        if (PRESET_8.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(7).load();
        if (PRESET_9.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(8).load();
        if (PRESET_10.isDown())
            NotEnoughKeys.instance.presetManager.getPreset(9).load();
    }
}
