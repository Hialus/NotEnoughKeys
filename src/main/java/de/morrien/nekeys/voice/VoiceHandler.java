package de.morrien.nekeys.voice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mojang.blaze3d.platform.InputConstants;
import de.morrien.nekeys.Keybindings;
import de.morrien.nekeys.NotEnoughKeys;
import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.IVoiceCommand;
import de.morrien.nekeys.api.command.IVoiceCommandTickable;
import de.morrien.nekeys.util.KeyMappingTypeAdapter;
import de.morrien.nekeys.util.VoiceCommandTypeAdapter;
import de.morrien.nekeys.util.RuleUtil;
import de.morrien.nekeys.voice.command.OpenGuiVoiceCommand;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.morrien.nekeys.NotEnoughKeys.logger;

/**
 * Created by Timor Morrien
 */
public class VoiceHandler {

    private final Path oldConfigFile;
    private final Path configFile;
    private final Gson gson;
    private final Type voiceCommandListType;
    public FactoryMap factoryMap;
    public int tickBuffer;
    private SpeechRecognizer recognizer;
    private List<IVoiceCommand> voiceCommands;

    public VoiceHandler() {
        gson = new GsonBuilder()
                .registerTypeAdapter(IVoiceCommand.class, new VoiceCommandTypeAdapter<>())
                .registerTypeHierarchyAdapter(KeyMapping.class, new KeyMappingTypeAdapter())
                .create();
        voiceCommandListType = new TypeToken<List<IVoiceCommand>>() {}.getType();
        voiceCommands = new ArrayList<>();
        factoryMap = new FactoryMap();
        oldConfigFile = NotEnoughKeys.instance.configDirectory.resolve("voice.config");
        configFile = NotEnoughKeys.instance.configDirectory.resolve("voice_commands.json");
    }

    public void init() {
        logger.debug("Initializing recognizer.");
        recognizer = new SpeechRecognizer();

        reloadConfig();
        updateGrammar();
        recognizer.setEnabled(true);
    }

    public void tickUpdate() {
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Keybindings.PUSH_TO_TALK.getKey().getValue())) {
            if (!recognizer.recording) {
                recognizer.recording = true;
                tickBuffer = 20;
            }
        } else {
            if (recognizer.recording) {
                if (tickBuffer <= 0) {
                    recognizer.recording = false;
                } else {
                    tickBuffer--;
                }
            }
        }

        // Let the IVoiceCommandTickables tick
        voiceCommands.stream().filter(iVoiceCommand -> iVoiceCommand instanceof IVoiceCommandTickable).forEach(iVoiceCommand -> ((IVoiceCommandTickable) iVoiceCommand).tick());
        // Check if the user gave any command(s)
        while (recognizer.getQueueSize() > 0) {
            String command = recognizer.popString();
            command = command.toLowerCase();
            logger.info("Command recognized: " + command);
            for (IVoiceCommand voiceKeybind : voiceCommands) {
                if (voiceKeybind.isValidCommand(command)) {
                    voiceKeybind.activate(command);
                }
            }
        }
    }

    public <T extends IVoiceCommand> void bind(Class<T> command, VoiceCommandFactory<T> factory) {
        factoryMap.put(command, factory);
    }

    public void reloadConfig() {
        logger.info("Reloading voice commands config.");
        if (Files.exists(oldConfigFile)) {
            logger.info("Found old voice config file. Converting to new format.");
            try {
                voiceCommands = new ArrayList<>();
                List<String> lines = Files.readAllLines(oldConfigFile);
                for (String line : lines) {
                    IVoiceCommand cmd = parseLineToVoiceCommand(line);
                    if (cmd != null) voiceCommands.add(cmd);
                }
                logger.info("Read " + voiceCommands.size() + " voice commands. Saving...");
                saveConfig();
                logger.info("Voice config file converted. Deleting old file.");
                Files.delete(oldConfigFile);
                logger.info("Old voice config file deleted.");
            } catch (IOException e) {
                logger.error("Could not read old voice.config. Settings were not loaded!", e);
            }
        } else if (Files.exists(configFile)) {
            try {
                var json = String.join("\n", Files.readAllLines(configFile, StandardCharsets.UTF_8));
                voiceCommands = gson.fromJson(json, voiceCommandListType);
                logger.info("Read " + voiceCommands.size() + " voice commands.");
            } catch (IOException e) {
                logger.error("Could not read voice_commands.json. Settings were not loaded!", e);
            }
        } else {
            logger.info("No voice command file found. Creating default command.");
            voiceCommands = new ArrayList<>();
            voiceCommands.add(new OpenGuiVoiceCommand("voice", "[open] ((voice settings)|(voice)|(speech settings))", OpenGuiVoiceCommand.AllowedGuis.VOICE_COMMANDS));
            saveConfig();
        }
    }

    public void saveConfig() {
        try {
            var json = gson.toJson(voiceCommands, voiceCommandListType);
            Files.write(configFile, Collections.singleton(json), StandardCharsets.UTF_8);
        } catch (IOException | JsonParseException e) {
            logger.warn("Unable to save config file", e);
        }
    }

    @Deprecated
    private IVoiceCommand parseLineToVoiceCommand(String line) {
        String[] splitLine = line.split(":");
        if (splitLine.length == 0) return null;
        final IVoiceCommand[] command = new IVoiceCommand[1];
        factoryMap.forEach(((clazz, factory) -> {
            if (clazz.getName().equals(splitLine[0])) {
                command[0] = factory.newCommand(splitLine);
            }
        }));
        return command[0];
    }

    public void updateGrammar() {
        boolean enabled = recognizer.isEnabled();
        if (enabled) recognizer.setEnabled(false);
        recognizer.removeAllRules();
        voiceCommands.forEach((voiceCommand) -> {
            if (RuleUtil.isRuleValid(voiceCommand.getName(), voiceCommand.getRuleContent(), voiceCommands)) {
                recognizer.setRule(voiceCommand.getName().toLowerCase(), voiceCommand.getRuleContent().toLowerCase());
            } else {
                logger.warn("Rule for voice command " + voiceCommand.getName() + " is invalid. Command will not be recognized.");
            }
        });
        if (enabled) recognizer.setEnabled(true);
    }

    public void addVoiceCommand(IVoiceCommand voiceCommand) {
        voiceCommands.add(voiceCommand);
    }

    public void removeVoiceCommand(IVoiceCommand voiceCommand) {
        voiceCommands.remove(voiceCommand);
    }

    public List<IVoiceCommand> getVoiceCommands() {
        return voiceCommands;
    }

    public SpeechRecognizer getRecognizer() {
        return recognizer;
    }
}
