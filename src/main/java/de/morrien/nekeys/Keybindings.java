package de.morrien.nekeys;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created by Timor Morrien
 */
public class Keybindings {

    public static final KeyBinding PUSH_TO_TALK = new KeyBinding("key.nekeys.push_to_talk", Keyboard.KEY_LMENU, "key.categories.nekeys");
    public static final KeyBinding PRESET_1 = new KeyBinding("key.nekeys.preset_1", Keyboard.KEY_NUMPAD1, "key.categories.nekeys");
    public static final KeyBinding PRESET_2 = new KeyBinding("key.nekeys.preset_2", Keyboard.KEY_NUMPAD2, "key.categories.nekeys");
    public static final KeyBinding PRESET_3 = new KeyBinding("key.nekeys.preset_3", Keyboard.KEY_NUMPAD3, "key.categories.nekeys");
    public static final KeyBinding PRESET_4 = new KeyBinding("key.nekeys.preset_4", Keyboard.KEY_NUMPAD4, "key.categories.nekeys");
    public static final KeyBinding PRESET_5 = new KeyBinding("key.nekeys.preset_5", Keyboard.KEY_NUMPAD5, "key.categories.nekeys");
    public static final KeyBinding PRESET_6 = new KeyBinding("key.nekeys.preset_6", Keyboard.KEY_NUMPAD6, "key.categories.nekeys");
    public static final KeyBinding PRESET_7 = new KeyBinding("key.nekeys.preset_7", Keyboard.KEY_NUMPAD7, "key.categories.nekeys");
    public static final KeyBinding PRESET_8 = new KeyBinding("key.nekeys.preset_8", Keyboard.KEY_NUMPAD8, "key.categories.nekeys");
    public static final KeyBinding PRESET_9 = new KeyBinding("key.nekeys.preset_9", Keyboard.KEY_NUMPAD9, "key.categories.nekeys");
    public static final KeyBinding PRESET_10 = new KeyBinding("key.nekeys.preset_10", Keyboard.KEY_NUMPAD0, "key.categories.nekeys");

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

    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (PRESET_1.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(0).load();
        if (PRESET_2.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(1).load();
        if (PRESET_3.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(2).load();
        if (PRESET_4.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(3).load();
        if (PRESET_5.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(4).load();
        if (PRESET_6.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(5).load();
        if (PRESET_7.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(6).load();
        if (PRESET_8.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(7).load();
        if (PRESET_9.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(8).load();
        if (PRESET_10.isPressed())
            NotEnoughKeys.instance.presetManager.getPreset(9).load();
    }
}
