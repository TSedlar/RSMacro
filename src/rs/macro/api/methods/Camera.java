package rs.macro.api.methods;

import rs.macro.api.methods.input.Keyboard;
import rs.macro.api.util.Time;

import java.awt.event.KeyEvent;

/**
 * @author Jacob Doiron
 * @since 5/17/15
 */
public class Camera {

    public static boolean setAngle(int angle) {
        if (Math.abs(Minimap.angle() - angle) <= 5) {
            return true;
        }
        if (RuneScape.playing()) {
            while (Math.abs(Minimap.angle() - angle) > 5) {
                Keyboard.pressKey((char) KeyEvent.VK_LEFT);
                Time.sleep(50, 100);
                Keyboard.releaseKey((char) KeyEvent.VK_LEFT);
            }
            return Math.abs(Minimap.angle() - angle) <= 5;
        }
        return false;
    }

    public static void setPitch(boolean up) {
        if (RuneScape.playing()) {
            char key = up ? (char) KeyEvent.VK_UP : (char) KeyEvent.VK_DOWN;
            Keyboard.pressKey(key);
            Time.sleep(1200, 1500);
            Keyboard.releaseKey(key);
        }
    }
}