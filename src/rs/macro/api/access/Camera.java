package rs.macro.api.access;

import rs.macro.api.access.input.Keyboard;
import rs.macro.api.util.Time;

import java.awt.event.KeyEvent;

/**
 * @author Jacob Doiron
 * @since 5/17/15
 */
public class Camera {

    /**
     * Sets the camera to the specified angle.
     *
     * @param angle The angle to be set.
     * @return <t>true</t> if the camera was set to the specified angle; otherwise, <t>false</t>.
     */
    public static boolean setAngle(int angle) {
        if (RuneScape.playing()) {
            if (Math.abs(Minimap.angle() - angle) <= 5) {
                return true;
            }
            Keyboard.pressKey((char) KeyEvent.VK_LEFT);
            while (Math.abs(Minimap.angle() - angle) > 5) {
                Time.sleep(20, 40);
            }
            Keyboard.releaseKey((char) KeyEvent.VK_LEFT);
            return Math.abs(Minimap.angle() - angle) <= 5;
        }
        return false;
    }

    /**
     * Sets the camera's pitch.
     *
     * @param up <t>true</t> to set the camera's pitch up; otherwise, <t>false</t>.
     */
    public static void setPitch(boolean up) {
        if (RuneScape.playing()) {
            char key = up ? (char) KeyEvent.VK_UP : (char) KeyEvent.VK_DOWN;
            Keyboard.pressKey(key);
            Time.sleep(1200, 1500);
            Keyboard.releaseKey(key);
        }
    }
}