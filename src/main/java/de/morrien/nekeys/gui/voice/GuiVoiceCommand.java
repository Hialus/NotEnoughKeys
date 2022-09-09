package de.morrien.nekeys.gui.voice;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import de.morrien.nekeys.NotEnoughKeys;
import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.DropDownList;
import de.morrien.nekeys.gui.ScaleableButton;
import de.morrien.nekeys.voice.command.EmptyVoiceCommand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * Created by Timor Morrien
 */
@OnlyIn(Dist.CLIENT)
public class GuiVoiceCommand extends Screen {
    // A reference to the screen object that created this. Used for navigating between screens.
    private final Screen parentScreen;
    private GuiVoiceCommandList voiceCommandList;
    private GuiVoiceCommandSettings activeSettings;

    public GuiVoiceCommand() {
        this(null);
    }

    public GuiVoiceCommand(Screen screen) {
        super(new TranslatableComponent("gui.nekeys.voice_commands.title"));
        if (screen instanceof GuiVoiceCommand)
            screen = null;
        this.parentScreen = screen;
    }

    public void openSettings(GuiVoiceCommandList.CommandEntry entry) {
        activeSettings = new GuiVoiceCommandSettings(entry);
        if (activeSettings.voiceCommandPopup == null) {
            activeSettings = null;
            NotEnoughKeys.logger.warn("Could not open popup for entry \"" + entry.getClass().getName() + "\"");
        }
    }

    @Override
    public void renderBackground(PoseStack pPoseStack) {

    }

    /**
     * Init the GUI components
     */
    @Override
    public void init() {
        this.voiceCommandList = new GuiVoiceCommandList(this, this.minecraft);
        ((List<GuiEventListener>) this.children()).add(this.voiceCommandList);
        addRenderableWidget(new ScaleableButton(this.width / 2 - 165, this.height - 29, 150, 20, new TranslatableComponent("gui.done"), button -> {
            voiceCommandList.save();
            minecraft.setScreen(parentScreen);
        }));
        addRenderableWidget(new ScaleableButton(this.width / 2 + 15, this.height - 29, 150, 20, new TranslatableComponent("gui.nekeys.reload"), button -> {
            NotEnoughKeys.instance.voiceHandler.reloadConfig();
            voiceCommandList.loadCommands();
        }));
        addRenderableWidget(new ScaleableButton(this.width / 2 - 165, this.height - 54, 150, 20, new TranslatableComponent("gui.nekeys.createNew"), button -> {
            voiceCommandList.children().add(voiceCommandList.newEntry(new EmptyVoiceCommand("empty", "")));
        }));
        addRenderableWidget(new ScaleableButton(this.width / 2 + 15, this.height - 54, 150, 20, new TranslatableComponent("gui.nekeys.clone"), button -> {
            voiceCommandList.activeAction = commandEntry -> {
                int index = voiceCommandList.children().indexOf(commandEntry);
                IVoiceCommand command = NotEnoughKeys.instance.voiceHandler.factoryMap.newVoiceCommand(commandEntry.getCommand());
                GuiVoiceCommandList.CommandEntry entry = voiceCommandList.newEntry(command);
                voiceCommandList.children().add(index, entry);
                voiceCommandList.activeAction = null;
                return true;
            };
        }));
    }

    @Override
    public void onClose() {
        voiceCommandList.save();
        super.onClose();
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (activeSettings != null)
            return activeSettings.mouseScrolled(pMouseX, pMouseY, pDelta);
        return voiceCommandList.mouseScrolled(pMouseX, pMouseY, pDelta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (activeSettings != null) {
            return activeSettings.mouseClicked(mouseX, mouseY, button);
        }
        if (this.voiceCommandList.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != 0 || !this.voiceCommandList.mouseReleased(mouseX, mouseY, button)) {
            super.mouseReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseX, button);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        if (activeSettings != null) {
            return activeSettings.voiceCommandPopup.charTyped(typedChar, keyCode);
        }
        if (this.voiceCommandList.charTyped(typedChar, keyCode)) {
            return true;
        }

        return super.charTyped(typedChar, keyCode);
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (activeSettings != null) {
            return activeSettings.voiceCommandPopup.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
        }
        if (this.voiceCommandList.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)) {
            return true;
        }

        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
        if (activeSettings != null) {
            return activeSettings.voiceCommandPopup.keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_);
        }
        if (this.voiceCommandList.keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_)) {
            return true;
        }

        return super.keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int mX = mouseX;
        int mY = mouseY;
        if (activeSettings != null) {
            mX = -1;
            mY = -1;
        }
        if (voiceCommandList == null) return;
        this.voiceCommandList.render(matrixStack, mX, mouseY, partialTicks);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 8, 16777215);
        int x = width / 2 - 340 / 2;
        drawCenteredString(matrixStack, this.font, new TranslatableComponent("gui.nekeys.voice_commands.name"), x + 20, 25, ChatFormatting.GOLD.getColor());
        drawString(matrixStack, this.font, new TranslatableComponent("gui.nekeys.voice_commands.rule"), x + 60, 25, ChatFormatting.GOLD.getColor());
        super.render(matrixStack, mX, mouseY, partialTicks);

        if (activeSettings != null) {
            activeSettings.draw(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    public class GuiVoiceCommandSettings {
        private final GuiVoiceCommandList.CommandEntry commandEntry;
        private AbstractPopup voiceCommandPopup;
        private final DropDownList<Class<? extends IVoiceCommand>> dropDownList;
        private Class<? extends IVoiceCommand> lastSelection;
        private Button saveButton;

        public GuiVoiceCommandSettings(GuiVoiceCommandList.CommandEntry entry) {
            this.commandEntry = entry;
            this.dropDownList = new DropDownList<>(0, 0, 0, 18, 6);
            this.dropDownList.stringifier = clazz -> I18n.get("voiceCommand." + clazz.getSimpleName() + ".name");
            NotEnoughKeys.instance.voiceHandler.factoryMap.forEach((voiceCommandClass, factory) -> {
                dropDownList.optionsList.add(voiceCommandClass);
                if (voiceCommandClass.equals(commandEntry.getCommand().getClass())) {
                    dropDownList.selection = voiceCommandClass;
                    voiceCommandPopup = NotEnoughKeys.instance.voiceHandler.factoryMap.newPopup(commandEntry.getCommand());
                    lastSelection = dropDownList.selection;
                }
            });
            dropDownList.optionsList.sort((o1, o2) -> dropDownList.stringifier.toString(o1).compareToIgnoreCase(dropDownList.stringifier.toString(o2)));
            this.saveButton = new ScaleableButton(width / 2 + 100 - 45, height / 2 + 60 - 25, 40, 20, new TextComponent("Save"), button -> {
                saveButton.playDownSound(Minecraft.getInstance().getSoundManager());
                commandEntry.setCommand(voiceCommandPopup.getCommand());
                activeSettings = null;
            });
        }

        public void draw(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            fill(matrixStack, 0, 0, width, height, 0x77000000);
            fill(matrixStack, width / 2 - 101, height / 2 - 61, width / 2 + 101, height / 2 + 61, 0xFFFFFFFF);

            double dialogX = width / 2D - 100;
            double dialogY = height / 2D - 60;
            float dialogWidth = 200;
            float dialogHeight = 120;

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, new ResourceLocation("textures/block/oak_planks.png"));
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            bufferbuilder.vertex(dialogX, dialogY + dialogHeight, 0).uv(0, dialogHeight / 32F).color(150, 150, 150, 255).endVertex();
            bufferbuilder.vertex(dialogX + dialogWidth, dialogY + dialogHeight, 0).uv(dialogWidth / 32F, dialogHeight / 32F).color(150, 150, 150, 255).endVertex();
            bufferbuilder.vertex(dialogX + dialogWidth, dialogY, 0).uv(dialogWidth / 32F, 0).color(150, 150, 150, 255).endVertex();
            bufferbuilder.vertex(dialogX, dialogY, 0).uv(0, 0).color(150, 150, 150, 255).endVertex();
            tesselator.end();

            saveButton.render(matrixStack, mouseX, mouseY, partialTicks);

            if (lastSelection != dropDownList.selection) {
                commandEntry.setCommand(NotEnoughKeys.instance.voiceHandler.factoryMap.get(dropDownList.selection).newCommand(commandEntry.getCommand().getName(), commandEntry.getCommand().getRuleContent()));
                voiceCommandPopup = NotEnoughKeys.instance.voiceHandler.factoryMap.newPopup(commandEntry.getCommand());
                lastSelection = dropDownList.selection;
            }

            voiceCommandPopup.draw(matrixStack, (int) dialogX, (int) dialogY + 22, (int) dialogWidth, (int) dialogHeight - 22, mouseX, mouseY, partialTicks);

            dropDownList.x = (int) (dialogX + 5);
            dropDownList.y = (int) (dialogY + 3);
            dropDownList.width = (int) (dialogWidth - 10);
            dropDownList.draw(matrixStack);
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX <= width / 2D - 102 ||
                    mouseX >= width / 2D + 102 ||
                    mouseY <= height / 2D - 62 ||
                    mouseY >= height / 2D + 62) {
                activeSettings = null;
                return true;
            }
            return dropDownList.mouseClicked(mouseX, mouseY, button) || voiceCommandPopup.mouseClicked(mouseX, mouseY, button) || saveButton.mouseClicked(mouseX, mouseY, button);
        }

        public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
            return dropDownList.mouseScrolled(pMouseX, pMouseY, pDelta) || voiceCommandPopup.mouseScrolled(pMouseX, pMouseY, pDelta);
        }
    }
}
