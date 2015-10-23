package rs.macro;

import rs.macro.util.Configuration;
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

    public static void main(String[] args) throws Exception {
        Configuration.setup();
        ResourceLoader resources = new ResourceLoader("./rs/macro/res/");
        InputStream canvas = resources.get("lib/CanvasHack.jar");
        File canvasJar = new File(Configuration.LIBRARIES, "CanvasHack.jar");
        try (FileOutputStream localCanvas = new FileOutputStream(canvasJar)) {
            Streams.transfer(canvas, localCanvas);
        }
        new Executor(Boot.class, RSMacro.class, Configuration.APPLICATION_NAME)
                .flag("-Dsun.java2d.d3d=false")
                .flag("-XX:SurvivorRatio=4")
                .flag("-XX:+UseCompressedClassPointers")
                .flag("-XX:+UseCompressedOops")
                .flag("-XX:+UseParNewGC")
                .flag("-XX:+UseConcMarkSweepGC")
                .xboot(canvasJar)
                .lib(canvasJar)
                .start();
    }
}
