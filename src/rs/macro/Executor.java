package rs.macro;

import rs.macro.util.OperatingSystem;
import rs.macro.util.Strings;
import rs.macro.util.io.Streams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Executor {

    private static final char QUOTE = '"';
    private static final char SPACE = ' ';
    private static final char PATH_SEPARTOR = File.pathSeparatorChar;

    private static final String JAVA = "java";
    private static final String CLASS_PATH = "-classpath";
    private static final String XBOOT = "-Xbootclasspath/p:";

    private final List<String> flags = new ArrayList<>();
    private final List<String> libs = new ArrayList<>();

    private final Class<?> main, application;

    /**
     * Creates an Executor with the given arguments.
     *
     * @param main        The Class that will be calling this class.
     * @param application The Class to be ran.
     * @param appName     The name of the application.
     */
    public Executor(Class<?> main, Class<?> application, String appName) {
        this.main = main;
        this.application = application;
        if (OperatingSystem.get() == OperatingSystem.MAC) {
            StringBuilder macFlags = new StringBuilder();
            if (appName != null) {
                macFlags.append("-Xdock:name=");
                macFlags.append(QUOTE);
                macFlags.append(appName);
                macFlags.append(QUOTE);
                macFlags.append(SPACE);
            }
//            if (appIcon != null) {
//                macFlags.append("-Xdock:icon=");
//                macFlags.append(QUOTE);
//                macFlags.append(appIcon);
//                macFlags.append(QUOTE);
//                macFlags.append(SPACE);
//            }
            macFlags.append("-Dcom.apple.macos.useScreenMenuBar=true");
            macFlags.append(SPACE);
            flags.add(macFlags.toString());
        }
    }

    /**
     * Appends the given flag to the list of application flags.
     *
     * @param flag The flag to append.
     * @return The current Executor with the appended flag.
     */
    public Executor flag(String flag) {
        flags.add(flag);
        return this;
    }

    /**
     * Appends an Xboot argument to the list of application flags.
     *
     * @param jar The Jar to be on the -Xbootclasspath.
     * @return The current Executor with the appended Xboot argument.
     */
    public Executor xboot(File jar) {
        StringBuilder builder = new StringBuilder();
        builder.append(XBOOT);
        builder.append(QUOTE);
        builder.append(Strings.cleanUTF8(jar.getAbsolutePath()));
        builder.append(QUOTE);
        flags.add(builder.toString());
        return this;
    }

    /**
     * Appends the given jar library to the list of application libraries.
     *
     * @param jar The library to be on the -classpath.
     * @return The current Executor with the appended jar library.
     */
    public Executor lib(File jar) {
        libs.add(Strings.cleanUTF8(jar.getAbsolutePath()));
        return this;
    }

    /**
     * Executes the built command.
     *
     * @return The built process's #waitFor result.
     */
    public int start() {
        StringBuilder builder = new StringBuilder();
        builder.append(JAVA);
        for (String flag : flags) {
            builder.append(SPACE);
            builder.append(flag);
        }
        builder.append(SPACE);
        builder.append(CLASS_PATH);
        builder.append(SPACE);
        String location = main.getProtectionDomain().getCodeSource().getLocation().getPath();
        location = Strings.cleanUTF8(location);
        builder.append(QUOTE);
        builder.append(location);
        builder.append(QUOTE);
        for (String lib : libs) {
            builder.append(PATH_SEPARTOR);
            builder.append(QUOTE);
            builder.append(lib);
            builder.append(QUOTE);
        }
        builder.append(SPACE);
        builder.append(application.getCanonicalName());
        String command = builder.toString();
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process;
            if (OperatingSystem.get() == OperatingSystem.WINDOWS) {
                process = runtime.exec(command);
            } else {
                process = runtime.exec(new String[]{"sh", "-c", command});
            }
            Streams.pipe(process.getInputStream());
            Streams.pipe(process.getErrorStream());
            return process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
