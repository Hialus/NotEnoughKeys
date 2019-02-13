package de.morrien.nekeys.command;

import de.morrien.nekeys.gui.voice.GuiVoiceCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

public class CommandOpenVoiceCommandSettings extends CommandBase implements IClientCommand {

    @Override
    public String getName() {
        return "voice";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/voice";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiVoiceCommand());
    }

    @Override
    public boolean allowUsageWithoutPrefix(ICommandSender sender, String message) {
        return false;
    }
}
