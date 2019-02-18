package de.morrien.nekeys.api.popup;

import de.morrien.nekeys.api.command.IVoiceCommand;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.IGuiEventListener;

/**
 * Represents a popup for a VoiceCommand. This popup will be presented to user to change the settings of the command
 *
 * @author Timor Morrien
 */
public abstract class AbstractPopup extends Gui implements IGuiEventListener {

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
