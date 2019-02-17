package de.morrien.nekeys.api;

import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;

import java.util.List;

public abstract class VoiceCommandFactory<T extends IVoiceCommand> {

    public abstract T newCommand(String[] params);

    public T newCommand(T command) {
        List<String> params = command.getConfigParams();
        params.add(0, command.getClass().getName());
        return newCommand(params.toArray(new String[0]));
    }

    public abstract T newCommand(String name, String rule);

    public abstract AbstractPopup newPopup(T command);
}
