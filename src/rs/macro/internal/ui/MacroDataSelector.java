package rs.macro.internal.ui;

import rs.macro.api.data.BankModel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Tyler Sedlar, Jacob Doiron
 * @since 5/16/15
 */
public class MacroDataSelector extends JDialog {

    private final JComboBox<BankModel> combo;
    private final JComboBox<String> periodBox;
    private final JSpinner hourSpinner = new JSpinner(new SpinnerNumberModel(0,
            0, 12, 1));
    private final JSpinner minuteSpinner = new JSpinner(new SpinnerNumberModel(0,
            0, 59, 1));
    private boolean selected;
    private BankModel chosen;
    private int hour;
    private int minute;
    private String period;

    /**
     * Creates the dialog that has data pertaining to the selected Macro.
     */
    public MacroDataSelector() {
        setTitle("Bank Selector");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel north = new JPanel();
        north.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        north.add(new JLabel("Bank:"));
        combo = new JComboBox<>(BankModel.values());
        combo.setPreferredSize(new Dimension(150, 25));
        north.add(combo);
        JPanel center = new JPanel();
        center.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        center.add(new JLabel("Stop:"));
        hourSpinner.setEditor(new JSpinner.DefaultEditor(hourSpinner));
        hourSpinner.setPreferredSize(new Dimension(50, 25));
        center.add(hourSpinner);
        minuteSpinner.setEditor(new JSpinner.DefaultEditor(minuteSpinner));
        minuteSpinner.setPreferredSize(new Dimension(50, 25));
        center.add(minuteSpinner);
        periodBox = new JComboBox<>(new String[]{"AM", "PM"});
        periodBox.setPreferredSize(new Dimension(50, 25));
        center.add(periodBox);
        JPanel south = new JPanel();
        south.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        JButton select = new JButton("Select");
        select.addActionListener((e) -> {
            selected = true;
            dispose();
        });
        select.setPreferredSize(new Dimension(100, 25));
        south.add(select);
        south.add(Box.createRigidArea(new Dimension(0, 10)));
        add(north);
        add(center);
        add(south);
        pack();
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }

    /**
     * The BankModel of the bank that was chosen.
     *
     * @return The BankModel of the bank that was chosen.
     */
    public BankModel bank() {
        return chosen;
    }

    /**
     * The hour of the day to stop the Macro at.
     *
     * @return The hour of the day to stop the Macro at.
     */
    public int hour() {
        return hour;
    }

    /**
     * The minute of the given hour to stop the Macro at.
     *
     * @return The minute of the given hour to stop the Macro at.
     */
    public int minute() {
        return minute;
    }

    /**
     * The time of day to stop the Macro at. (AM/PM)
     *
     * @return The time of day to stop the Macro at. (AM/PM)
     */
    public String period() {
        return period;
    }

    /**
     * Chooses the BankModel of the selected bank JComboBox.
     *
     * @return Chooses the BankModel of the selected bank JComboBox.
     */
    public BankModel choose() {
        selected = false;
        try {
            EventQueue.invokeAndWait(() -> setVisible(true));
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (selected) {
            chosen = (BankModel) combo.getSelectedItem();
            hour = (int) hourSpinner.getValue();
            minute = (int) minuteSpinner.getValue();
            period = (String) periodBox.getSelectedItem();
        }
        return chosen;
    }
}