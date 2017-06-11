package rs.macro.api.util.fx.model;

import rs.macro.api.access.RuneScape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 4/30/15
 */
public class PixelModel {

    private static final String SPACE = " ";

    public final Pixel root;
    public final Pixel[] pixels;

    /**
     * Constructs a PixelModel with the following arguments:
     *
     * @param root   The root Pixel.
     * @param pixels The subsequent pixels.
     */
    public PixelModel(Pixel root, Pixel... pixels) {
        this.root = root;
        this.pixels = pixels;
    }

    /**
     * Constructs a PixelModel from a list of pixels.
     *
     * @param pixels The list of pixels.
     * @return The PixelModel representation of the list of pixels.
     */
    public static PixelModel fromList(List<Pixel> pixels) {
        Pixel root = pixels.get(0);
        Pixel[] pixelArray = pixels.toArray(new Pixel[pixels.size()]);
        Pixel[] trimmedPixelArray = new Pixel[pixelArray.length - 1];
        System.arraycopy(pixelArray, 1, trimmedPixelArray, 0, trimmedPixelArray.length);
        return new PixelModel(root, trimmedPixelArray);
    }

    /**
     * Constructs a PixelModel from a String.
     *
     * @param string The String representation of the pixels.
     * @return The PixelModel representation of the String.
     */
    public static PixelModel fromString(String string) {
        String[] data = string.split(SPACE);
        List<Pixel> pixels = new ArrayList<>();
        for (String str : data) {
            pixels.add(Pixel.fromString(str));
        }
        return fromList(pixels);
    }

    /**
     * Constructs a PixelModel from a list of points and a tolerance.
     *
     * @param points     The list of points.
     * @param tolterance The pixel tolerance.
     * @return The PixelModel representation of the list of points and color tolerance.
     */
    public static PixelModel fromPoints(List<Point> points, int tolterance) {
        List<Pixel> pixels = new ArrayList<>();
        Point root = points.get(0);
        pixels.add(new Pixel(RuneScape.rgbAt(root.x, root.y), tolterance));
        for (int i = 1; i < points.size(); i++) {
            Point current = points.get(i);
            pixels.add(new Pixel(RuneScape.rgbAt(current.x, current.y), tolterance,
                    current.x - root.x, current.y - root.y));
        }
        return fromList(pixels);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(root.toString());
        for (Pixel pixel : pixels) {
            builder.append(SPACE).append(pixel);
        }
        return builder.toString();
    }

    /**
     * Draws the PixelModel at the given location.
     *
     * @param g The Graphics2D object to draw on.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     */
    public void draw(Graphics2D g, int x, int y) {
        for (Pixel pixel : pixels) {
            g.setColor(pixel.color);
            g.fillRect(x + pixel.xOff, y + pixel.yOff, 1, 1);
        }
    }

    /**
     * Draws the PixelModel at the given location.
     *
     * @param g     The Graphics2D object to draw on.
     * @param color The color to draw with
     * @param x     The X coordinate.
     * @param y     The Y coordinate.
     */
    public void draw(Graphics2D g, Color color, int x, int y) {
        for (Pixel pixel : pixels) {
            g.setColor(color);
            g.fillRect(x + pixel.xOff, y + pixel.yOff, 1, 1);
        }
    }
}
