//package de.morrien.nekeys.gui.voice.popup.psi;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.morrien.nekeys.api.command.IVoiceCommand;
//import de.morrien.nekeys.api.popup.AbstractPopup;
//import de.morrien.nekeys.voice.command.psi.SelectPsiSlotVoiceCommand;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.widget.EditBox;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.network.chat.TextComponent;
//import net.minecraft.network.chat.TranslatableComponent;
//
///**
// * Created by Timor Morrien
// */
//public class SelectPsiSlotPopup extends AbstractPopup {
//    protected EditBox numberTextField;
//
//    public SelectPsiSlotPopup(String name, String rule) {
//        super(name, rule);
//        init();
//    }
//
//    public SelectPsiSlotPopup(SelectPsiSlotVoiceCommand voiceCommand) {
//        super(voiceCommand);
//        init();
//        numberTextField.setValue(String.valueOf(voiceCommand.getSlot()));
//    }
//
//    protected void init() {
//        numberTextField = new EditBox(Minecraft.getInstance().font, 0, 0, 1000, 18, TextComponent.EMPTY);
//        numberTextField.setMaxLength(2);
//        //numberTextField.setFilter(input -> (input != null) && input.matches("\\d*"));
//        numberTextField.setValue("0");
//    }
//
//    @Override
//    public void draw(PoseStack matrixStack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
//        drawString(matrixStack, Minecraft.getInstance().font, new TranslatableComponent("gui.nekey.popup.psi.selectSlot"), x + 6, y + 4, 0xFFFFFFFF);
//
//        ResourceLocation rs = new ResourceLocation("nekeys", "textures/gui/psi_circle.png");
//        Minecraft.getInstance().getTextureManager().bind(rs);
//        blit(matrixStack, x, y + 30, 0, 0, 70, 70, 70, 70);
//
//        numberTextField.x = x + 6;
//        numberTextField.y = y + 16;
//        numberTextField.setWidth(width - 12);
//        numberTextField.render(matrixStack, mouseX, mouseY, partialTicks);
//    }
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (numberTextField.mouseClicked(mouseX, mouseY, button)) return true;
//        return super.mouseClicked(mouseX, mouseY, button);
//    }
//
//    @Override
//    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
//        return numberTextField.keyPressed(pKeyCode, pScanCode, pModifiers) ||
//            super.keyPressed(pKeyCode, pScanCode, pModifiers);
//    }
//
//    @Override
//    public boolean charTyped(char pCodePoint, int pModifiers) {
//        return numberTextField.charTyped(pCodePoint, pModifiers) ||
//                super.charTyped(pCodePoint, pModifiers);
//    }
//
//    @Override
//    public IVoiceCommand getCommand() {
//        return new SelectPsiSlotVoiceCommand(name, rule, Integer.parseInt(numberTextField.getValue()));
//    }
//}
