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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Timor Morrien
 */
@SideOnly(Side.CLIENT)
public class GuiVoiceCommandList extends GuiListExtended {

    private final GuiVoiceCommand gui;
    private final Minecraft mc;
    public IAction activeAction;
    List<CommandEntry> listEntries;
    private GuiTextField activeTextField;

    public GuiVoiceCommandList(GuiVoiceCommand gui, Minecraft mcIn) {
        super(mcIn, gui.width, gui.height, 35, gui.height - 62, 25);
        this.gui = gui;
        this.mc = mcIn;

        loadCommands();
    }

    void loadCommands() {
        listEntries = new ArrayList<>();
        for (IVoiceCommand voiceCommand : NotEnoughKeys.instance.voiceHandler.getVoiceCommands()) {
            listEntries.add(new CommandEntry(voiceCommand));
        }
    }

    /**
     * Save the changes that have been made
     */
    void save() {
        // Use a Thread so the GUI won't freeze while saving
        new Thread(() -> {
            NotEnoughKeys.instance.voiceHandler.getVoiceCommands().clear();
            for (CommandEntry entry : listEntries) {
                NotEnoughKeys.instance.voiceHandler.addVoiceCommand(entry.getCommand());
            }
            NotEnoughKeys.instance.voiceHandler.updateGrammar();
            NotEnoughKeys.instance.voiceHandler.saveConfig();
        }).start();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
        if (activeTextField == null || !activeTextField.mouseClicked(mouseX, mouseY, mouseEvent)) {
            return super.mouseClicked(mouseX, mouseY, mouseEvent);
        }
        return true;
    }

    /**
     * KeyTyped method to handle input for the textfields
     *
     * @param typedChar
     * @param keyCode
     * @return if the key type was used
     */
    boolean keyTyped(char typedChar, int keyCode) {
        if (activeTextField != null) {
            activeTextField.textboxKeyTyped(typedChar, keyCode);
            return true;
        }
        return false;
    }

    public void update() {
        if (activeTextField != null)
            activeTextField.updateCursorCounter();
    }

    CommandEntry newEntry(IVoiceCommand command) {
        return new CommandEntry(command);
    }

    /*
     * Overridden methods
     */

    @Override
    protected int getSize() {
        return listEntries.size();
    }

    @Override
    public CommandEntry getListEntry(int index) {
        return listEntries.get(index);
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
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/blocks/planks_jungle.png"));
        GlStateManager.color(1F, 1F, 1F, 1F);
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
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/blocks/planks_big_oak.png"));
        GlStateManager.color(1F, 1F, 1F, 1F);
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
    @SideOnly(Side.CLIENT)
    public class CommandEntry implements GuiListExtended.IGuiListEntry {
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
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 95, 20, "");
            this.btnDelete = new BetterButton(0, 0, 0, 50, 20, I18n.format("selectServer.delete"));
            this.btnEdit = new GuiButton(0, 0, 0, 20, 20, "E");
            this.upButton = new BetterButton(0, 0, 0, 10, 10, "\u25B2");
            this.downButton = new BetterButton(0, 0, 0, 10, 10, "\u25BC");

            this.nameTextField = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, 40, 18);
            this.nameTextField.setText(command.getName());
            this.ruleTextField = new GuiTextField(0, Minecraft.getMinecraft().fontRenderer, 0, 0, 200, 18);
            this.ruleTextField.setMaxStringLength(Integer.MAX_VALUE);
            this.ruleTextField.setText(command.getRuleContent());
            checkValid(ruleTextField.getText());
            checkName();
        }

        @Override
        public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
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
            GlStateManager.disableDepth();

            int posY = y + slotHeight / 2 - GuiVoiceCommandList.this.mc.fontRenderer.FONT_HEIGHT / 2;
            this.nameTextField.x = x;
            this.nameTextField.y = posY - 5;
            this.nameTextField.drawTextBox();
            gui.drawVerticalLine(x + 45, posY - 7, posY + 14, 0xFFFFFFFF);
            this.ruleTextField.x = x + 51;
            this.ruleTextField.y = posY - 5;
            this.ruleTextField.drawTextBox();
            this.btnEdit.x = x + 256;
            this.btnEdit.y = y;
            this.btnEdit.drawButton(mc, mouseX, mouseY, partialTicks);
            Minecraft.getMinecraft().getTextureManager().bindTexture(settingsIcon);
            GlStateManager.color(1, 1, 1);
            Gui.drawModalRectWithCustomSizedTexture(x + 258, posY - 4, 0, 0, 16, 15, 16, 16);

            this.upButton.x = x + 278;
            this.upButton.y = y;
            this.upButton.fontScale = 0.9;
            this.upButton.drawButton(mc, mouseX, mouseY, partialTicks);
            this.downButton.x = x + 278;
            this.downButton.y = y + 10;
            this.downButton.fontScale = 0.9;
            this.downButton.drawButton(mc, mouseX, mouseY, partialTicks);

            this.btnDelete.x = x + 280 + 10;
            this.btnDelete.y = y;
            this.btnDelete.drawButton(mc, mouseX, mouseY, partialTicks);
            if (listEntries.lastIndexOf(this) != listEntries.size() - 1) {
                gui.drawHorizontalLine(x - 1, x + listWidth - 23 + 10, posY + 16, 0xFFFFFFFF);
            }

            if (btnEdit.isMouseOver()) {
                gui.drawHoveringText(I18n.format("gui.nekeys.voice_commands.settings"), x + 268, y + 18);
                // This is needed to prevent graphical bugs
                GlStateManager.disableRescaleNormal();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
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
            for (CommandEntry listEntry : listEntries) {
                listEntry.nameTextField.setTextColor(0xFFFFFF);
                for (CommandEntry listEntry2 : listEntries) {
                    if (listEntry != listEntry2 && listEntry.nameTextField.getText().equals(listEntry2.nameTextField.getText())) {
                        listEntry.nameTextField.setTextColor(0xFF0000);
                    }
                }
            }
        }

        @Override
        public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
            if (activeAction != null && activeAction.perform(this)) {
                return true;
            }
            if (this.nameTextField.mouseClicked(mouseX, mouseY, mouseEvent)) {
                activeTextField = nameTextField;
                return true;
            } else if (this.ruleTextField.mouseClicked(mouseX, mouseY, mouseEvent)) {
                activeTextField = ruleTextField;
                return true;
            } else if (this.btnEdit.mousePressed(mc, mouseX, mouseY)) {
                btnEdit.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                gui.openSettings(this);
                return true;
            } else if (this.btnDelete.mousePressed(mc, mouseX, mouseY)) {
                btnDelete.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                listEntries.remove(this);
            } else if (this.upButton.mousePressed(mc, mouseX, mouseY)) {
                upButton.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                int index = listEntries.indexOf(this);
                if (index != 0) {
                    listEntries.remove(this);
                    listEntries.add(index - 1, this);
                }
            } else if (this.downButton.mousePressed(mc, mouseX, mouseY)) {
                downButton.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                int index = listEntries.indexOf(this);
                if (index != listEntries.size() - 1) {
                    listEntries.remove(this);
                    listEntries.add(index + 1, this);
                }
            }
            activeTextField = null;
            return false;
        }

        @Override
        public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
            this.btnChangeKeyBinding.mouseReleased(x, y);
            this.btnDelete.mouseReleased(x, y);
        }

        @Override
        public void updatePosition(int slotIndex, int x, int y, float partialTicks) {
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
