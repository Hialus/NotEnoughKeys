package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.AbstractVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.voice.popup.EmptyPopup;

/**
 * Created by Timor Morrien
 */
public class EmptyVoiceCommand extends AbstractVoiceCommand {

    public EmptyVoiceCommand() {

    }

    public EmptyVoiceCommand(String name, String command) {
        super(name, command);
    }

    @Override
    public void activate(String voiceCommand) {
        // Do nothing
    }

    public static class Factory extends VoiceCommandFactory<EmptyVoiceCommand> {

        @Override
        public EmptyVoiceCommand newCommand(String[] params) {
            EmptyVoiceCommand voiceCommand = new EmptyVoiceCommand();
            voiceCommand.fromConfigParams(params);
            return voiceCommand;
        }

        @Override
        public EmptyVoiceCommand newCommand(String name, String rule) {
            return new EmptyVoiceCommand(name, rule);
        }

        @Override
        public AbstractPopup newPopup(EmptyVoiceCommand command) {
            return new EmptyPopup(command);
        }
    }
}
