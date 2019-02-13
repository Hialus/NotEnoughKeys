package de.morrien.nekeys.gui.voice;

/**
 * Created by Timor Morrien
 */
@FunctionalInterface
public interface IAction {
    /**
     * Perform this action
     * If the action was successful it may remove itself from the GuiVoiceCommandList object, which can be obtained from the CommandEntry
     * @param commandEntry The entry that calls this action
     * @return If the action was successful
     */
    boolean perform(GuiVoiceCommandList.CommandEntry commandEntry);
}
