package de.morrien.nekeys.gui.voice.popup.psi;

import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.voice.command.psi.SelectPsiSlotVoiceCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Timor Morrien
 */
public class SelectPsiSlotPopup extends AbstractPopup {

    protected GuiTextField numberTextField;

    public SelectPsiSlotPopup(String name, String rule) {
        super(name, rule);
        init();
    }

    public SelectPsiSlotPopup(SelectPsiSlotVoiceCommand voiceCommand) {
        super(voiceCommand);
        init();
        numberTextField.setText(String.valueOf(voiceCommand.getSlot()));
    }

    protected void init() {
        numberTextField = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, 1000, 18);
        numberTextField.setMaxStringLength(1000);
        numberTextField.setValidator(input -> (input != null) && input.matches("\\d*"));
        numberTextField.setText("0");
    }

    @Override
    public void draw(int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        drawString(Minecraft.getMinecraft().fontRenderer, I18n.format("gui.nekey.popup.psi.selectSlot"), x + 6, y + 4, 0xFFFFFFFF);

        ResourceLocation rs = new ResourceLocation("nekeys", "textures/gui/psi_circle.png");
        Minecraft.getMinecraft().getTextureManager().bindTexture(rs);
        drawModalRectWithCustomSizedTexture(x, y + 30, 0, 0, 70, 70, 70, 70);

        numberTextField.x = x + 6;
        numberTextField.y = y + 16;
        numberTextField.width = width - 12;
        numberTextField.drawTextBox();
    }

    @Override
    public boolean onClick(int mouseX, int mouseY) {
        if (numberTextField.mouseClicked(mouseX, mouseY, 0)) return true;
        return super.onClick(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (!numberTextField.textboxKeyTyped(typedChar, keyCode))
            super.keyTyped(typedChar, keyCode);
    }

    @Override
    public IVoiceCommand getCommand() {
        return new SelectPsiSlotVoiceCommand(name, rule, Integer.parseInt(numberTextField.getText()));
    }
}
