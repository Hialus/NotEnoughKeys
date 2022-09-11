package de.morrien.nekeys.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/**
 * Created by Timor Morrien
 */
public class ScalableButton extends Button {

    public float fontScale = 1;

    public ScalableButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Button.OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }

    public ScalableButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Button.OnPress pOnPress, Button.OnTooltip pOnTooltip) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pOnTooltip);
    }

    public static void renderScalableButtonBackground(GuiComponent gui, PoseStack poseStack, int x, int y, int width, int height, float alpha, int version) {
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        gui.blit(
                poseStack,
                x,
                y,
                0,
                46 + version * 20,
                width / 2,
                height / 2
        );
        gui.blit(
                poseStack,
                x,
                y + height / 2,
                0,
                46 + version * 20 + (20 - height / 2),
                width / 2,
                height / 2
        );
        gui.blit(
                poseStack,
                x + width / 2,
                y,
                200 - width / 2,
                46 + version * 20,
                width / 2,
                height / 2
        );
        gui.blit(
                poseStack,
                x + width / 2,
                y + height / 2,
                200 - width / 2,
                46 + version * 20 + (20 - height / 2),
                width / 2,
                height / 2);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        renderScalableButtonBackground(this, poseStack, this.x, this.y, this.width, this.height, this.alpha, getYImage(this.isHovered));
        this.renderBg(poseStack, minecraft, mouseX, mouseY);
        int j = getFGColor();
        poseStack.pushPose();
        poseStack.translate(this.x + this.width / 2, this.y + (this.height - 8 * fontScale) / 2, 0);
        poseStack.scale(fontScale, fontScale, fontScale);
        drawCenteredString(poseStack, font, this.getMessage(), 0, 0, j | Mth.ceil(this.alpha * 255.0F) << 24);
        poseStack.popPose();
        if (this.isHovered) {
            this.renderToolTip(poseStack, mouseX, mouseY);
        }
    }
}
