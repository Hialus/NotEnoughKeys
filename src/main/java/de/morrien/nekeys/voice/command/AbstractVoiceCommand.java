package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.NotEnoughKeys;
import edu.cmu.sphinx.jsgf.parser.JSGFParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Timor Morrien
 */
public abstract class AbstractVoiceCommand implements IVoiceCommand {

    protected String name;
    protected String command;

    protected AbstractVoiceCommand() {}

    public AbstractVoiceCommand(String name, String command) {
        this.name = name;
        this.command = command;
    }

    @Override
    public boolean isValidCommand(String voiceCommand) {
        try {
            return voiceCommand.matches(getCleanedRuleContent(NotEnoughKeys.instance.voiceHandler.getVoiceCommands()));
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getRuleContent() {
        return command;
    }

    @Override
    public void setRuleContent(String ruleContent) {
        this.command = ruleContent;
    }

    @Override
    public List<String> getConfigParams() {
        List<String> params = new ArrayList<>();
        params.add(name);
        params.add(command);
        return params;
    }

    @Override
    public void fromConfigParams(String[] params) {
        this.name = params.length>=2?params[1]:"";
        this.command = params.length>=3?params[2]:"";
    }
}
