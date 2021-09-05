package de.morrien.nekeys.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

/**
 * Created by Timor Morrien
 */
public class BetterButton extends Button {

    public double fontScale = 1;

    public BetterButton(int pX, int pY, int pWidth, int pHeight, ITextComponent pMessage, IPressable pOnPress) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress);
    }

    public BetterButton(int pX, int pY, int pWidth, int pHeight, ITextComponent pMessage, IPressable pOnPress, ITooltip pOnTooltip) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pOnTooltip);
    }


    @Override
    public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.font;
        minecraft.getTextureManager().bind(WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(
                pMatrixStack,
                this.x,
                this.y,
                0,
                46 + i * 20,
                this.width / 2,
                this.height / 2
        );
        this.blit(
                pMatrixStack,
                this.x,
                this.y + this.height / 2,
                0,
                46 + i * 20 + (20 - this.height / 2),
                this.width / 2,
                this.height / 2
        );
        this.blit(
                pMatrixStack,
                this.x + this.width / 2,
                this.y,
                200 - this.width / 2,
                46 + i * 20,
                this.width / 2,
                this.height / 2
        );
        this.blit(
                pMatrixStack,
                this.x + this.width / 2,
                this.y + this.height / 2,
                200 - this.width / 2,
                46 + i * 20 + (20 - this.height / 2),
                this.width / 2,
                this.height / 2);
        this.renderBg(pMatrixStack, minecraft, pMouseX, pMouseY);
        int j = getFGColor();
        drawCenteredString(pMatrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);

        if (this.isHovered()) {
            this.renderToolTip(pMatrixStack, pMouseX, pMouseY);
        }
        //if (this.visible) {
        //    y += 2;
        //    Minecraft mc = Minecraft.getInstance();
        //    FontRenderer fontrenderer = mc.font;
        //    mc.getTextureManager().bind(WIDGETS_LOCATION);
        //    GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        //    this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        //    int i = this.getHoverState(this.hovered);
        //    GlStateManager.enableBlend();
        //    GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        //    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//
        //    this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height / 2);
        //    this.drawTexturedModalRect(this.x, this.y + this.height / 2, 0, 46 + i * 20 + (20 - this.height / 2), this.width / 2, this.height / 2);
//
        //    this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height / 2);
        //    this.drawTexturedModalRect(this.x + this.width / 2, this.y + this.height / 2, 200 - this.width / 2, 46 + i * 20 + (20 - this.height / 2), this.width / 2, this.height / 2);
//
        //    // TODO: this.mouseDragged(mc, mouseX, mouseY);
        //    int textColor = 0xE0E0E0;
//
        //    if (packedFGColor != 0) {
        //        textColor = packedFGColor;
        //    } else if (!this.enabled) {
        //        textColor = 0xA0A0A0;
        //    } else if (this.hovered) {
        //        textColor = 0xFFFFA0;
        //    }
//
        //    GL11.glPushMatrix();
        //    {
        //        GL11.glTranslated((this.x + this.width / 2D) - fontrenderer.getStringWidth(displayString) * fontScale / 2D, this.y + (this.height - 8D) / fontScale / 2D, 0);
        //        GL11.glScaled(fontScale, fontScale, 0);
        //        fontrenderer.drawStringWithShadow(this.displayString, 0, 0, textColor);
        //    }
        //    GL11.glPopMatrix();
        //    y -= 2;
        //    //this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, textColor);
        //}
    }
}
