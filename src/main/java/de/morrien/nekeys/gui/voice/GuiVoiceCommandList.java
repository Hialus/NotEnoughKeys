package de.morrien.nekeys.gui.voice;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import de.morrien.nekeys.NotEnoughKeys;
import de.morrien.nekeys.Reference;
import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.gui.ScaleableButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Timor Morrien
 */
@OnlyIn(Dist.CLIENT)
public class GuiVoiceCommandList extends ContainerObjectSelectionList<GuiVoiceCommandList.CommandEntry> implements GuiEventListener {
    private final GuiVoiceCommand gui;
    private final Minecraft mc;
    public IAction activeAction;
    private EditBox activeTextField;

    public GuiVoiceCommandList(GuiVoiceCommand gui, Minecraft mcIn) {
        super(mcIn, gui.width, gui.height, 35, gui.height - 62, 25);
        this.gui = gui;
        this.mc = mcIn;
        this.setRenderBackground(false);
        this.setRenderTopAndBottom(false);

        loadCommands();
    }

    void loadCommands() {
        children().clear();
        for (IVoiceCommand voiceCommand : NotEnoughKeys.instance.voiceHandler.getVoiceCommands()) {
            children().add(new CommandEntry(voiceCommand));
        }
    }

    /**
     * Save the changes that have been made
     */
    void save() {
        // Use a Thread so the GUI won't freeze while saving
        new Thread(() -> {
            NotEnoughKeys.instance.voiceHandler.getVoiceCommands().clear();
            for (CommandEntry entry : children()) {
                NotEnoughKeys.instance.voiceHandler.addVoiceCommand(entry.getCommand());
            }
            NotEnoughKeys.instance.voiceHandler.updateGrammar();
            NotEnoughKeys.instance.voiceHandler.saveConfig();
        }).start();
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (activeTextField == null || !activeTextField.mouseClicked(pMouseX, pMouseY, pButton)) {
            return super.mouseClicked(pMouseX, pMouseY, pButton);
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

    CommandEntry newEntry(IVoiceCommand command) {
        return new CommandEntry(command);
    }

    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 45;
    }

    @Override
    public int getRowWidth() {
        return 340;
    }

    @Override
    protected void renderBackground(PoseStack matrixStack) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation("textures/block/jungle_planks.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // TODO: Was 7
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(0, this.height, 0).uv(0, this.height / 32F).color(150, 150, 150, 255).endVertex();
        bufferbuilder.vertex(this.width, this.height, 0).uv(this.width / 32F, this.height / 32F).color(150, 150, 150, 255).endVertex();
        bufferbuilder.vertex(this.width, 0, 0).uv(this.width / 32F, 0).color(150, 150, 150, 255).endVertex();
        bufferbuilder.vertex(0, 0, 0).uv(0, 0).color(150, 150, 150, 255).endVertex();
        tesselator.end();
    }

    @Override
    protected void renderList(PoseStack pPoseStack, int pX, int pY, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderList(pPoseStack, pX, pY, pMouseX, pMouseY, pPartialTick);
        this.renderTopAndBottom();
    }

    private void renderTopAndBottom() {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation("textures/block/spruce_planks.png"));
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(519);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.vertex(this.x0, this.y0, -100.0D).uv(0.0F, (float) this.y0 / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.x0 + this.width, this.y0, -100.0D).uv((float) this.width / 32.0F, (float) this.y0 / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.x0 + this.width, 0.0D, -100.0D).uv((float) this.width / 32.0F, 0.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.x0, 0.0D, -100.0D).uv(0.0F, 0.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.x0, this.height, -100.0D).uv(0.0F, (float) this.height / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.x0 + this.width, this.height, -100.0D).uv((float) this.width / 32.0F, (float) this.height / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.x0 + this.width, this.y1, -100.0D).uv((float) this.width / 32.0F, (float) this.y1 / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.vertex(this.x0, this.y1, -100.0D).uv(0.0F, (float) this.y1 / 32.0F).color(64, 64, 64, 255).endVertex();
        tesselator.end();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(this.x0, this.y0 + 4, 0.0D).color(0, 0, 0, 0).endVertex();
        bufferbuilder.vertex(this.x1, this.y0 + 4, 0.0D).color(0, 0, 0, 0).endVertex();
        bufferbuilder.vertex(this.x1, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.vertex(this.x0, this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.vertex(this.x0, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.vertex(this.x1, this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
        bufferbuilder.vertex(this.x1, this.y1 - 4, 0.0D).color(0, 0, 0, 0).endVertex();
        bufferbuilder.vertex(this.x0, this.y1 - 4, 0.0D).color(0, 0, 0, 0).endVertex();
        tesselator.end();
    }

    private List<CommandEntry> rows() {
        return this.children();
    }

    /**
     * A list @see{GuiListExtended.IGuiListEntry} to display a VoiceCommand
     */
    @OnlyIn(Dist.CLIENT)
    public class CommandEntry extends Entry<CommandEntry> {
        private final Button btnDelete;
        private final Button btnEdit;
        private final ScaleableButton upButton;
        private final ScaleableButton downButton;
        private final EditBox nameTextField;
        private final EditBox ruleTextField;
        private final ResourceLocation settingsIcon = new ResourceLocation(Reference.MODID, "textures/gui/settings.png");
        private IVoiceCommand command;

        private CommandEntry(IVoiceCommand command) {
            this.command = command;
            this.btnDelete = new ScaleableButton(0, 0, 50, 20, new TranslatableComponent("selectServer.delete"), this::deleteAction);
            this.btnEdit = new ScaleableButton(0, 0, 20, 20, new TextComponent("E"), this::editAction, (button, matrixStack, mouseX, mouseY) -> {
                gui.renderTooltip(matrixStack, Minecraft.getInstance().font.split(new TranslatableComponent("gui.nekeys.voice_commands.settings"), Math.max(gui.width / 2 - 43, 170)), mouseX, mouseY + 10);
            });
            this.upButton = new ScaleableButton(0, 0, 10, 10, new TextComponent("\u25B2"), this::moveUpAction);
            this.downButton = new ScaleableButton(0, 0, 10, 10, new TextComponent("\u25BC"), this::moveDownAction);

            this.nameTextField = new EditBox(Minecraft.getInstance().font, 0, 0, 40, 18, TextComponent.EMPTY);
            this.nameTextField.setValue(command.getName());
            this.ruleTextField = new EditBox(Minecraft.getInstance().font, 0, 0, 200, 18, TextComponent.EMPTY);
            this.ruleTextField.setMaxLength(Integer.MAX_VALUE);
            this.ruleTextField.setValue(command.getRuleContent());
            checkValid(ruleTextField.getValue());
            checkName();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(btnDelete, btnEdit, upButton, downButton, nameTextField, ruleTextField);
        }

        private void deleteAction(Button button) {
            button.playDownSound(Minecraft.getInstance().getSoundManager());
            rows().remove(this);
        }

        private void editAction(Button button) {
            button.playDownSound(Minecraft.getInstance().getSoundManager());
            gui.openSettings(this);
        }

        private void moveUpAction(Button button) {
            upButton.playDownSound(Minecraft.getInstance().getSoundManager());
            int index = rows().indexOf(this);
            if (index != 0) {
                rows().remove(this);
                rows().add(index - 1, this);
            }
        }

        private void moveDownAction(Button button) {
            downButton.playDownSound(Minecraft.getInstance().getSoundManager());
            int index = rows().indexOf(this);
            if (index != rows().size() - 1) {
                rows().remove(this);
                rows().add(index + 1, this);
            }
        }

        @Override
        public void render(PoseStack matrixStack, int pIndex, int pTop, int pLeft, int pWidth, int pHeight, int pMouseX, int pMouseY, boolean pIsMouseOver, float pPartialTicks) {
            command.setName(nameTextField.getValue());
            if (!ruleTextField.getValue().equals(command.getRuleContent())) {
                String text = ruleTextField.getValue();
                checkValid(text);
                command.setRuleContent(ruleTextField.getValue());
            }
            checkName();
            GlStateManager._disableDepthTest();
            int x = gui.width / 2 - 340 / 2;
            int y = pTop;

            int posY = y + pHeight / 2 - GuiVoiceCommandList.this.mc.font.lineHeight / 2;
            this.nameTextField.x = x;
            this.nameTextField.y = posY - 5;
            this.nameTextField.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
            vLine(matrixStack, x + 45, posY - 7, posY + 14, 0xFFFFFFFF);
            this.ruleTextField.x = x + 51;
            this.ruleTextField.y = posY - 5;
            this.ruleTextField.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
            this.btnEdit.x = x + 256;
            this.btnEdit.y = y;
            this.btnEdit.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
            RenderSystem.setShaderTexture(0, settingsIcon);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            blit(matrixStack, x + 258, posY - 4, 0, 0, 16, 15, 16, 16);

            this.upButton.x = x + 278;
            this.upButton.y = y;
            this.upButton.fontScale = 0.9;
            this.upButton.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
            this.downButton.x = x + 278;
            this.downButton.y = y + 10;
            this.downButton.fontScale = 0.9;
            this.downButton.render(matrixStack, pMouseX, pMouseY, pPartialTicks);

            this.btnDelete.x = x + 280 + 10;
            this.btnDelete.y = y;
            this.btnDelete.render(matrixStack, pMouseX, pMouseY, pPartialTicks);
            if (rows().lastIndexOf(this) != rows().size() - 1) {
                hLine(matrixStack, x - 1, x + 339, posY + 16, 0xFFFFFFFF);
            }

            if (btnEdit.isMouseOver(pMouseX, pMouseY)) {
                btnEdit.renderToolTip(matrixStack, pMouseX, pMouseY);
                GlStateManager._disableDepthTest();
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
            for (CommandEntry listEntry : rows()) {
                listEntry.nameTextField.setTextColor(0xFFFFFF);
                for (CommandEntry listEntry2 : rows()) {
                    if (listEntry != listEntry2 && listEntry.nameTextField.getValue().equals(listEntry2.nameTextField.getValue())) {
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
                gui.openSettings(this);
                return true;
            } else if (this.btnDelete.mouseClicked(mouseX, mouseY, button)) {
                rows().remove(this);
            } else if (this.upButton.mouseClicked(mouseX, mouseY, button)) {
                int index = rows().indexOf(this);
                if (index != 0) {
                    rows().remove(this);
                    rows().add(index, this);
                }
            } else if (this.downButton.mouseClicked(mouseX, mouseY, button)) {
                int index = rows().indexOf(this);
                if (index != rows().size() - 1) {
                    rows().remove(this);
                    rows().add(index, this);
                }
            }
            activeTextField = null;
            return false;
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

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.emptyList();
        }
    }
}
