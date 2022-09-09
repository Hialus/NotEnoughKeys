package de.morrien.nekeys.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/**
 * Created by Timor Morrien
 */
public class ScaleableButton extends Button {

    public double fontScale = 1;

    public ScaleableButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Button.OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }

    public ScaleableButton(int pX, int pY, int pWidth, int pHeight, Component pMessage, Button.OnPress pOnPress, Button.OnTooltip pOnTooltip) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pOnTooltip);
    }


    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontrenderer = minecraft.font;
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(
                pPoseStack,
                this.x,
                this.y,
                0,
                46 + i * 20,
                this.width / 2,
                this.height / 2
        );
        this.blit(
                pPoseStack,
                this.x,
                this.y + this.height / 2,
                0,
                46 + i * 20 + (20 - this.height / 2),
                this.width / 2,
                this.height / 2
        );
        this.blit(
                pPoseStack,
                this.x + this.width / 2,
                this.y,
                200 - this.width / 2,
                46 + i * 20,
                this.width / 2,
                this.height / 2
        );
        this.blit(
                pPoseStack,
                this.x + this.width / 2,
                this.y + this.height / 2,
                200 - this.width / 2,
                46 + i * 20 + (20 - this.height / 2),
                this.width / 2,
                this.height / 2);
        this.renderBg(pPoseStack, minecraft, pMouseX, pMouseY);
        int j = getFGColor();
        drawCenteredString(pPoseStack, fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | Mth.ceil(this.alpha * 255.0F) << 24);

        if (this.isHovered) {
            this.renderToolTip(pPoseStack, pMouseX, pMouseY);
        }
    }
}
