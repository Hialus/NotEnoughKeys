package de.morrien.nekeys.voice.command;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Timor Morrien
 */
public interface IVoiceCommand {

    boolean isValidCommand(String voiceCommand);

    void activate(String voiceCommand);

    String getName();

    void setName(String name);

    String getRuleContent();

    default String getCleanedRuleContent(List<IVoiceCommand> commands) {
        String clean = getRuleContent()
                .replaceAll("\\[", "(")
                .replaceAll("\\)\\*", " )*")
                .replaceAll("\\)\\+", " )+")
                .replaceAll("\\)\\*", " )*")
                .replaceAll("]\\* ?", " )*")
                .replaceAll("]\\+ ?", " )+")
                .replaceAll("] ", " )?")
                .replaceAll("]", ")?");
        Pattern pattern = Pattern.compile(".*?<(.*?)>.*?");
        while (true) {
            Matcher matcher = pattern.matcher(clean);
            if (!matcher.matches())
                break;
            String group = matcher.group(1);
            for (IVoiceCommand command : commands) {
                if (command.getName().equals(group)) {
                    clean = matcher.replaceFirst("(" + command.getCleanedRuleContent(commands) + ")");
                    break;
                }
            }
        }
        return clean;
    }

    void setRuleContent(String ruleContent);

    List<String> getConfigParams();

    void fromConfigParams(String[] params);
}
