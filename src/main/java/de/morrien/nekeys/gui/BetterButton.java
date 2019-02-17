package de.morrien.nekeys.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * Created by Timor Morrien
 */
public class BetterButton extends GuiButton {

    public double fontScale = 1;

    public BetterButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }

    public BetterButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x, this.y + this.height / 2, 0, 46 + i * 20 + (20 - this.height / 2), this.width / 2, this.height / 2);

            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y + this.height / 2, 200 - this.width / 2, 46 + i * 20 + (20 - this.height / 2), this.width / 2, this.height / 2);

            this.mouseDragged(mc, mouseX, mouseY);
            int textColor = 0xE0E0E0;

            if (packedFGColour != 0) {
                textColor = packedFGColour;
            } else if (!this.enabled) {
                textColor = 0xA0A0A0;
            } else if (this.hovered) {
                textColor = 0xFFFFA0;
            }

            GL11.glPushMatrix();
            {
                GL11.glTranslated((this.x + this.width / 2D) - fontrenderer.getStringWidth(displayString) * fontScale / 2D, this.y + (this.height - 8D) / fontScale / 2D, 0);
                GL11.glScaled(fontScale, fontScale, 0);
                fontrenderer.drawStringWithShadow(this.displayString, 0, 0, textColor);
            }
            GL11.glPopMatrix();

            //this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, textColor);
        }
    }
}
