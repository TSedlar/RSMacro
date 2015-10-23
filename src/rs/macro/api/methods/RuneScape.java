package rs.macro.api.methods;

import rs.macro.api.util.fx.PixelTask;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class RuneScape {

    private static PixelTask pixelTask = new PixelTask();

    public static PixelTask pixels() {
        return pixelTask;
    }

    public static int rgbAt(int x, int y) {
        return pixels().operator().at(x, y);
    }

    public static BufferedImage image() {
        return GameCanvas.raw;
    }

    public static BufferedImage fullImage() {
        return GameCanvas.buffer;
    }

    public static boolean validatePoint(int x, int y) {
        return x >= 0 && x <= GameCanvas.GAME_SIZE.width && y >= 0 && y <= GameCanvas.GAME_SIZE.height;
    }

    public static boolean validatePoint(Point p) {
        return validatePoint(p.x, p.y);
    }
}
