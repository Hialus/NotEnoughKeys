package de.morrien.nekeys.api.command;

/**
 * Implement this in a VoiceCommand to let it perform an action every tick
 *
 * @author Timor Morrien
 */
public interface IVoiceCommandTickable extends IVoiceCommand {

    /**
     * Action to be executed each tick
     */
    void tick();
}
