package de.morrien.nekeys.voice.command.thaumcraft;

//import de.morrien.nekeys.api.VoiceCommandFactory;
//import de.morrien.nekeys.api.command.AbstractVoiceCommand;
//import de.morrien.nekeys.api.popup.AbstractPopup;
//import de.morrien.nekeys.gui.voice.popup.thaumcraft.SelectFocusPopup;
//import net.minecraft.client.Minecraft;
//import thaumcraft.common.lib.network.PacketHandler;
//import thaumcraft.common.lib.network.misc.PacketFocusChangeToServer;
//
//import java.util.List;
//
///**
// * Created by Timor Morrien
// */
//public class SelectFocusVoiceCommand extends AbstractVoiceCommand {
//
//    protected String fociHash = "REMOVE";
//
//    public SelectFocusVoiceCommand() {
//        super();
//    }
//
//    public SelectFocusVoiceCommand(String name, String command, String fociHash) {
//        super(name, command);
//        this.fociHash = fociHash;
//    }
//
//    @Override
//    public void activate(String voiceCommand) {
//        if (Minecraft.getInstance().player != null)
//            PacketHandler.INSTANCE.sendToServer(new PacketFocusChangeToServer(fociHash));
//    }
//
//    @Override
//    public List<String> getConfigParams() {
//        List<String> list = super.getConfigParams();
//        list.add(fociHash);
//        return list;
//    }
//
//    @Override
//    public void fromConfigParams(String[] params) {
//        super.fromConfigParams(params);
//        this.fociHash = params[3];
//    }
//
//    public String getFociHash() {
//        return fociHash;
//    }
//
//    public void setFociHash(String fociHash) {
//        this.fociHash = fociHash;
//    }
//
//    public static class Factory extends VoiceCommandFactory<SelectFocusVoiceCommand> {
//
//        @Override
//        public SelectFocusVoiceCommand newCommand(String[] params) {
//            SelectFocusVoiceCommand voiceCommand = new SelectFocusVoiceCommand();
//            voiceCommand.fromConfigParams(params);
//            return voiceCommand;
//        }
//
//        @Override
//        public SelectFocusVoiceCommand newCommand(String name, String rule) {
//            return new SelectFocusVoiceCommand(name, rule, "REMOVE");
//        }
//
//        @Override
//        public AbstractPopup newPopup(SelectFocusVoiceCommand command) {
//            return new SelectFocusPopup(command);
//        }
//    }
//}
