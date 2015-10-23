package rs.macro.util;

import rs.macro.RSMacro;

import java.io.File;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class Configuration {

    public static final String APPLICATION_NAME = "RSMacro";

    public static final String HOME = home() + File.separator +
            APPLICATION_NAME + File.separator;
    public static final String CACHE = HOME + "cache" + File.separator;
    public static final String DATA = CACHE + "data" + File.separator;
    public static final String LIBRARIES = HOME + "libraries" + File.separator;
    public static final String MACROS = HOME + "macros" + File.separator;
    public static final String[] DIRECTORIES = {CACHE, DATA, LIBRARIES, MACROS};


    public static boolean local() {
        return !RSMacro.class.getResource(RSMacro.class.getSimpleName() +
                ".class").toString().contains("jar:");
    }

    public static void setup() {
        for (String dir : DIRECTORIES) {
            new File(dir).mkdirs();
        }
    }

    public static String home() {
        OperatingSystem os = OperatingSystem.get();
        return (os == OperatingSystem.WINDOWS || os == OperatingSystem.MAC ?
                System.getProperty("user.home") + "/Documents/" :
                System.getProperty("user.home") + "/");
    }
}