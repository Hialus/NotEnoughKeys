package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.voice.popup.ToggleKeybindPopup;
import net.minecraft.client.KeyMapping;

/**
 * Created by Timor Morrien
 */
public class ToggleVoiceKeybind extends AbstractVoiceKeybind {

    private ToggleVoiceKeybind() {
    }

    public ToggleVoiceKeybind(String name, String command, KeyMapping keybind) {
        super(name, command, keybind);
    }

    @Override
    public void activate(String voiceCommand) {
        if (keybind != null)
            KeyMapping.set(keybind.getKey(), !keybind.isDown());
    }

    public static class Factory extends VoiceCommandFactory<ToggleVoiceKeybind> {

        @Override
        public ToggleVoiceKeybind newCommand(String[] params) {
            ToggleVoiceKeybind voiceCommand = new ToggleVoiceKeybind();
            voiceCommand.fromConfigParams(params);
            return voiceCommand;
        }

        @Override
        public ToggleVoiceKeybind newCommand(String name, String rule) {
            return new ToggleVoiceKeybind(name, rule, null);
        }

        @Override
        public AbstractPopup newPopup(ToggleVoiceKeybind command) {
            return new ToggleKeybindPopup(command);
        }
    }
}
