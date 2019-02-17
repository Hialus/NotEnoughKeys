package de.morrien.nekeys.gui.voice.popup;

import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.voice.command.ToggleVoiceKeybind;

/**
 * Created by Timor Morrien
 */
public class ToggleKeybindPopup extends AbstractKeybindPopup {

    public ToggleKeybindPopup(String name, String rule) {
        super(name, rule);
    }

    public ToggleKeybindPopup(ToggleVoiceKeybind voiceCommand) {
        super(voiceCommand);
    }

    @Override
    public IVoiceCommand getCommand() {
        return new ToggleVoiceKeybind(name, rule, keyBindingDropDown.selection);
    }
}
