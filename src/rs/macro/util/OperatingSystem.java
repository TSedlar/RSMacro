package rs.macro.util;

import java.io.File;

/**
 * @author Tyler Sedlar
 * @since 10/31/14
 */
public enum OperatingSystem {

    WINDOWS, MAC, LINUX, UNKNOWN;

    /**
     * Gets the path to the user home directory.
     *
     * @return The path to the user home directory.
     */
    public static String home() {
        return System.getProperty("user.home") + File.separator;
    }

    @Override
    public String toString() {
        String orig = super.toString();
        return Character.toUpperCase(orig.charAt(0)) +
                orig.substring(1).toLowerCase();
    }

    /**
     * Checks whether the OS is 64bit or not.
     *
     * @return <t>true</t> if the OS is 64bit, otherwise <t>false</t>.
     */
    public boolean arch64() {
        String[] keys = {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
        for (String key : keys) {
            String property = System.getProperty(key);
            if (property != null && property.contains("64")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the OS data string that's contained within a User-Agent.
     *
     * @return Gets the OS data string that's contained within a User-Agent.
     */
    public String userAgentPart() {
        switch (get()) {
            case LINUX: {
                return "X11; Linux " + (arch64() ? "x86_64" : "i686");
            }
            case MAC: {
                return "Macintosh; Intel Mac OS X 10_7_5";
            }
            default: {
                return "Windows NT 6.1" + (arch64() ? "; WOW64" : "");
            }
        }
    }

    /**
     * Gets the currently running OperatingSystem.
     *
     * @return The currently running OperatingSystem.
     */
    public static OperatingSystem get() {
        String os = System.getProperty("os.name");
        for (OperatingSystem o : values()) {
            if (os.contains(o.toString())) {
                return o;
            }
        }
        return UNKNOWN;
    }
}