package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.NotEnoughKeys;

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
            NotEnoughKeys.instance.presetManager.getPreset(preset-1).load();
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
}
