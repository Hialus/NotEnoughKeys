package de.morrien.nekeys.voice.command.thaumcraft;

import de.morrien.nekeys.voice.command.AbstractVoiceCommand;
import net.minecraft.client.Minecraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;

import java.util.List;

/**
 * Created by Timor Morrien
 */
public class SelectFocusVoiceCommand extends AbstractVoiceCommand {

    protected String fociHash = "REMOVE";

    public SelectFocusVoiceCommand() {
        super();
    }

    public SelectFocusVoiceCommand(String name, String command, String fociHash) {
        super(name, command);
        this.fociHash = fociHash;
    }

    @Override
    public void activate(String voiceCommand) {
        if (Minecraft.getMinecraft().player != null)
            PacketHandler.INSTANCE.sendToServer(new PacketFocusChangeToServer(fociHash));
    }

    @Override
    public List<String> getConfigParams() {
        List<String> list = super.getConfigParams();
        list.add(fociHash);
        return list;
    }

    @Override
    public void fromConfigParams(String[] params) {
        super.fromConfigParams(params);
        this.fociHash = params[3];
    }

    public String getFociHash() {
        return fociHash;
    }

    public void setFociHash(String fociHash) {
        this.fociHash = fociHash;
    }
}
