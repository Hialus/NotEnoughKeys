package de.morrien.nekeys.voice.command.psi;

import de.morrien.nekeys.NotEnoughKeys;
import de.morrien.nekeys.api.VoiceCommandFactory;
import de.morrien.nekeys.api.command.AbstractVoiceCommand;
import de.morrien.nekeys.api.popup.AbstractPopup;
import de.morrien.nekeys.gui.voice.popup.psi.SelectPsiSlotPopup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.network.MessageRegister;
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
        ItemStack itemStack = PsiAPI.getPlayerCAD(Minecraft.getInstance().player);
        final LazyOptional<ISocketable> socketableOptional = itemStack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY);
        if (socketableOptional.isPresent()) {
            ISocketable socketable = socketableOptional.resolve().get();
            if (socketable.isSocketSlotAvailable(slot)) {
                PlayerDataHandler.get(Minecraft.getInstance().player).stopLoopcast();

                MessageChangeSocketableSlot message = new MessageChangeSocketableSlot(slot);
                MessageRegister.HANDLER.sendToServer(message);
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
