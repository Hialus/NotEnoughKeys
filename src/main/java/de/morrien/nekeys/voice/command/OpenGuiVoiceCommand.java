package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.AbstractVoiceCommand;
import de.morrien.nekeys.gui.voice.GuiVoiceCommand;
import de.morrien.nekeys.gui.voice.popup.OpenGuiPopup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;

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
        VOICE_COMMANDS("gui.nekeys.popup.gui.voice_commands.name") {
            @Override
            public void openGui() {
                Minecraft.getInstance().setScreen(new GuiVoiceCommand(Minecraft.getInstance().screen));
            }
        },
        MENU("gui.nekeys.popup.gui.main_menu.name") {
            @Override
            public void openGui() {
                if (Minecraft.getInstance().level == null) {
                    Minecraft.getInstance().setScreen(new MainMenuScreen());
                } else {
                    Minecraft.getInstance().pauseGame(false);
                }
            }
        },
        CONTROLS("gui.nekeys.popup.gui.controls.name") {
            @Override
            public void openGui() {
                Minecraft.getInstance().setScreen(new ControlsScreen(null, Minecraft.getInstance().options));
            }
        },
        INVENTORY("gui.nekeys.popup.gui.inventory.name") {
            @Override
            public void openGui() {
                if (Minecraft.getInstance().level != null)
                    Minecraft.getInstance().setScreen(new InventoryScreen(Minecraft.getInstance().player));
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
