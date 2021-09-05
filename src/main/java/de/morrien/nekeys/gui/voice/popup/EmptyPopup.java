package de.morrien.nekeys.gui.voice.popup;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.voice.command.EmptyVoiceCommand;

public class EmptyPopup extends AbstractPopup {

    public EmptyPopup(String name, String rule) {
        super(name, rule);
    }

    public EmptyPopup(EmptyVoiceCommand voiceCommand) {
        super(voiceCommand);
    }

    @Override
    public void draw(MatrixStack matrixStack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public IVoiceCommand getCommand() {
        return new EmptyVoiceCommand(name, rule);
    }
}
