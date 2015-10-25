package rs.macro.api.access;

import rs.macro.RSMacro;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * @author Tyler Sedlar
 * @since 4/30/15
 */
public class Environment {

    public static final int INPUT_MOUSE = 0x02, INPUT_KEYBOARD = 0x04,
            INPUT_BOTH = (INPUT_MOUSE | INPUT_KEYBOARD);

    private static int input = INPUT_BOTH;

    /**
     * Sets the environment's input flags.
     *
     * @param input The flag(s) to be set.
     */
    public static void setInput(int input) {
        Environment.input = input;
        RSMacro.instance().toolbar().mouseInput.setSelected(
                (Environment.input & INPUT_MOUSE) == INPUT_MOUSE
        );
        RSMacro.instance().toolbar().keyboardInput.setSelected(
                (Environment.input & INPUT_KEYBOARD) == INPUT_KEYBOARD
        );
    }

    /**
     * @return The environment's input flag.
     */
    public static int input() {
        return input;
    }

    /**
     * Copies text to the system clipboard.
     *
     * @param content The text to be copied.
     */
    public static void copyToClipboard(String content) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(content);
        clipboard.setContents(selection, null);
    }
}