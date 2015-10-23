package rs.macro.internal.ui;

import rs.macro.RSMacro;
import rs.macro.api.methods.Environment;

import javax.swing.*;

/**
 * @author Tyler Sedlar
 * @since 4/30/15
 */
public class Toolbar extends JMenuBar {

    public final JCheckBoxMenuItem macro = new JCheckBoxMenuItem("Run macro");
    public final JMenuItem exit = new JMenuItem("Exit");
    public final JCheckBoxMenuItem mouseInput = new JCheckBoxMenuItem("Mouse");
    public final JCheckBoxMenuItem keyboardInput = new JCheckBoxMenuItem("Keyboard");

    public Toolbar() {
        JMenu file = new JMenu("File");
        macro.addActionListener(evt -> {
            if (!RSMacro.instance().isRunningMacro()) {
                RSMacro.instance().selector().loadSlaves();
                RSMacro.instance().selector().setLocationRelativeTo(RSMacro.instance());
                RSMacro.instance().selector().setVisible(true);
            } else {
                MacroSelector.unsetSlave();
            }
        });
        file.add(macro);
        exit.addActionListener(evt -> System.exit(0));
        file.add(exit);
        add(file);
        JMenu input = new JMenu("Input");
        mouseInput.setSelected(true);
        mouseInput.addActionListener(evt -> {
            int in = Environment.input();
            Environment.setInput(mouseInput.isSelected() ? (in | Environment.INPUT_MOUSE) :
                    (in & ~Environment.INPUT_MOUSE));
        });
        input.add(mouseInput);
        keyboardInput.setSelected(true);
        keyboardInput.addActionListener(evt -> {
            int in = Environment.input();
            Environment.setInput(keyboardInput.isSelected() ? (in | Environment.INPUT_KEYBOARD) :
                    (in & ~Environment.INPUT_KEYBOARD));
        });
        input.add(keyboardInput);
        add(input);
    }
}