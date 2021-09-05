package de.morrien.nekeys.gui.voice.popup;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.voice.command.ChatVoiceCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Created by Timor Morrien
 */
public class ChatPopup extends AbstractPopup {
    protected TextFieldWidget chatMessageTextField;

    public ChatPopup(String name, String rule) {
        super(name, rule);
        init();
    }

    public ChatPopup(ChatVoiceCommand voiceCommand) {
        super(voiceCommand);
        init();
        if (voiceCommand.getChatMessage() != null)
            chatMessageTextField.setValue(voiceCommand.getChatMessage());
    }

    protected void init() {
        chatMessageTextField = new TextFieldWidget(Minecraft.getInstance().font, 0, 0, 1000, 18, StringTextComponent.EMPTY);
        chatMessageTextField.setMaxLength(256);
    }

    @Override
    public void draw(MatrixStack matrixStack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        drawString(matrixStack, Minecraft.getInstance().font, new TranslationTextComponent("gui.nekey.popup.chat"), x + 6, y + 4, 0xFFFFFFFF);
        chatMessageTextField.x = x + 6;
        chatMessageTextField.y = y + 16;
        chatMessageTextField.setWidth(width - 12);
        chatMessageTextField.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int delta) {
        return chatMessageTextField.mouseClicked(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return chatMessageTextField.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return chatMessageTextField.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        return chatMessageTextField.charTyped(typedChar, keyCode);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return chatMessageTextField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
        return chatMessageTextField.keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_);
    }

    @Override
    public IVoiceCommand getCommand() {
        return new ChatVoiceCommand(name, rule, chatMessageTextField.getValue());
    }
}
