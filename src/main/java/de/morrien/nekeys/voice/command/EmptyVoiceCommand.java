package de.morrien.nekeys.voice.command;

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
}
