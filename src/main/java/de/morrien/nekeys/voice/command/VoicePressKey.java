package de.morrien.nekeys.voice.command;

import de.morrien.nekeys.NotEnoughKeys;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Created by Timor Morrien
 */
public class VoicePressKey extends AbstractVoiceCommand implements IVoiceCommandTickable {

    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            NotEnoughKeys.logger.warn(e.toString());
        }
    }

    private int ticksLeft;
    private int keyCode;

    private VoicePressKey() {}

    public VoicePressKey(String name, String command, int keyCode) {
        super(name, command);
        this.keyCode = keyCode;
    }

    @Override
    public void activate(String voiceCommand) {
        if (keyCode == 0) return;
        robot.keyPress(keyCode);
        ticksLeft = 1;
    }

    @Override
    public void tick() {
        if (ticksLeft >= 0) {
            ticksLeft--;
            if (ticksLeft == 0) {
                robot.keyRelease(keyCode);
            }
        }
    }

    @Override
    public java.util.List<String> getConfigParams() {
        List<String> params = super.getConfigParams();
        params.add(String.valueOf(keyCode));
        return params;
    }

    @Override
    public void fromConfigParams(String[] params) {
        super.fromConfigParams(params);
        this.keyCode = Integer.parseInt(params[3]);
    }

    public int getKeyCode() {
        return keyCode;
    }
}
