package de.morrien.nekeys.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.IGuiEventListener;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timor Morrien
 */
public class DropDownList<E> extends Gui implements IGuiEventListener {

    public final List<E> optionsList;
    public Stringfier<E> stringifier;
    public CellRenderer<E> cellRenderer;
    public E selection;
    public int x;
    public int y;
    public int width;
    public int height;
    public int expandHeight;
    public int cellHeight = 13;
    public boolean expanded;
    public int scrollPosition = 0;
    public boolean disabled;

    public DropDownList(int x, int y, int width, int height, int expandHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.expandHeight = expandHeight;
        this.expanded = false;
        this.optionsList = new ArrayList<>();
        this.stringifier = Object::toString;
        this.disabled = false;
    }

    public void draw() {
        drawRect(x, y, x + width, y + height, disabled ? 0xFFAAAAAA : 0xFFFFFFFF);
        drawRect(x + 1, y + 1, x + width - 1, y + height - 1, 0xFF000000);
        if (cellRenderer != null && selection != null) {
            cellRenderer.render(selection, x + 1, y + 2, width - 3, this);
        } else {
            drawString(Minecraft.getInstance().fontRenderer, selection == null ? "None" : stringifier.toString(selection), x + 5, y + 5, 0xFFFFFF);
        }
        int buttonHeight = height - 2;
        drawRect(x + width - buttonHeight - 1, y + 1, x + width - 1, y + 1 + buttonHeight, 0xFF565656);
        drawRect(x + width - buttonHeight, y + 2, x + width - 2, y + buttonHeight, 0xFF787878);
        GL11.glPushMatrix();
        GL11.glTranslated(x + width - buttonHeight + 1, y - 3, 0);
        GL11.glScaled(3.6, 3, 1);
        if (expanded) {
            drawString(Minecraft.getInstance().fontRenderer, "\u25B2", 0, 0, disabled ? 0xA0A0A0 : 0xFFFFFF);
        } else {
            drawString(Minecraft.getInstance().fontRenderer, "\u25BC", 0, 0, disabled ? 0xA0A0A0 : 0xFFFFFF);
        }
        GL11.glPopMatrix();

        if (this.expanded) {
            drawRect(x, y + height, x + width, y + height + expandHeight * cellHeight, 0xFFFFFFFF);
            drawRect(x + 1, y + height, x + width - 1, y + height + expandHeight * cellHeight - 1, 0xFF000000);
            int offset = 0;
            for (int i = scrollPosition; i < Math.min(optionsList.size(), scrollPosition + expandHeight); i++) {
                if (cellRenderer != null) {
                    cellRenderer.render(optionsList.get(i), x + 1, y + height + offset * cellHeight, width - 3, this);
                } else {
                    drawString(Minecraft.getInstance().fontRenderer, stringifier.toString(optionsList.get(i)), x + 5, y + height + offset * cellHeight + 2, 0xFFFFFF);
                }
                drawHorizontalLine(x, x + width - 1, y + height + offset * cellHeight + cellHeight - 1, 0xFFFFFFFF);
                offset++;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int buttonHeight = height - 2;
        if (mouseX >= x + width - buttonHeight - 1 &&
                mouseX <= x + width - 1 &&
                mouseY >= y + 1 &&
                mouseY <= y + 1 + buttonHeight) {
            expanded = !expanded && !disabled;
            return true;
        }

        if (expanded) {
            int offset = 0;
            for (int i = scrollPosition; i < Math.min(optionsList.size(), scrollPosition + expandHeight); i++) {
                if (mouseX >= x &&
                        mouseX <= x + width &&
                        mouseY >= y + height + (offset - 1) * cellHeight + cellHeight &&
                        mouseY <= y + height + offset * cellHeight + cellHeight - 1) {
                    selection = optionsList.get(i);
                    expanded = false;
                    return true;
                }
                offset++;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double delta) {
        if (!expanded) return false;
        if (delta != 0) {
            if (delta > 0)
                delta = -1;
            else
                delta = 1;

            if (expandHeight < optionsList.size())
                scrollPosition += delta;
        }
        if (scrollPosition >= optionsList.size() - expandHeight) scrollPosition = optionsList.size() - expandHeight;
        if (scrollPosition < 0) scrollPosition = 0;

        return true;
    }

    public interface Stringfier<E> {
        String toString(E e);
    }

    public interface CellRenderer<E> {
        void render(E e, int x, int y, int width, DropDownList<E> dropDownList);
    }
}
