package rs.macro.api.methods;

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

    public static void setInput(int input) {
        Environment.input = input;
        RSMacro.instance().toolbar().mouseInput.setSelected(
                (Environment.input & INPUT_MOUSE) == INPUT_MOUSE
        );
        RSMacro.instance().toolbar().keyboardInput.setSelected(
                (Environment.input & INPUT_KEYBOARD) == INPUT_KEYBOARD
        );
    }

    public static int input() {
        return input;
    }

    public static void copyToClipboard(String content) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(content);
        clipboard.setContents(selection, null);
    }
}