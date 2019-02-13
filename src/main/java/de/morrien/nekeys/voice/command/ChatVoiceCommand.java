package de.morrien.nekeys.voice.command;

import net.minecraft.client.Minecraft;

import java.util.List;

/**
 * Created by Timor Morrien
 */
public class ChatVoiceCommand extends AbstractVoiceCommand {

    protected String chatMessage;

    protected ChatVoiceCommand() { }

    public ChatVoiceCommand(String name, String command, String chatMessage) {
        super(name, command);
        this.chatMessage = chatMessage;
    }

    @Override
    public void activate(String voiceCommand) {
        if (chatMessage != null && Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.sendChatMessage(chatMessage);
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
        this.chatMessage = params[3];
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
}
