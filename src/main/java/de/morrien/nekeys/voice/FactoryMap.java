package de.morrien.nekeys.voice;

import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;

import java.util.HashMap;
import java.util.function.BiConsumer;

/**
 * Created by Timor Morrien
 */
@SuppressWarnings("unchecked")
public class FactoryMap {
    private final HashMap<Class<? extends IVoiceCommand>, VoiceCommandFactory<? extends IVoiceCommand>> map;

    public FactoryMap() {
        map = new HashMap<>();
    }

    public <T extends IVoiceCommand> void put(Class<T> clazz, VoiceCommandFactory<T> factory) {
        map.put(clazz, factory);
    }

    public <T extends IVoiceCommand> VoiceCommandFactory<T> get(Class<T> clazz) {
        return (VoiceCommandFactory<T>) map.get(clazz);
    }

    public <T extends IVoiceCommand> T newVoiceCommand(T voiceCommand) {
        Class<T> clazz = (Class<T>) voiceCommand.getClass();
        return get(clazz).newCommand(voiceCommand);
    }

    public <T extends IVoiceCommand> AbstractPopup newPopup(T voiceCommand) {
        Class<T> clazz = (Class<T>) voiceCommand.getClass();
        return get(clazz).newPopup(voiceCommand);
    }

    public void forEach(BiConsumer<Class<? extends IVoiceCommand>, VoiceCommandFactory<? extends IVoiceCommand>> consumer) {
        map.forEach(consumer);
    }
}
