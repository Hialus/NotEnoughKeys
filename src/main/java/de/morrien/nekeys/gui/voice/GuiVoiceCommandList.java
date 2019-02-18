package de.morrien.nekeys.gui.voice;

import de.morrien.nekeys.NotEnoughKeys;
import de.morrien.nekeys.Reference;
import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.gui.BetterButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Timor Morrien
 */
@OnlyIn(Dist.CLIENT)
public class GuiVoiceCommandList extends GuiListExtended<GuiVoiceCommandList.CommandEntry> {

    private final GuiVoiceCommand gui;
    private final Minecraft mc;
    public IAction activeAction;
    //List<CommandEntry> listEntries;
    private GuiTextField activeTextField;

    public GuiVoiceCommandList(GuiVoiceCommand gui, Minecraft mcIn) {
        super(mcIn, gui.width, gui.height, 35, gui.height - 62, 25);
        this.gui = gui;
        this.mc = mcIn;

        loadCommands();
    }

    void loadCommands() {
        for (IVoiceCommand voiceCommand : NotEnoughKeys.instance.voiceHandler.getVoiceCommands()) {
            getChildren().add(new CommandEntry(voiceCommand));
        }
    }

    /**
     * Save the changes that have been made
     */
    void save() {
        // Use a Thread so the GUI won't freeze while saving
        new Thread(() -> {
            NotEnoughKeys.instance.voiceHandler.getVoiceCommands().clear();
            for (CommandEntry entry : getChildren()) {
                NotEnoughKeys.instance.voiceHandler.addVoiceCommand(entry.getCommand());
            }
            NotEnoughKeys.instance.voiceHandler.updateGrammar();
            NotEnoughKeys.instance.voiceHandler.saveConfig();
        }).start();
    }

    /**
     * Called when the mouse is clicked onto an entry.
     *
     * @param index  Index of the entry
     * @param button The mouse button that was pressed.
     * @param mouseX The mouse X coordinate.
     * @param mouseY The mouse Y coordinate.
     * @return true if the entry did something with the click and it should be selected.
     */
    @Override
    protected boolean mouseClicked(int index, int button, double mouseX, double mouseY) {
        if (activeTextField == null || !activeTextField.mouseClicked(mouseX, mouseY, button)) {
            return super.mouseClicked(index, button, mouseX, mouseY);
        }
        return true;
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (activeTextField != null) {
            activeTextField.charTyped(typedChar, keyCode);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (activeTextField != null) {
            activeTextField.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
        if (activeTextField != null) {
            activeTextField.keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_);
            return true;
        }
        return false;
    }

    // TODO
    //public void update() {
    //    if (activeTextField != null)
    //        activeTextField.updateCursorCounter();
    //}

    CommandEntry newEntry(IVoiceCommand command) {
        return new CommandEntry(command);
    }

    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 45;
    }

    @Override
    public int getListWidth() {
        return super.getListWidth() + 132;
    }

    @Override
    public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
        super.drawScreen(mouseXIn, mouseYIn, partialTicks);
    }

    /**
     * Custom drawBackground method to customize background texture
     */
    @Override
    protected void drawBackground() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/block/jungle_planks.png"));
        GlStateManager.color4f(1F, 1F, 1F, 1F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0, (double) this.height, 0).tex(0, (double) ((float) this.height / 32F)).color(150, 150, 150, 255).endVertex();
        bufferbuilder.pos((double) this.width, (double) this.height, 0).tex((double) ((float) this.width / 32F), (double) ((float) this.height / 32F)).color(150, 150, 150, 255).endVertex();
        bufferbuilder.pos((double) this.width, 0, 0).tex((double) ((float) this.width / 32F), 0).color(150, 150, 150, 255).endVertex();
        bufferbuilder.pos(0, 0, 0).tex(0, 0).color(150, 150, 150, 255).endVertex();
        tessellator.draw();
    }

    /**
     * Custom overlayBackground method to customize background texture
     */
    @Override
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/block/dark_oak_planks.png"));
        GlStateManager.color4f(1F, 1F, 1F, 1F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos((double) this.left, (double) endY, 0).tex(0, (double) ((float) endY / 32F)).color(255, 255, 255, endAlpha).endVertex();
        bufferbuilder.pos((double) (this.left + this.width), (double) endY, 0).tex((double) ((float) this.width / 32F), (double) ((float) endY / 32F)).color(255, 255, 255, endAlpha).endVertex();
        bufferbuilder.pos((double) (this.left + this.width), (double) startY, 0).tex((double) ((float) this.width / 32F), (double) ((float) startY / 32F)).color(255, 255, 255, startAlpha).endVertex();
        bufferbuilder.pos((double) this.left, (double) startY, 0).tex(0, (double) ((float) startY / 32F)).color(255, 255, 255, startAlpha).endVertex();
        tessellator.draw();
    }

    @Override
    protected void drawContainerBackground(Tessellator tessellator) {
    }

    /**
     * A list @see{GuiListExtended.IGuiListEntry} to display a VoiceCommand
     */
    @OnlyIn(Dist.CLIENT)
    public class CommandEntry extends GuiListExtended.IGuiListEntry<CommandEntry> {
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnDelete;
        private final GuiButton btnEdit;
        private final BetterButton upButton;
        private final GuiTextField nameTextField;
        private final GuiTextField ruleTextField;
        private final ResourceLocation settingsIcon = new ResourceLocation(Reference.MODID, "textures/gui/settings.png");
        BetterButton downButton;
        private IVoiceCommand command;

        private CommandEntry(IVoiceCommand command) {
            this.command = command;
            this.btnChangeKeyBinding = new BetterButton(0, 0, 0, 95, 20, "");
            this.btnDelete = new BetterButton(0, 0, 0, 50, 20, I18n.format("selectServer.delete"));
            this.btnEdit = new BetterButton(0, 0, 0, 20, 20, "E");
            this.upButton = new BetterButton(0, 0, 0, 10, 10, "\u25B2");
            this.downButton = new BetterButton(0, 0, 0, 10, 10, "\u25BC");

            this.nameTextField = new GuiTextField(0, Minecraft.getInstance().fontRenderer, 0, 0, 40, 18);
            this.nameTextField.setText(command.getName());
            this.ruleTextField = new GuiTextField(0, Minecraft.getInstance().fontRenderer, 0, 0, 200, 18);
            this.ruleTextField.setMaxStringLength(Integer.MAX_VALUE);
            this.ruleTextField.setText(command.getRuleContent());
            checkValid(ruleTextField.getText());
            checkName();
        }

        @Override
        public void drawEntry(int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
            command.setName(nameTextField.getText());
            if (!ruleTextField.getText().equals(command.getRuleContent())) {
                String text = ruleTextField.getText();
                checkValid(text);
                command.setRuleContent(ruleTextField.getText());
            }
            checkName();

            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepthTest();
            int x = getX();
            int y = getY();

            int posY = y + slotHeight / 2 - GuiVoiceCommandList.this.mc.fontRenderer.FONT_HEIGHT / 2;
            this.nameTextField.x = x;
            this.nameTextField.y = posY - 5;
            this.nameTextField.drawTextField(mouseX, mouseY, partialTicks);
            gui.drawVerticalLine(x + 45, posY - 7, posY + 14, 0xFFFFFFFF);
            this.ruleTextField.x = x + 51;
            this.ruleTextField.y = posY - 5;
            this.ruleTextField.drawTextField(mouseX, mouseY, partialTicks);
            this.btnEdit.x = x + 256;
            this.btnEdit.y = y;
            this.btnEdit.render(mouseX, mouseY, partialTicks);
            Minecraft.getInstance().getTextureManager().bindTexture(settingsIcon);
            GlStateManager.color3f(1, 1, 1);
            Gui.drawModalRectWithCustomSizedTexture(x + 258, posY - 4, 0, 0, 16, 15, 16, 16);

            this.upButton.x = x + 278;
            this.upButton.y = y;
            this.upButton.fontScale = 0.9;
            this.upButton.render(mouseX, mouseY, partialTicks);
            this.downButton.x = x + 278;
            this.downButton.y = y + 10;
            this.downButton.fontScale = 0.9;
            this.downButton.render(mouseX, mouseY, partialTicks);

            this.btnDelete.x = x + 280 + 10;
            this.btnDelete.y = y;
            this.btnDelete.render(mouseX, mouseY, partialTicks);
            if (getChildren().lastIndexOf(this) != getChildren().size() - 1) {
                gui.drawHorizontalLine(x - 1, x + entryWidth - 23 + 10, posY + 16, 0xFFFFFFFF);
            }

            if (btnEdit.isMouseOver()) {
                gui.drawHoveringText(I18n.format("gui.nekeys.voice_commands.settings"), x + 268, y + 18);
                // This is needed to prevent graphical bugs
                GlStateManager.disableRescaleNormal();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.disableDepthTest();
            }
        }

        private boolean checkValid(String text) {
            boolean valid = true;
            String reducedText = text.replaceAll("[\\[\\]<>()|+*]", " ");
            String[] words = reducedText.split(" ");
            for (String word : words) {
                if (word.matches(" *")) continue;
                if (!NotEnoughKeys.instance.voiceHandler.getRecognizer().isWord(word)) {
                    valid = false;
                }
            }
            Map<String, Integer> map = new HashMap<>();
            map.put("[]", 0);
            map.put("()", 0);
            map.put("<>", 0);

            char[] charArray = text.toCharArray();
            for (int i = 0; i < charArray.length && valid; i++) {
                char c = charArray[i];
                switch (c) {
                    case '[':
                        map.put("[]", map.get("[]") + 1);
                        break;
                    case ']':
                        map.put("[]", map.get("[]") - 1);
                        if (map.get("[]") < 0)
                            valid = false;
                        break;
                    case '(':
                        map.put("()", map.get("()") + 1);
                        break;
                    case ')':
                        map.put("()", map.get("()") - 1);
                        if (map.get("()") < 0)
                            valid = false;
                        break;
                    case '<':
                        map.put("<>", map.get("<>") + 1);
                        break;
                    case '>':
                        map.put("<>", map.get("<>") - 1);
                        if (map.get("<>") < 0)
                            valid = false;
                        break;
                    default:
                        break;
                }
            }
            if (map.get("[]") != 0 || map.get("()") != 0 || map.get("<>") != 0)
                valid = false;

            // Set color
            if (valid)
                ruleTextField.setTextColor(0xFFFFFF);
            else
                ruleTextField.setTextColor(0xFF0000);
            return valid;
        }

        private void checkName() {
            for (CommandEntry listEntry : getChildren()) {
                listEntry.nameTextField.setTextColor(0xFFFFFF);
                for (CommandEntry listEntry2 : getChildren()) {
                    if (listEntry != listEntry2 && listEntry.nameTextField.getText().equals(listEntry2.nameTextField.getText())) {
                        listEntry.nameTextField.setTextColor(0xFF0000);
                    }
                }
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (activeAction != null && activeAction.perform(this)) {
                return true;
            }
            if (this.nameTextField.mouseClicked(mouseX, mouseY, button)) {
                activeTextField = nameTextField;
                return true;
            } else if (this.ruleTextField.mouseClicked(mouseX, mouseY, button)) {
                activeTextField = ruleTextField;
                return true;
            } else if (this.btnEdit.mouseClicked(mouseX, mouseY, button)) {
                btnEdit.playPressSound(Minecraft.getInstance().getSoundHandler());
                gui.openSettings(this);
                return true;
            } else if (this.btnDelete.mouseClicked(mouseX, mouseY, button)) {
                btnDelete.playPressSound(Minecraft.getInstance().getSoundHandler());
                getChildren().remove(this);
            } else if (this.upButton.mouseClicked(mouseX, mouseY, button)) {
                upButton.playPressSound(Minecraft.getInstance().getSoundHandler());
                int index = getChildren().indexOf(this);
                if (index != 0) {
                    getChildren().remove(this);
                    getChildren().add(index - 1, this);
                }
            } else if (this.downButton.mouseClicked(mouseX, mouseY, button)) {
                downButton.playPressSound(Minecraft.getInstance().getSoundHandler());
                int index = getChildren().indexOf(this);
                if (index != getChildren().size() - 1) {
                    getChildren().remove(this);
                    getChildren().add(index + 1, this);
                }
            }
            activeTextField = null;
            return false;
        }

        @Override
        public boolean mouseReleased(double x, double y, int button) {
            return this.btnChangeKeyBinding.mouseReleased(x, y, button) || this.btnDelete.mouseReleased(x, y, button);
        }

        public GuiVoiceCommandList getList() {
            return GuiVoiceCommandList.this;
        }

        public IVoiceCommand getCommand() {
            return command;
        }

        public void setCommand(IVoiceCommand command) {
            this.command = command;
        }
    }
}
