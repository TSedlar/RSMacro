package rs.macro;

import rs.macro.util.Configuration;
import rs.macro.util.OperatingSystem;
import rs.macro.util.io.ResourceLoader;
import rs.macro.util.io.Streams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Boot {

    /**
     * Sets up project configuration and executes the project with certain flags.
     *
     * @param args The flags to run the application with.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Configuration.setup();
        File canvasJar = new File(Configuration.LIBRARIES, "CanvasHack.jar");
        if (!canvasJar.exists()) {
            ResourceLoader resources = new ResourceLoader("./rs/macro/res/");
            InputStream canvas = resources.get("lib/CanvasHack.jar");
            try (FileOutputStream localCanvas = new FileOutputStream(canvasJar)) {
                Streams.transfer(canvas, localCanvas);
            }
        }
        Executor exec = new Executor(Boot.class, RSMacro.class,
                Configuration.APPLICATION_NAME, null)
                .flag("-Dsun.java2d.d3d=false")
                .flag("-XX:SurvivorRatio=4")
                .flag("-XX:+UseParNewGC")
                .flag("-XX:+UseConcMarkSweepGC");
        if (OperatingSystem.get() != OperatingSystem.WINDOWS) {
            exec = exec.flag("-XX:+UseCompressedClassPointers")
                    .flag("-XX:+UseCompressedOops");
        }
        for (String arg : args) {
            exec = exec.flag(arg);
        }
        exec = exec.xboot(canvasJar).lib(canvasJar);
        exec.start();
    }
}
