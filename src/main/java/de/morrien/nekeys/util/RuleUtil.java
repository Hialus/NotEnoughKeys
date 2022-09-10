package de.morrien.nekeys.util;

import de.morrien.nekeys.NotEnoughKeys;
import de.morrien.nekeys.api.command.IVoiceCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleUtil {
    public static boolean isRuleValid(String ruleName, String rule, List<IVoiceCommand> currentCommands) {
        rule = rule.toLowerCase();
        if (!areWordsInDictionary(rule)) return false;
        if (!areBracesValid(rule)) return false;
        if (!areReferencesValid(ruleName, rule, currentCommands)) return false;

        return true;
    }

    private static boolean areReferencesValid(String ruleName, String rule, List<IVoiceCommand> currentCommands) {
        var matcher = Pattern.compile("<(?<reference>[^<>]*)>").matcher(rule);
        while(matcher.find()) {
            String reference = matcher.group("reference");
            if (reference.equalsIgnoreCase(ruleName)) return false;
            if (currentCommands.stream().noneMatch(c -> c.getName().equalsIgnoreCase(reference))) return false;
        }

        return true;
    }

    private static boolean areWordsInDictionary(String rule) {
        String reducedText = rule
                .replaceAll("[\\[\\]()|+*]", " ")
                .replaceAll("<[^<>]*>", " ");
        String[] words = reducedText.split(" ");
        for (String word : words) {
            if (word.matches(" *")) continue;
            if (!NotEnoughKeys.instance.voiceHandler.getRecognizer().isWord(word)) {
                return false;
            }
        }
        return true;
    }

    private static boolean areBracesValid(String rule) {
        Map<String, Integer> map = new HashMap<>();
        map.put("[]", 0);
        map.put("()", 0);
        map.put("<>", 0);

        char[] charArray = rule.toCharArray();
        for (char c : charArray) {
            switch (c) {
                case '[':
                    map.put("[]", map.get("[]") + 1);
                    break;
                case ']':
                    map.put("[]", map.get("[]") - 1);
                    if (map.get("[]") < 0)
                        return false;
                    break;
                case '(':
                    map.put("()", map.get("()") + 1);
                    break;
                case ')':
                    map.put("()", map.get("()") - 1);
                    if (map.get("()") < 0)
                        return false;
                    break;
                case '<':
                    map.put("<>", map.get("<>") + 1);
                    break;
                case '>':
                    map.put("<>", map.get("<>") - 1);
                    if (map.get("<>") < 0)
                        return false;
                    break;
                default:
                    break;
            }
        }

        return map.get("[]") == 0 && map.get("()") == 0 && map.get("<>") == 0;
    }
}
