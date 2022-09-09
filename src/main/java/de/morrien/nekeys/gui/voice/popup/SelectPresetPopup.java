package de.morrien.nekeys.gui.voice.popup;

import com.mojang.blaze3d.vertex.PoseStack;
import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.DropDownList;
import de.morrien.nekeys.voice.command.SelectPresetVoiceCommand;

/**
 * Created by Timor Morrien
 */
public class SelectPresetPopup extends AbstractPopup {
    protected DropDownList<Integer> presetDropDown;

    public SelectPresetPopup(String name, String rule) {
        super(name, rule);
        init();
    }

    public SelectPresetPopup(SelectPresetVoiceCommand voiceCommand) {
        super(voiceCommand);
        init();
        presetDropDown.selection = voiceCommand.getPreset();
    }

    protected void init() {
        presetDropDown = new DropDownList<>(0, 0, 0, 18, 6);
        for (int i = 1; i <= 10; i++) {
            presetDropDown.optionsList.add(i);
        }
    }

    @Override
    public void draw(PoseStack matrixStack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        presetDropDown.x = x + 5;
        presetDropDown.y = y + 2;
        presetDropDown.width = width - 10;
        presetDropDown.draw(matrixStack);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int delta) {
        return presetDropDown.mouseClicked(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return presetDropDown.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return presetDropDown.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public IVoiceCommand getCommand() {
        return new SelectPresetVoiceCommand(name, rule, presetDropDown.selection);
    }
}
