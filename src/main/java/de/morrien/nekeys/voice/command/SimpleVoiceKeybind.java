package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.IVoiceCommandTickable;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.voice.popup.SimpleKeybindPopup;
import net.minecraft.client.settings.KeyBinding;

/**
 * Created by Timor Morrien
 */
public class SimpleVoiceKeybind extends AbstractVoiceKeybind implements IVoiceCommandTickable {

    private int ticksLeft;

    private SimpleVoiceKeybind() {
    }

    public SimpleVoiceKeybind(String name, String command, KeyBinding keybind) {
        super(name, command, keybind);
    }

    @Override
    public void activate(String voiceCommand) {
        if (keybind == null) return;
        KeyBinding.click(keybind.getKey());
        KeyBinding.set(keybind.getKey(), true);
        ticksLeft = 1;
    }

    @Override
    public void tick() {
        if (keybind != null && ticksLeft >= 0) {
            ticksLeft--;
            if (ticksLeft == 0) {
                KeyBinding.set(keybind.getKey(), false);
            }
        }
    }

    public static class Factory extends VoiceCommandFactory<SimpleVoiceKeybind> {

        @Override
        public SimpleVoiceKeybind newCommand(String[] params) {
            SimpleVoiceKeybind voiceCommand = new SimpleVoiceKeybind();
            voiceCommand.fromConfigParams(params);
            return voiceCommand;
        }

        @Override
        public SimpleVoiceKeybind newCommand(String name, String rule) {
            return new SimpleVoiceKeybind(name, rule, null);
        }

        @Override
        public AbstractPopup newPopup(SimpleVoiceKeybind command) {
            return new SimpleKeybindPopup(command);
        }
    }
}
