//package de.morrien.nekeys.command;
//
//import com.mojang.brigadier.CommandDispatcher;
//import de.morrien.nekeys.gui.voice.GuiVoiceCommand;
//import net.minecraft.client.Minecraft;
//import net.minecraft.command.CommandSource;
//import net.minecraft.command.Commands;
//
//public class CommandOpenVoiceCommandSettings { // TODO
//
//    public static void register(CommandDispatcher<CommandSource> dispatcher) {
//        dispatcher.register(Commands.literal("voice")
//                .requires(source -> source.hasPermission(0))
//                .executes(context -> {
//                    Minecraft.getInstance().setScreen(new GuiVoiceCommand());
//                    return 1;
//                })
//        );
//    }
//}
