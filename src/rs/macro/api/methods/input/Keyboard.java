package rs.macro.api.methods.input;

import rs.macro.RSMacro;

import java.awt.event.KeyEvent;

/**
 * @author Tyler Sedlar
 * @since 5/16/2015
 */
public class Keyboard {

    public static void pressKey(char key) {
        RSMacro.instance().dispatcher().pressKey(key);
    }

    public static void releaseKey(char key) {
        RSMacro.instance().dispatcher().releaseKey(key);
    }

    public static void typeKey(char key) {
        RSMacro.instance().dispatcher().typeKey(key);
    }

    public static void type(String string) {
        RSMacro.instance().dispatcher().type(string);
    }

    public static void send(String string) {
        type(string);
        typeKey((char) KeyEvent.VK_ENTER);
    }
}