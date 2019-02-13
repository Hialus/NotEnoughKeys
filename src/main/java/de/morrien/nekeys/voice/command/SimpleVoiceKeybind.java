package de.morrien.nekeys.voice.command;

import net.minecraft.client.settings.KeyBinding;

/**
 * Created by Timor Morrien
 */
public class SimpleVoiceKeybind extends AbstractVoiceKeybind implements IVoiceCommandTickable {

    private int ticksLeft;

    private SimpleVoiceKeybind() {}

    public SimpleVoiceKeybind(String name, String command, KeyBinding keybind) {
        super(name, command, keybind);
    }

    @Override
    public void activate(String voiceCommand) {
        if (keybind == null) return;
        KeyBinding.onTick(keybind.getKeyCode());
        KeyBinding.setKeyBindState(keybind.getKeyCode(), true);
        ticksLeft = 1;
    }

    @Override
    public void tick() {
        if (keybind != null && ticksLeft >= 0) {
            ticksLeft--;
            if (ticksLeft == 0) {
                KeyBinding.setKeyBindState(keybind.getKeyCode(), false);
            }
        }
    }
}
