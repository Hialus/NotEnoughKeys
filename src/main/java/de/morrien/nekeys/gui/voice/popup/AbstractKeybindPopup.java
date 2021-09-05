package de.morrien.nekeys.gui.voice.popup;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.DropDownList;
import de.morrien.nekeys.voice.command.AbstractVoiceKeybind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;

import java.util.Arrays;

/**
 * Created by Timor Morrien
 */
public abstract class AbstractKeybindPopup extends AbstractPopup {
    protected DropDownList<KeyBinding> keyBindingDropDown;

    public AbstractKeybindPopup(String name, String rule) {
        super(name, rule);
        init();
    }

    public AbstractKeybindPopup(AbstractVoiceKeybind voiceCommand) {
        super(voiceCommand);
        init();
        keyBindingDropDown.selection = voiceCommand.getKeybind();
    }

    protected void init() {
        keyBindingDropDown = new DropDownList<>(0, 0, 0, 18, 6);
        keyBindingDropDown.stringifier = keyBinding -> I18n.get(keyBinding.getName());
        keyBindingDropDown.optionsList.addAll(Arrays.asList(Minecraft.getInstance().options.keyMappings));
    }

    @Override
    public void draw(MatrixStack matrixStack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        keyBindingDropDown.x = x + 5;
        keyBindingDropDown.y = y + 2;
        keyBindingDropDown.width = width - 10;
        keyBindingDropDown.draw(matrixStack);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return keyBindingDropDown.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return keyBindingDropDown.mouseScrolled(pMouseX, pMouseY, pDelta);
    }
}
