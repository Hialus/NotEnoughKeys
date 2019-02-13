package de.morrien.nekeys.gui.voice.popup;

import de.morrien.nekeys.gui.DropDownList;
import de.morrien.nekeys.voice.command.IVoiceCommand;
import de.morrien.nekeys.voice.command.SelectPresetVoiceCommand;

/**
 * Created by Timor Morrien
 */
public class SelectPresetPopup extends AbstractPopup {
    protected DropDownList<Integer> guiDropDown;

    public SelectPresetPopup(String name, String rule) {
        super(name, rule);
        init();
    }

    public SelectPresetPopup(SelectPresetVoiceCommand voiceCommand) {
        super(voiceCommand);
        init();
        guiDropDown.selection = voiceCommand.getPreset();
    }

    protected void init() {
        guiDropDown = new DropDownList<>(0, 0, 0 ,18, 6);
        for (int i = 1; i <= 10; i++) {
            guiDropDown.optionsList.add(i);
        }
    }

    @Override
    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        guiDropDown.x = x + 5;
        guiDropDown.y = y + 2;
        guiDropDown.width = width - 10;
        guiDropDown.draw();
    }

    @Override
    public boolean onClick(int mouseX, int mouseY) {
        if (guiDropDown.onClick(mouseX, mouseY)) return true;
        return super.onClick(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        guiDropDown.handleMouseInput();
    }

    @Override
    public IVoiceCommand getCommand() {
        return new SelectPresetVoiceCommand(name, rule, guiDropDown.selection);
    }
}
