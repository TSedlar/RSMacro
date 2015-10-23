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

    public Crawler(GameType type) {
        this.type = type;
        pack = Configuration.CACHE + (type == GameType.OSRS ? "os" : "rs3") +
                "_pack.jar";
        CacheData.parseWorld();
        home = String.format("http://oldschool%d.runescape.com/",
                CacheData.world());
        config = home + "jav_config.ws";
    }

    public Applet start(ClassLoader classloader) {
        try {
            String main = parameters.get("initial_class").replace(".class", "");
            Applet applet = (Applet) classloader.loadClass(main).newInstance();
            applet.setBackground(Color.BLACK);
            applet.setPreferredSize(appletSize());
            applet.setLayout(null);
            applet.setStub(envFor(applet));
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

    public AppletStub envFor(Applet applet) {
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

    public int hash() {
        return hash;
    }

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

    public void initHash() {
        hash = localHash();
    }

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

    public boolean download(String target) {
        return download(target, null);
    }

    public boolean download() {
        return download(pack);
    }

    public boolean download(Runnable callback) {
        return download(pack, callback);
    }

    public Dimension appletSize() {
        try {
            return new Dimension(
                    Integer.parseInt(parameters.get("applet_minwidth")),
                    Integer.parseInt(parameters.get("applet_minheight")));
        } catch (NumberFormatException e) {
            return GameCanvas.GAME_SIZE;
        }
    }

    public enum GameType {
        OSRS, RS3
    }
}