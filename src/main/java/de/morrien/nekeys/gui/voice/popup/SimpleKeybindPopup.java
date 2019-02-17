package de.morrien.nekeys.gui.voice.popup;

import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.voice.command.SimpleVoiceKeybind;

/**
 * Created by Timor Morrien
 */
public class SimpleKeybindPopup extends AbstractKeybindPopup {

    public SimpleKeybindPopup(String name, String rule) {
        super(name, rule);
    }

    public SimpleKeybindPopup(SimpleVoiceKeybind voiceCommand) {
        super(voiceCommand);
    }

    @Override
    public IVoiceCommand getCommand() {
        return new SimpleVoiceKeybind(name, rule, keyBindingDropDown.selection);
    }
}
