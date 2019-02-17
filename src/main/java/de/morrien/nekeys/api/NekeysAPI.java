package de.morrien.nekeys.api;

import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.preset.PresetManager;

import static de.morrien.nekeys.NotEnoughKeys.instance;

/**
 * Main class for using the Not Enough Keys API
 * IMPORTANT: As this mod is still under active development, so the API will change in the future.
 */
public class NekeysAPI {

    /**
     * The current version of the API
     * It follows the basic Major.Minor scheme.
     */
    public static final String API_VERSION = "1.0";

    /**
     * Add a voice command to be used by the player.
     *
     * @param command Class of the VoiceCommand to be used
     * @param factory The factory to create instances of VoiceCommands an Popups
     */
    public static <T extends IVoiceCommand> void addVoiceCommand(Class<T> command, VoiceCommandFactory<T> factory) {
        instance.voiceHandler.bind(command, factory);
    }

    /**
     * Load a preset.
     *
     * @param presetID Needs to be between 1 and 10
     * @throws IllegalArgumentException When presetID is out of bounds
     */
    public static void loadPreset(int presetID) {
        PresetManager.Preset preset = instance.presetManager.getPreset(presetID - 1);
        if (preset != null)
            preset.load();
        else
            throw new IllegalArgumentException("presetID needs to be between 1 and 10. Got: " + presetID);
    }

    /**
     * @return Array containing all currently loaded VoiceCommands
     */
    public static IVoiceCommand[] getLoadedVoiceCommands() {
        return instance.voiceHandler.getVoiceCommands().toArray(new IVoiceCommand[0]);
    }
}
