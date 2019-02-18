package de.morrien.nekeys.gui.voice.popup;

import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.BetterButton;
import de.morrien.nekeys.voice.command.VoicePressKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import java.awt.event.KeyEvent;

/**
 * Created by Timor Morrien
 */
public class PressKeyPopup extends AbstractPopup {

    protected int keycode = 0;
    protected GuiButton selectKeyButton;
    protected boolean listen;

    public PressKeyPopup(String name, String rule) {
        super(name, rule);
        init();
    }

    public PressKeyPopup(VoicePressKey voiceCommand) {
        super(voiceCommand);
        keycode = voiceCommand.getKeyCode();
        init();
    }

    protected void init() {
        selectKeyButton = new BetterButton(0, 0, 0, "");
    }

    @Override
    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        drawString(Minecraft.getInstance().fontRenderer, "Select key", x + 10, y + 7, 0xFFFFFFFF);
        Minecraft.getInstance().fontRenderer.drawSplitString(I18n.format("gui.nekey.popup.wip"), x + 10, y + 30, width - 20, 0xFFFFFFFF);

        selectKeyButton.x = x + width / 2 - 5;
        selectKeyButton.y = y + 2;
        selectKeyButton.width = width / 2;
        selectKeyButton.height = 20;
        selectKeyButton.displayString = keycode == 0 ? "None" : KeyEvent.getKeyText(keycode);

        selectKeyButton.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int delta) {
        if (selectKeyButton.mouseClicked(mouseX, mouseY, 0)) {
            listen = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return selectKeyButton.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (listen) {
            keycode = KeyEvent.getExtendedKeyCodeForChar(typedChar);
            listen = false;
            return true;
        }
        return false;
    }

    @Override
    public VoicePressKey getCommand() {
        return new VoicePressKey(name, rule, keycode);
    }
}
