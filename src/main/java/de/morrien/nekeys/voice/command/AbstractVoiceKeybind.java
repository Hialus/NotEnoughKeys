package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.api.command.AbstractVoiceCommand;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.List;

/**
 * Created by Timor Morrien
 */
public abstract class AbstractVoiceKeybind extends AbstractVoiceCommand {

    protected KeyMapping keybind;

    protected AbstractVoiceKeybind() {
    }

    public AbstractVoiceKeybind(String name, String command, KeyMapping keybind) {
        super(name, command);
        this.keybind = keybind;
    }

    @Override
    public List<String> getConfigParams() {
        List<String> params = super.getConfigParams();
        params.add(keybind == null ? "" : keybind.getName());
        return params;
    }

    @Override
    public void fromConfigParams(String[] params) {
        super.fromConfigParams(params);
        this.keybind = getKeybindByDescription(params[3]);
    }

    protected KeyMapping getKeybindByDescription(String description) {
        for (KeyMapping keyBinding : Minecraft.getInstance().options.keyMappings) {
            if (keyBinding.getName().equalsIgnoreCase(description)) {
                return keyBinding;
            }
        }
        return null;
    }

    public KeyMapping getKeybind() {
        return keybind;
    }

    public void setKeybind(KeyMapping keybind) {
        this.keybind = keybind;
    }
}
