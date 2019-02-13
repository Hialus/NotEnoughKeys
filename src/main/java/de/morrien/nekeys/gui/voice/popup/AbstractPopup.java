package de.morrien.nekeys.gui.voice.popup;

import de.morrien.nekeys.voice.command.IVoiceCommand;
import net.minecraft.client.gui.Gui;

/**
 * Created by Timor Morrien
 */
public abstract class AbstractPopup extends Gui {

    protected String name;
    protected String rule;

    public AbstractPopup(String name, String rule) {
        this.name = name;
        this.rule = rule;
    }

    public AbstractPopup(IVoiceCommand voiceCommand) {
        this.name = voiceCommand.getName();
        this.rule = voiceCommand.getRuleContent();
    }

    public abstract void draw(int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks);

    public boolean onClick(int mouseX, int mouseY) {
        return false;
    }

    public void keyTyped(char typedChar, int keyCode) {}

    public void handleMouseInput() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public abstract IVoiceCommand getCommand();
}
