package rs.macro.util.io;

import rs.macro.internal.CacheData;
import rs.macro.util.Configuration;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarInputStream;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class Crawler {

    public final Map<String, String> parameters = new HashMap<>();
    public final GameType type;
    public final String pack;
    private final String home, config;
    public int percent;

    private int hash = -1;

    /**
     * Creates a Crawler for the given type.
     *
     * @param type The GameType to create this Crawler for.
     */
    public Crawler(GameType type) {
        this.type = type;
        pack = Configuration.CACHE + (type == GameType.OSRS ? "os" : "rs3") +
                "_pack.jar";
        CacheData.parseWorld();
        home = String.format(type.format, CacheData.world());
        config = home + "jav_config.ws";
    }

    /**
     * Creates the Applet from the given ClassLoader.
     *
     * @param classloader The ClassLoader of the downloaded game.
     * @return The Applet from the given ClassLoader.
     */
    public Applet start(ClassLoader classloader) {
        try {
            String main = parameters.get("initial_class").replace(".class", "");
            Applet applet = (Applet) classloader.loadClass(main).newInstance();
            applet.setBackground(Color.BLACK);
            applet.setPreferredSize(appletSize());
            applet.setLayout(null);
            applet.setStub(stub(applet));
            applet.init();
            applet.start();
            applet.setVisible(true);
            return applet;
        } catch (InstantiationException | IllegalAccessException |
                ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates the AppletStub for the given Applet.
     *
     * @param applet The game Applet.
     * @return The AppletStub for the given Applet.
     */
    public AppletStub stub(Applet applet) {
        return new AppletStub() {

            @Override
            public boolean isActive() {
                return true;
            }

            @Override
            public URL getDocumentBase() {
                try {
                    return new URL(parameters.get("codebase"));
                } catch (MalformedURLException e) {
                    return null;
                }
            }

            @Override
            public URL getCodeBase() {
                try {
                    return new URL(parameters.get("codebase"));
                } catch (MalformedURLException ignored) {
                    return null;
                }
            }

            @Override
            public String getParameter(String name) {
                return parameters.get(name);
            }

            @Override
            public void appletResize(int width, int height) {
                Dimension size = new Dimension(width, height);
                applet.setSize(size);
            }

            @Override
            public AppletContext getAppletContext() {
                return null;
            }
        };
    }

    /**
     * Gets the hash of the local game jar.
     *
     * @return The hash of the local game jar.
     */
    private int localHash() {
        try {
            URL url = new File(pack).toURI().toURL();
            try (JarInputStream stream = new JarInputStream(url.openStream())) {
                return stream.getManifest().hashCode();
            } catch (Exception ignored) {
                return -1;
            }
        } catch (MalformedURLException ignored) {
            return -1;
        }
    }

    /**
     * Gets the current game hash.
     *
     * @return The current game hash.
     */
    public int hash() {
        return hash;
    }

    /**
     * Gets the game hash on the server.
     *
     * @return The game hash on the server.
     */
    public int remoteHash() {
        try {
            URL url = new URL(home + parameters.get("initial_jar"));
            try (JarInputStream stream = new JarInputStream(url.openStream())) {
                return stream.getManifest().hashCode();
            } catch (Exception ignored) {
                return -1;
            }
        } catch (IOException ignored) {
            return -1;
        }
    }

    /**
     * Sets the hash object to the local jar hash.
     */
    public void initHash() {
        hash = localHash();
    }

    /**
     * Checks whether the local jar is outdated or not.
     *
     * @return <t>true</t> if the local hash doesn't match the remote hash, otherwise <t>false</t>.
     */
    public boolean outdated() {
        File gamepack = new File(pack);
        if (!gamepack.exists()) {
            return true;
        }
        if (hash == -1) {
            hash = localHash();
        }
        boolean outdated = hash == -1 || hash != remoteHash();
        if (!outdated) {
            percent = 100;
        }
        return outdated;
    }

    /**
     * Reads the server configuration.
     *
     * @return <t>true</t> if the server configuration was read, otherwise <t>false</t>.
     */
    public boolean crawl() {
        try {
            List<String> source = Internet.read(config);
            for (String line : source) {
                if (line.startsWith("param=")) {
                    line = line.substring(6);
                }
                int idx = line.indexOf("=");
                if (idx == -1) {
                    continue;
                }
                parameters.put(line.substring(0, idx), line.substring(idx + 1));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Downloads the game jar to the given target.
     *
     * @param target   The path to download to.
     * @param callback The callback to be run upon download percentage change.
     * @return <t>true</t> if the game was downloaded, otherwise <t>false</t>.
     */
    public boolean download(String target, Runnable callback) {
        hash = remoteHash();
        return Internet.download(home + parameters.get("initial_jar"),
                target, new InternetCallback() {
                    public void onDownload(int p) {
                        percent = p;
                        if (callback != null) {
                            callback.run();
                        }
                    }
                }) != null;
    }

    /**
     * Downloads the game jar to the given target.
     *
     * @param target The path to download to.
     * @return <t>true</t> if the game was downloaded, otherwise <t>false</t>.
     */
    public boolean download(String target) {
        return download(target, null);
    }

    /**
     * Downloads the game jar.
     */
    public boolean download() {
        return download(pack);
    }

    /**
     * Downloads the game jar.
     *
     * @param callback The callback to be run upon download percentage change.
     * @return <t>true</t> if the game was downloaded, otherwise <t>false</t>.
     */
    public boolean download(Runnable callback) {
        return download(pack, callback);
    }

    /**
     * Gets the default Applet dimensions based on its parameters.
     *
     * @return The default Applet dimensions based on its parameters.
     */
    public Dimension appletSize() {
        try {
            return new Dimension(
                    Integer.parseInt(parameters.get("applet_minwidth")),
                    Integer.parseInt(parameters.get("applet_minheight")));
        } catch (NumberFormatException e) {
            return GameCanvas.GAME_SIZE;
        }
    }

    /**
     * The types of games this Crawler can load.
     */
    public enum GameType {
        OSRS("http://oldschool%d.runescape.com/"),
        RS3("http://world%d.runescape.com/");

        public final String format;

        GameType(String format) {
            this.format = format;
        }
    }
}