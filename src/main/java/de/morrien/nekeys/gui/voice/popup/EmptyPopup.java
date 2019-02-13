package de.morrien.nekeys.gui.voice.popup;

import de.morrien.nekeys.voice.command.EmptyVoiceCommand;
import de.morrien.nekeys.voice.command.IVoiceCommand;

public class EmptyPopup extends AbstractPopup {

    public EmptyPopup(String name, String rule) {
        super(name, rule);
    }

    public EmptyPopup(EmptyVoiceCommand voiceCommand) {
        super(voiceCommand);
    }

    @Override
    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public IVoiceCommand getCommand() {
        return new EmptyVoiceCommand(name, rule);
    }
}
