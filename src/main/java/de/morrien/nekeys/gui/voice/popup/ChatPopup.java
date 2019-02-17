package de.morrien.nekeys.gui.voice.popup;

import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.voice.command.ChatVoiceCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

/**
 * Created by Timor Morrien
 */
public class ChatPopup extends AbstractPopup {

    protected GuiTextField chatMessageTextField;

    public ChatPopup(String name, String rule) {
        super(name, rule);
        init();
    }

    public ChatPopup(ChatVoiceCommand voiceCommand) {
        super(voiceCommand);
        init();
        if (voiceCommand.getChatMessage() != null)
            chatMessageTextField.setText(voiceCommand.getChatMessage());
    }

    protected void init() {
        chatMessageTextField = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, 1000, 18);
        chatMessageTextField.setMaxStringLength(256);
    }

    @Override
    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        drawString(Minecraft.getMinecraft().fontRenderer, I18n.format("gui.nekey.popup.chat"), x + 6, y + 4, 0xFFFFFFFF);
        chatMessageTextField.x = x + 6;
        chatMessageTextField.y = y + 16;
        chatMessageTextField.width = width - 12;
        chatMessageTextField.drawTextBox();
    }

    @Override
    public boolean onClick(int mouseX, int mouseY) {
        if (chatMessageTextField.mouseClicked(mouseX, mouseY, 0)) return true;
        return super.onClick(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (!chatMessageTextField.textboxKeyTyped(typedChar, keyCode))
            super.keyTyped(typedChar, keyCode);
    }

    @Override
    public IVoiceCommand getCommand() {
        return new ChatVoiceCommand(name, rule, chatMessageTextField.getText());
    }
}
