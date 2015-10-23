package rs.macro.internal.ui;

import rs.macro.RSMacro;
import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.methods.RuneScape;
import rs.macro.internal.mdn.LocalMacroLoader;
import rs.macro.internal.mdn.MacroDefinition;
import rs.macro.util.Configuration;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * @author Tyler Sedlar
 * @since 4/30/2015
 */
public class MacroSelector extends JDialog {

    private final MacroDataSelector macroDataSelector = new MacroDataSelector();

    private final DefaultTableModel model;
    private final JTable table;

    private SlaveVector selected;

    private static Macro current;

    public final MacroDataSelector dataSelector() {
        return macroDataSelector;
    }

    public static Macro current() {
        return current;
    }

    public static void unsetSlave() {
        if (current != null) {
            current.interrupt();
            if (current != null) {
                current.atEnd();
            }
            RuneScape.pixels().stop();
            RuneScape.pixels().setImage(null);
        }
        current = null;
        RSMacro.instance().setRunningMacro(false);
    }

    public MacroSelector() {
        setTitle("Macro Selector");
        setResizable(false);
        setModal(true);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                RSMacro.instance().setRunningMacro(false);
            }
        });
        JPanel container = new JPanel(new BorderLayout());
        container.setPreferredSize(new Dimension(500, 300));
        model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Author");
        model.addColumn("Name");
        model.addColumn("Description");
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(noFocusBorder);
                return this;
            }

        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.setRowSelectionAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        TableColumnModel columns = table.getColumnModel();
        columns.getColumn(0).setPreferredWidth(100);
        columns.getColumn(1).setPreferredWidth(100);
        columns.getColumn(2).setPreferredWidth(300);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }
            selected = (SlaveVector) model.getDataVector().get(row);
        });
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(500, 270));
        container.add(scrollpane, BorderLayout.NORTH);
        JButton start = new JButton("Start");
        start.setPreferredSize(new Dimension(500, 30));
        start.addActionListener(e -> new Thread(() -> {
            if (selected != null) {
                boolean success = true;
                try {
                    Manifest manifest = selected.def.manifest();
                    if (manifest.banks()) {
                        dispose();
                        macroDataSelector.setLocationRelativeTo(RSMacro.instance());
                        if (macroDataSelector.choose() == null) {
                            success = false;
                        }
                    }
                    if (success) {
                        System.out.println("Started " + manifest.name() +
                                " by " + manifest.author());
                        current = selected.def.mainClass().newInstance();
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                    success = false;
                }
                if (success) {
                    RSMacro.instance().setRunningMacro(true);
                    RuneScape.pixels().start();
                    RuneScape.pixels().setImage(GameCanvas.raw);
                    current().atStart();
                    current().start();
                } else {
                    RSMacro.instance().setRunningMacro(false);
                    RuneScape.pixels().stop();
                    RuneScape.pixels().setImage(null);
                    unsetSlave();
                }
                dispose();
            }
        }).start());
        container.add(start, BorderLayout.SOUTH);
        add(container);
        pack();
    }

    public void loadSlaves() {
        model.getDataVector().clear();
        model.fireTableDataChanged();
        LocalMacroLoader loader = new LocalMacroLoader();
        try {
            loader.parse(new File(Configuration.MACROS));
            MacroDefinition[] definitions = loader.definitions();
            for (MacroDefinition def : definitions) {
                addSlave(def);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        model.fireTableDataChanged();
    }

    private class SlaveVector extends Vector<String> {

        public final MacroDefinition def;

        public SlaveVector(MacroDefinition def, String... strings) {
            this.def = def;
            for (String string : strings) {
                add(string);
            }
        }
    }

    public void addSlave(MacroDefinition def) {
        Manifest manifest = def.manifest();
        model.addRow(new SlaveVector(def, manifest.author(), manifest.name(),
                manifest.description()));
    }
}