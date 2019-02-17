package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.NotEnoughKeys;
import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.AbstractVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.voice.popup.SelectPresetPopup;

import java.util.List;

/**
 * Created by Timor Morrien
 */
public class SelectPresetVoiceCommand extends AbstractVoiceCommand {
    protected int preset = 0;

    public SelectPresetVoiceCommand() {
        super();
    }

    public SelectPresetVoiceCommand(String name, String command, int preset) {
        super(name, command);
        this.preset = preset;
    }

    @Override
    public void activate(String voiceCommand) {
        if (preset > 0) {
            NotEnoughKeys.instance.presetManager.getPreset(preset - 1).load();
        }
    }

    @Override
    public List<String> getConfigParams() {
        List<String> list = super.getConfigParams();
        list.add(String.valueOf(preset));
        return list;
    }

    @Override
    public void fromConfigParams(String[] params) {
        super.fromConfigParams(params);
        this.preset = Integer.parseInt(params[3]);
    }

    public int getPreset() {
        return preset;
    }

    public void setPreset(int preset) {
        this.preset = preset;
    }

    public static class Factory extends VoiceCommandFactory<SelectPresetVoiceCommand> {

        @Override
        public SelectPresetVoiceCommand newCommand(String[] params) {
            SelectPresetVoiceCommand voiceCommand = new SelectPresetVoiceCommand();
            voiceCommand.fromConfigParams(params);
            return voiceCommand;
        }

        @Override
        public SelectPresetVoiceCommand newCommand(String name, String rule) {
            return new SelectPresetVoiceCommand(name, rule, 0);
        }

        @Override
        public AbstractPopup newPopup(SelectPresetVoiceCommand command) {
            return new SelectPresetPopup(command);
        }
    }
}
