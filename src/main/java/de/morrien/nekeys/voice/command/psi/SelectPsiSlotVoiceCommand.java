package de.morrien.nekeys.voice.command.psi;

import de.morrien.nekeys.NotEnoughKeys;
import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.AbstractVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.voice.popup.psi.SelectPsiSlotPopup;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.network.NetworkMessage;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.message.MessageChangeSocketableSlot;

import java.util.List;

public class SelectPsiSlotVoiceCommand extends AbstractVoiceCommand {

    protected int slot;

    public SelectPsiSlotVoiceCommand() {
        super();
    }

    public SelectPsiSlotVoiceCommand(String name, String command, int slot) {
        super(name, command);
        this.slot = slot;
    }

    @Override
    public void activate(String voiceCommand) {
        ItemStack itemStack = PsiAPI.getPlayerCAD(Minecraft.getMinecraft().player);
        Item item = itemStack.getItem();
        if (item instanceof ISocketable) {
            ISocketable socketable = (ISocketable) item;
            if (socketable.showSlotInRadialMenu(itemStack, slot)) {
                PlayerDataHandler.get(Minecraft.getMinecraft().player).stopLoopcast();

                NetworkMessage message = new MessageChangeSocketableSlot(slot);
                NetworkHandler.INSTANCE.sendToServer(message);
            } else {
                NotEnoughKeys.instance.sendLocalizedStatusMessage("nekeys.status.psi.invalidSlot");
            }
        }
    }

    @Override
    public List<String> getConfigParams() {
        List<String> list = super.getConfigParams();
        list.add(String.valueOf(slot));
        return list;
    }

    @Override
    public void fromConfigParams(String[] params) {
        super.fromConfigParams(params);
        this.slot = Integer.parseInt(params[3]);
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public static class Factory extends VoiceCommandFactory<SelectPsiSlotVoiceCommand> {

        @Override
        public SelectPsiSlotVoiceCommand newCommand(String[] params) {
            SelectPsiSlotVoiceCommand voiceCommand = new SelectPsiSlotVoiceCommand();
            voiceCommand.fromConfigParams(params);
            return voiceCommand;
        }

        @Override
        public SelectPsiSlotVoiceCommand newCommand(String name, String rule) {
            return new SelectPsiSlotVoiceCommand(name, rule, 0);
        }

        @Override
        public AbstractPopup newPopup(SelectPsiSlotVoiceCommand command) {
            return new SelectPsiSlotPopup(command);
        }
    }
}
