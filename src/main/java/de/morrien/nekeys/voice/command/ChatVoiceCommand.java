package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.AbstractVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.voice.popup.ChatPopup;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

/**
 * Created by Timor Morrien
 */
public class ChatVoiceCommand extends AbstractVoiceCommand {

    protected String chatMessage;

    protected ChatVoiceCommand() {
    }

    public ChatVoiceCommand(String name, String command, String chatMessage) {
        super(name, command);
        this.chatMessage = chatMessage;
    }

    @Override
    public void activate(String voiceCommand) {
        if (chatMessage != null && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().gui.getChat().addRecentChat(chatMessage);
            Minecraft.getInstance().player.chat(chatMessage);
        }
    }

    @Override
    public List<String> getConfigParams() {
        List<String> params = super.getConfigParams();
        params.add(chatMessage);
        return params;
    }

    @Override
    public void fromConfigParams(String[] params) {
        super.fromConfigParams(params);
        this.chatMessage = params.length >= 4 ? params[3] : "";
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public static class Factory extends VoiceCommandFactory<ChatVoiceCommand> {

        @Override
        public ChatVoiceCommand newCommand(String[] params) {
            ChatVoiceCommand chatVoiceCommand = new ChatVoiceCommand();
            chatVoiceCommand.fromConfigParams(params);
            return chatVoiceCommand;
        }

        @Override
        public ChatVoiceCommand newCommand(String name, String rule) {
            return new ChatVoiceCommand(name, rule, "");
        }

        @Override
        public AbstractPopup newPopup(ChatVoiceCommand command) {
            return new ChatPopup(command);
        }
    }
}
