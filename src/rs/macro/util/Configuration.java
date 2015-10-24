package rs.macro.util;

import rs.macro.RSMacro;

import java.io.File;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class Configuration {

    /**
     * The name of the application.
     */
    public static final String APPLICATION_NAME = "RSMacro";

    /**
     * The home directory of the application.
     */
    public static final String HOME = home() + File.separator +
            APPLICATION_NAME + File.separator;

    /**
     * The cache directory located at HOME/cache.
     */
    public static final String CACHE = HOME + "cache" + File.separator;

    /**
     * The cache data directory located at CACHE/data.
     */
    public static final String DATA = CACHE + "data" + File.separator;

    /**
     * The library directory located at HOME/libraries.
     */
    public static final String LIBRARIES = HOME + "libraries" + File.separator;

    /**
     * The macros directory located at HOME/macros.
     */
    public static final String MACROS = HOME + "macros" + File.separator;

    /**
     * An array of the available directories.
     */
    public static final String[] DIRECTORIES = {CACHE, DATA, LIBRARIES, MACROS};

    /**
     * Checks whether the application is being ran from source or not.
     *
     * @return <t>true</t> if the application is being ran from source, otherwise <t>false</t>.
     */
    public static boolean local() {
        return !RSMacro.class.getResource(RSMacro.class.getSimpleName() +
                ".class").toString().contains("jar:");
    }

    /**
     * Creates all of the available directories.
     */
    public static void setup() {
        for (String dir : DIRECTORIES) {
            new File(dir).mkdirs();
        }
    }

    /**
     * Gets the path to the user's system home directory.
     *
     * @return The path to the user's system home directory.
     */
    public static String home() {
        OperatingSystem os = OperatingSystem.get();
        return (os == OperatingSystem.WINDOWS || os == OperatingSystem.MAC ?
                System.getProperty("user.home") + "/Documents/" :
                System.getProperty("user.home") + "/");
    }
}