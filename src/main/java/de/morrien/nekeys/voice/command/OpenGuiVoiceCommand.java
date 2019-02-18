package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.AbstractVoiceCommand;
import de.morrien.nekeys.gui.voice.GuiVoiceCommand;
import de.morrien.nekeys.gui.voice.popup.OpenGuiPopup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.inventory.GuiInventory;

import java.util.List;

/**
 * Created by Timor Morrien
 */
public class OpenGuiVoiceCommand extends AbstractVoiceCommand {

    protected AllowedGuis gui;

    protected OpenGuiVoiceCommand() {
    }

    public OpenGuiVoiceCommand(String name, String command, AllowedGuis gui) {
        super(name, command);
        this.gui = gui;
    }

    @Override
    public void activate(String voiceCommand) {
        if (gui != null) {
            gui.openGui();
        }
    }

    @Override
    public List<String> getConfigParams() {
        List<String> params = super.getConfigParams();
        params.add(gui.name());
        return params;
    }

    @Override
    public void fromConfigParams(String[] params) {
        super.fromConfigParams(params);
        this.gui = AllowedGuis.valueOf(params[3]);
    }

    public AllowedGuis getGui() {
        return gui;
    }

    public void setGui(AllowedGuis gui) {
        this.gui = gui;
    }

    public enum AllowedGuis {
        MENU("gui.nekeys.mainmenu.name") {
            @Override
            public void openGui() {
                if (Minecraft.getInstance().world == null) {
                    Minecraft.getInstance().displayGuiScreen(new GuiMainMenu());
                } else {
                    Minecraft.getInstance().displayInGameMenu();
                }
            }
        },
        CONTROLS("gui.nekeys.controls.name") {
            @Override
            public void openGui() {
                Minecraft.getInstance().displayGuiScreen(new GuiControls(null, Minecraft.getInstance().gameSettings));
            }
        },
        VOICE_COMMANDS("gui.nekeys.voice_commands.title") {
            @Override
            public void openGui() {
                Minecraft.getInstance().displayGuiScreen(new GuiVoiceCommand());
            }
        },
        INVENTORY("gui.nekeys.inventory.name") {
            @Override
            public void openGui() {
                if (Minecraft.getInstance().world != null)
                    Minecraft.getInstance().displayGuiScreen(new GuiInventory(Minecraft.getInstance().player));
            }
        };

        public final String unlocalizedName;

        AllowedGuis(String unlocalizedName) {
            this.unlocalizedName = unlocalizedName;
        }

        public abstract void openGui();

        public boolean active() {
            return true;
        }
    }

    public static class Factory extends VoiceCommandFactory<OpenGuiVoiceCommand> {

        @Override
        public OpenGuiVoiceCommand newCommand(String[] params) {
            OpenGuiVoiceCommand openGuiVoiceCommand = new OpenGuiVoiceCommand();
            openGuiVoiceCommand.fromConfigParams(params);
            return openGuiVoiceCommand;
        }

        @Override
        public OpenGuiVoiceCommand newCommand(String name, String rule) {
            return new OpenGuiVoiceCommand(name, rule, AllowedGuis.VOICE_COMMANDS);
        }

        @Override
        public OpenGuiPopup newPopup(OpenGuiVoiceCommand command) {
            return new OpenGuiPopup(command);
        }
    }
}
