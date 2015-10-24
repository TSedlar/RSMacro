package rs.macro;

import rs.macro.api.Macro;
import rs.macro.api.methods.Environment;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.Time;
import rs.macro.internal.ext.EventDispatcher;
import rs.macro.internal.random.RandomEvent;
import rs.macro.internal.random.RandomHandler;
import rs.macro.internal.random.event.BankPin;
import rs.macro.internal.ui.MacroSelector;
import rs.macro.internal.ui.Toolbar;
import rs.macro.util.Configuration;
import rs.macro.util.OperatingSystem;
import rs.macro.util.io.Crawler;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class RSMacro extends JFrame implements Runnable {

    private static RSMacro instance;

    private final Toolbar toolbar;
    private final Crawler crawler;
    private final MacroSelector selector;

    private EventDispatcher dispatcher;
    private RandomHandler randoms;

    private boolean isRunningMacro;

    public static RSMacro instance() {
        return instance;
    }

    public RSMacro() {
        super("RSMacro");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBackground(Color.BLACK);
        setResizable(false);
        toolbar = new Toolbar();
        selector = new MacroSelector();
        setJMenuBar(toolbar);
        crawler = new Crawler(Crawler.GameType.OSRS);
        new Thread(this).start();
    }

    public MacroSelector selector() {
        return selector;
    }

    public EventDispatcher dispatcher() {
        return dispatcher;
    }

    public void setRunningMacro(boolean isRunningMacro) {
        this.isRunningMacro = isRunningMacro;
        toolbar.macro.setText((isRunningMacro ? "Stop" : "Start") + " slave");
        toolbar.macro.setState(isRunningMacro);
        Environment.setInput(isRunningMacro ? Environment.INPUT_KEYBOARD :
                (Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE));
        if (randoms == null) {
            randoms = new RandomHandler();
            randoms.submit(new BankPin());
        }
        if (isRunningMacro) {
            randoms.start();
        } else {
            randoms.stop();
        }
    }

    public boolean isRunningMacro() {
        return isRunningMacro;
    }

    public Toolbar toolbar() {
        return toolbar;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        Configuration.setup();
        crawler.crawl();
        if (crawler.outdated()) {
            crawler.download(() -> System.out.println("Downloaded: " +
                    crawler.percent + "%"));
        }
        URLClassLoader classloader;
        try {
            URL packURL = new File(crawler.pack).toURI().toURL();
            classloader = new URLClassLoader(new URL[]{packURL});
        } catch (Exception ignored) {
            throw new RuntimeException("Corrupt classloader");
        }
        Applet applet = crawler.start(classloader);
        add(applet);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        setVisible(true);
        GameCanvas.render = (g) -> {
            boolean random = false;
            if (randoms != null) {
                for (RandomEvent event : randoms.events()) {
                    if (event.solving()) {
                        random = true;
                        ((Renderable) event).render(g);
                        randoms.render(g);
                    }
                }
            }
            if (!random) {
                Macro macro = MacroSelector.current();
                if (macro != null && macro instanceof Renderable) {
                    ((Renderable) macro).render(g);
                }
            }
        };
        while (GameCanvas.instance == null) {
            Time.sleep(250, 500);
        }
        while (GameCanvas.instance.getMouseListeners().length == 0) {
            Time.sleep(250, 500);
        }
        while (!isVisible()) {
            Time.sleep(250, 500);
        }
        dispatcher = new EventDispatcher(GameCanvas.instance);
    }

    public static void main(String... args) throws Exception {
        if (OperatingSystem.get() == OperatingSystem.WINDOWS) {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } else if (OperatingSystem.get() == OperatingSystem.MAC) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        instance = new RSMacro();
    }
}
