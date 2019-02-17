package de.morrien.nekeys.api.command;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The interface to represent a VoiceCommand.
 *
 * @author Timor Morrien
 */
public interface IVoiceCommand {

    /**
     * Check if the given String matches this VoiceCommand's rule
     *
     * @param voiceCommand The recognized text
     * @return true if it matches the rule of this VoiceCommand
     */
    boolean isValidCommand(String voiceCommand);

    /**
     * Activate the action of this VoiceCommand
     *
     * @param voiceCommand The recognized text
     */
    void activate(String voiceCommand);

    /**
     * @return The (unique) name of this VoiceCommand
     */
    String getName();

    /**
     * Set the name of this VoiceCommand
     *
     * @param name The new name of this VoiceCommand
     */
    void setName(String name);

    /**
     * @return The JSGF-Rule this Command is bound to
     */
    String getRuleContent();

    /**
     * Set the rule of this VoiceCommand.
     *
     * @param ruleContent New JSGF-Rule for this VoiceCommand
     */
    void setRuleContent(String ruleContent);

    /**
     * @return A list of this commands params used for serialization
     */
    List<String> getConfigParams();

    /**
     * Initialize this command from deserialized parameters
     *
     * @param params The parameters that were once got with {@link IVoiceCommand#getConfigParams()}
     */
    void fromConfigParams(String[] params);

    /**
     * Get a regex representation of the JSGF-rule
     *
     * @param commands The list of all active VoiceCommands
     * @return Regular Expression
     */
    default String getCleanedRuleContent(List<IVoiceCommand> commands) {
        String clean = getRuleContent()
                .replaceAll(" \\[", " ?(")
                .replaceAll("\\[", "(")
                .replaceAll("\\)\\*", " ?)*")
                .replaceAll("\\)\\+", " ?)+")
                .replaceAll("]\\* ?", " ?)*")
                .replaceAll("]\\+ ?", " ?)+")
                .replaceAll("] ", ")? ?")
                .replaceAll("]", ")? ?");
        Pattern pattern = Pattern.compile(".*?<(.*?)>.*?");
        while (true) {
            Matcher matcher = pattern.matcher(clean);
            if (!matcher.matches())
                break;
            String group = matcher.group(1);
            for (IVoiceCommand command : commands) {
                if (command.getName().equals(group)) {
                    clean = clean.replaceAll("<" + group + ">", "(" + command.getCleanedRuleContent(commands) + ")");
                    break;
                }
            }
        }
        //clean = clean.replaceAll(" ", " ?");
        //System.out.println(getName() + ": " + clean);
        return clean;
    }
}
