package de.morrien.nekeys.gui.voice.popup;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.BetterButton;
import de.morrien.nekeys.voice.command.VoicePressKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.event.KeyEvent;

/**
 * Created by Timor Morrien
 */
public class PressKeyPopup extends AbstractPopup {

    protected int keycode = 0;
    protected Button selectKeyButton;
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
        selectKeyButton = new BetterButton(0, 0, 0, 0, StringTextComponent.EMPTY, button -> {
            listen = true;
        });
    }

    @Override
    public void draw(MatrixStack matrixStack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        drawString(matrixStack, Minecraft.getInstance().font, "Select key", x + 10, y + 7, 0xFFFFFFFF);
        Minecraft.getInstance().font.drawWordWrap(new TranslationTextComponent("gui.nekey.popup.wip"), x + 10, y + 30, width - 20, 0xFFFFFFFF);

        selectKeyButton.x = x + width / 2 - 5;
        selectKeyButton.y = y + 2;
        selectKeyButton.setWidth(width / 2);
        selectKeyButton.setHeight(20);
        selectKeyButton.setMessage(new StringTextComponent(keycode == 0 ? "None" : KeyEvent.getKeyText(keycode)));

        selectKeyButton.render(matrixStack, mouseX, mouseY, partialTicks);
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
