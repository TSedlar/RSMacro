package rs.macro.api.access.input;

import rs.macro.RSMacro;

import java.awt.event.KeyEvent;

/**
 * @author Tyler Sedlar
 * @since 5/16/2015
 */
public class Keyboard {

    /**
     * Presses the specified key.
     *
     * @param key The key to be pressed.
     */
    public static void pressKey(char key) {
        RSMacro.instance().dispatcher().pressKey(key);
    }

    /**
     * Releases the specified key.
     *
     * @param key The key to be released.
     */
    public static void releaseKey(char key) {
        RSMacro.instance().dispatcher().releaseKey(key);
    }

    /**
     * Types the specified key.
     *
     * @param key The key to be typed.
     */
    public static void typeKey(char key) {
        RSMacro.instance().dispatcher().typeKey(key);
    }

    /**
     * Types the specified String.
     *
     * @param string The String to be typed.
     */
    public static void type(String string) {
        RSMacro.instance().dispatcher().type(string);
    }

    /**
     * Sends the specified String.
     *
     * @param string The String to be sent.
     */
    public static void send(String string) {
        type(string);
        typeKey((char) KeyEvent.VK_ENTER);
    }
}