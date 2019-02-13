package de.morrien.nekeys.voice.command;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import java.util.List;

/**
 * Created by Timor Morrien
 */
public abstract class AbstractVoiceKeybind extends AbstractVoiceCommand {

    protected KeyBinding keybind;

    protected AbstractVoiceKeybind() {}

    public AbstractVoiceKeybind(String name, String command, KeyBinding keybind) {
        super(name, command);
        this.keybind = keybind;
    }

    @Override
    public List<String> getConfigParams() {
        List<String> params = super.getConfigParams();
        params.add(keybind.getKeyDescription());
        return params;
    }

    @Override
    public void fromConfigParams(String[] params) {
        super.fromConfigParams(params);
        this.keybind = getKeybindByDescription(params[3]);
    }

    protected KeyBinding getKeybindByDescription(String description) {
        for (KeyBinding keyBinding : Minecraft.getMinecraft().gameSettings.keyBindings) {
            if (keyBinding.getKeyDescription().equalsIgnoreCase(description)) {
                return keyBinding;
            }
        }
        return null;
    }

    public KeyBinding getKeybind() {
        return keybind;
    }

    public void setKeybind(KeyBinding keybind) {
        this.keybind = keybind;
    }
}