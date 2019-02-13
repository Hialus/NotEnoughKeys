package de.morrien.nekeys.voice.command;

import net.minecraft.client.settings.KeyBinding;

/**
 * Created by Timor Morrien
 */
public class ToggleVoiceKeybind extends AbstractVoiceKeybind {

    private ToggleVoiceKeybind() {}

    public ToggleVoiceKeybind(String name, String command, KeyBinding keybind) {
        super(name, command, keybind);
    }

    @Override
    public void activate(String voiceCommand) {
        if (keybind != null)
            KeyBinding.setKeyBindState(keybind.getKeyCode(), !keybind.isKeyDown());
    }
}
