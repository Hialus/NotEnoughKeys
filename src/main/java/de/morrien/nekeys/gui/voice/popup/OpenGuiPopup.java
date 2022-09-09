package de.morrien.nekeys.gui.voice.popup;

import com.mojang.blaze3d.vertex.PoseStack;
import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.DropDownList;
import de.morrien.nekeys.voice.command.OpenGuiVoiceCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Timor Morrien
 */
public class OpenGuiPopup extends AbstractPopup {
    protected DropDownList<OpenGuiVoiceCommand.AllowedGuis> guiDropDown;

    public OpenGuiPopup(String name, String rule) {
        super(name, rule);
        init();
    }

    public OpenGuiPopup(OpenGuiVoiceCommand voiceCommand) {
        super(voiceCommand);
        init();
        guiDropDown.selection = voiceCommand.getScreen();
    }

    protected void init() {
        guiDropDown = new DropDownList<>(0, 0, 0, 18, 6);
        guiDropDown.stringifier = gui -> I18n.get(gui.unlocalizedName);
        guiDropDown.optionsList.addAll(
                Arrays
                        .stream(OpenGuiVoiceCommand.AllowedGuis.values())
                        .filter(OpenGuiVoiceCommand.AllowedGuis::active)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void draw(PoseStack matrixStack, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        drawString(matrixStack, Minecraft.getInstance().font, new TranslatableComponent("gui.nekey.popup.selectPreset"), x + 6, y + 4, 0xFFFFFFFF);

        guiDropDown.x = x + 5;
        guiDropDown.y = y + 16;
        guiDropDown.width = width - 10;
        guiDropDown.draw(matrixStack);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return guiDropDown.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        return guiDropDown.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public IVoiceCommand getCommand() {
        return new OpenGuiVoiceCommand(name, rule, guiDropDown.selection);
    }
}
