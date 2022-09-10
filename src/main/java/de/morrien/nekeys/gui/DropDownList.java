package de.morrien.nekeys.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timor Morrien
 */
public class DropDownList<E> extends AbstractWidget {
    protected static final ResourceLocation SOCIAL_INTERACTIONS_LOCATION = new ResourceLocation("textures/gui/social_interactions.png");

    public final List<E> optionsList;
    public Stringfier<E> stringifier;
    public CellRenderer<E> cellRenderer;
    public ChangeListener<E> changeListener;
    public int maxShownItems;
    public boolean showSearch;
    public boolean disabled;
    public int cellHeight;

    // State
    public E selection;
    public boolean expanded;
    public int scrollPosition;
    protected EditBox searchBox;

    public DropDownList(int x, int y, int width, int height, int maxShownItems) {
        super(x, y, width, height, TextComponent.EMPTY);
        this.maxShownItems = maxShownItems;
        this.expanded = false;
        this.optionsList = new ArrayList<>();
        this.stringifier = Object::toString;
        this.disabled = false;
        this.showSearch = false;
        this.cellHeight = 13;
        this.scrollPosition = 0;
        this.searchBox = new EditBox(Minecraft.getInstance().font, x, y, width, height, TextComponent.EMPTY);
    }

    public DropDownList(int x, int y, int width, int height, int maxShownItems, Stringfier<E> stringifier) {
        this(x, y, width, height, maxShownItems);
        this.stringifier = stringifier;
    }

    public DropDownList(int x, int y, int width, int height, int maxShownItems, Stringfier<E> stringifier, CellRenderer<E> cellRenderer) {
        this(x, y, width, height, maxShownItems, stringifier);
        this.cellRenderer = cellRenderer;
    }

    public DropDownList(int pX, int pY, int pWidth, int pHeight, int maxShownItems, Stringfier<E> stringifier, ChangeListener<E> changeListener) {
        this(pX, pY, pWidth, pHeight, maxShownItems, stringifier);
        this.changeListener = changeListener;
    }

    public DropDownList(int x, int y, int width, int height, int maxShownItems, Stringfier<E> stringifier, CellRenderer<E> cellRenderer, ChangeListener<E> changeListener) {
        this(x, y, width, height, maxShownItems, stringifier, cellRenderer);
        this.changeListener = changeListener;
    }

    @Override
    public void render(PoseStack matrixStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.isHovered = pMouseX >= this.x && pMouseY >= this.y && pMouseX < this.x + this.width && pMouseY < this.y + this.height;
        fill(matrixStack, x, y, x + width, y + height, expanded ? 0xFFFFFFFF : 0xFFAAAAAA);
        fill(matrixStack, x + 1, y + 1, x + width - 1, y + height - 1, 0xFF000000);
        if (cellRenderer != null && selection != null) {
            cellRenderer.render(selection, x + 1, y + 2, width - 3, this);
        } else {
            final String text = selection == null ? "None" : stringifier.toString(selection);
            drawString(matrixStack, Minecraft.getInstance().font, text, x + 5, y + 5, 0xFFFFFF);
        }
        int buttonSize = height - 2;
        matrixStack.pushPose();
        ScalableButton.renderScalableButtonBackground(this, matrixStack, x + width - buttonSize - 1, y + 1, buttonSize, buttonSize, 1.0f, isButtonHovered(pMouseX, pMouseY) ? 2 : 1);
        matrixStack.translate(x + width - buttonSize + 2, y + 4, 0);
        matrixStack.scale(2, 1.6f, 1);
        if (expanded) {
            Minecraft.getInstance().font.draw(matrixStack, "\u25B2", 0, 0, disabled ? 0xA0A0A0 : 0xFFFFFF);
        } else {
            Minecraft.getInstance().font.draw(matrixStack, "\u25BC", 0, 0, disabled ? 0xA0A0A0 : 0xFFFFFF);
        }
        matrixStack.popPose();

        if (this.expanded) {
            int offset = showSearch ? 1 : 0;
            fill(matrixStack, x, y + height, x + width, y + height + maxShownItems * cellHeight + offset * cellHeight, 0xFFFFFFFF);
            fill(matrixStack, x + 1, y + height, x + width - 1, y + height + maxShownItems * cellHeight - 1 + offset * cellHeight, 0xFF000000);
            for (int i = scrollPosition; i < Math.min(getFilteredOptions().size(), scrollPosition + maxShownItems); i++) {
                if (cellRenderer != null) {
                    cellRenderer.render(getFilteredOptions().get(i), x + 1, y + height + offset * cellHeight, width - 3, this);
                } else {
                    drawString(matrixStack, Minecraft.getInstance().font, stringifier.toString(getFilteredOptions().get(i)), x + 5, y + height + offset * cellHeight + 2, 0xFFFFFF);
                }
                hLine(matrixStack, x, x + width - 1, y + height + offset * cellHeight + cellHeight - 1, 0xFFFFFFFF);
                offset++;
            }

            if (showSearch) {
                searchBox.x = x + 17;
                searchBox.y = y + height + 1;
                searchBox.setWidth(width - 19);
                searchBox.setHeight(cellHeight - 2);
                searchBox.setBordered(false);
                searchBox.render(matrixStack, pMouseX, pMouseY, pPartialTick);

                RenderSystem.setShaderTexture(0, SOCIAL_INTERACTIONS_LOCATION);
                this.blit(matrixStack, x + 2, y + height, 243, 1, 12, 12);

                this.hLine(matrixStack, x + 1, x + width - 2, y + height + cellHeight - 1, 0xffa0a0a0);
                this.vLine(matrixStack, x + 15, y + height - 1, y + height + cellHeight - 1, 0xffa0a0a0);
            }

            renderScrollBar();
        }
    }

    private void renderScrollBar() {
        int offset = showSearch ? cellHeight : 0;
        int y0 = y + height + offset;
        int y1 = y + height + maxShownItems * cellHeight - 1 + offset;

        int scrollBarX0 = this.x + this.width - 4;
        int scrollBarX1 = scrollBarX0 + 3;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);

        int maxScroll = getFilteredOptions().size() - maxShownItems;
        if (maxScroll > 0) {
            RenderSystem.disableTexture();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            int l1 = (int) ((float) ((y1 - y0) * (y1 - y0)) / ((float) (getFilteredOptions().size() * this.cellHeight)));
            l1 = Mth.clamp(l1, 32, y1 - y0 - 8);
            int i2 = scrollPosition * (y1 - y0 - l1) / maxScroll + y0;
            if (i2 < y0) {
                i2 = y0;
            }

            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            bufferbuilder.vertex(scrollBarX0, y1, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(scrollBarX1, y1, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(scrollBarX1, y0, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(scrollBarX0, y0, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.vertex(scrollBarX0, (i2 + l1), 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(scrollBarX1, (i2 + l1), 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(scrollBarX1, i2, 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(scrollBarX0, i2, 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.vertex(scrollBarX0, (i2 + l1 - 1), 0.0D).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex((scrollBarX1 - 1), (i2 + l1 - 1), 0.0D).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex((scrollBarX1 - 1), i2, 0.0D).color(192, 192, 192, 255).endVertex();
            bufferbuilder.vertex(scrollBarX0, i2, 0.0D).color(192, 192, 192, 255).endVertex();
            tesselator.end();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            expanded = !expanded && !disabled;
            return true;
        }

        if (expanded) {
            int offset = showSearch ? 1 : 0;
            for (int i = scrollPosition; i < Math.min(getFilteredOptions().size(), scrollPosition + maxShownItems); i++) {
                if (mouseX >= x &&
                        mouseX <= x + width &&
                        mouseY >= y + height + (offset - 1) * cellHeight + cellHeight &&
                        mouseY <= y + height + offset * cellHeight + cellHeight - 1) {
                    selection = getFilteredOptions().get(i);
                    expanded = false;
                    if (changeListener != null) {
                        changeListener.onChange(selection, this);
                    }
                    return true;
                }
                offset++;
            }

            if (showSearch && searchBox.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }

            expanded = false;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!expanded) return false;
        if (delta != 0) {
            if (delta > 0) {
                delta = -1;
            } else {
                delta = 1;
            }

            if (maxShownItems < getFilteredOptions().size()) {
                scrollPosition += delta;
            }
        }
        if (scrollPosition >= getFilteredOptions().size() - maxShownItems) {
            scrollPosition = getFilteredOptions().size() - maxShownItems;
        }
        if (scrollPosition < 0) {
            scrollPosition = 0;
        }

        return true;
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return showSearch && expanded && searchBox.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        return showSearch && expanded && searchBox.keyReleased(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return showSearch && expanded && searchBox.charTyped(pCodePoint, pModifiers);
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    protected List<E> getFilteredOptions() {
        if (showSearch && !searchBox.getValue().isEmpty()) {
            return optionsList.stream().filter(e -> stringifier.toString(e).toLowerCase().contains(searchBox.getValue().toLowerCase())).toList();
        }
        return optionsList;
    }

    private boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x &&
                mouseX <= x + width &&
                mouseY >= y &&
                mouseY <= y + height;
    }

    private boolean isButtonHovered(double mouseX, double mouseY) {
        return mouseX >= x + width - height + 1 &&
                mouseX <= x + width - 1 &&
                mouseY >= y + 1 &&
                mouseY <= y + 1 + (height - 2);
    }

    public interface Stringfier<E> {
        String toString(E e);
    }

    public interface CellRenderer<E> {
        void render(E e, int x, int y, int width, DropDownList<E> dropDownList);
    }

    public interface ChangeListener<E> {
        void onChange(E selection, DropDownList<E> dropDown);
    }
}
