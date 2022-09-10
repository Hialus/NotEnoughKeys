package de.morrien.nekeys.gui.voice.popup;

import com.mojang.blaze3d.vertex.PoseStack;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.DropDownList;
import de.morrien.nekeys.voice.command.AbstractVoiceKeybind;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;

import java.util.Arrays;

/**
 * Created by Timor Morrien
 */
public abstract class AbstractKeybindPopup extends AbstractPopup {
    protected DropDownList<KeyMapping> keyBindingDropDown;

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
        keyBindingDropDown = new DropDownList<>(0, 0, 0, 18, 5, keyBinding -> I18n.get(keyBinding.getName()));
        keyBindingDropDown.showSearch = true;
        keyBindingDropDown.optionsList.addAll(Arrays.asList(Minecraft.getInstance().options.keyMappings));
    }

    @Override
    public void draw(PoseStack matrixStack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        keyBindingDropDown.x = x + 5;
        keyBindingDropDown.y = y + 2;
        keyBindingDropDown.setWidth(width - 10);
        keyBindingDropDown.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return keyBindingDropDown.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return keyBindingDropDown.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return keyBindingDropDown.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return keyBindingDropDown.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return keyBindingDropDown.charTyped(pCodePoint, pModifiers);
    }
}
