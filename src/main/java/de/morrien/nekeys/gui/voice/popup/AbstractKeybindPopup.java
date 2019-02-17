package de.morrien.nekeys.gui.voice.popup;

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
        keyBindingDropDown.stringifier = keyBinding -> I18n.format(keyBinding.getKeyDescription());
        keyBindingDropDown.optionsList.addAll(Arrays.asList(Minecraft.getMinecraft().gameSettings.keyBindings));
    }

    @Override
    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        keyBindingDropDown.x = x + 5;
        keyBindingDropDown.y = y + 2;
        keyBindingDropDown.width = width - 10;
        keyBindingDropDown.draw();
    }

    @Override
    public boolean onClick(int mouseX, int mouseY) {
        super.onClick(mouseX, mouseY);
        //if (!keyBindingDropDown.selection.equals("Select a KeyBinding") && (keyBinding == null || !keyBindingDropDown.selection.equals(keyBinding.getDisplayName()))) {
        //    for (KeyBinding binding : Minecraft.getMinecraft().gameSettings.keyBindings) {
        //        if (keyBindingDropDown.selection.equals(I18n.format(binding.getKeyDescription()))) {
        //            keyBinding = binding;
        //            break;
        //        }
        //    }
        //}
        return keyBindingDropDown.onClick(mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        keyBindingDropDown.handleMouseInput();
    }
}
