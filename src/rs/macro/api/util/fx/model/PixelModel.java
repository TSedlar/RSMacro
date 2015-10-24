package rs.macro.api.util.fx.model;

import rs.macro.api.methods.RuneScape;

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

    public PixelModel(Pixel root, Pixel... pixels) {
        this.root = root;
        this.pixels = pixels;
    }

    public static PixelModel fromList(List<Pixel> pixels) {
        Pixel root = pixels.get(0);
        Pixel[] pixelArray = pixels.toArray(new Pixel[pixels.size()]);
        Pixel[] trimmedPixelArray = new Pixel[pixelArray.length - 1];
        System.arraycopy(pixelArray, 1, trimmedPixelArray, 0, trimmedPixelArray.length);
        return new PixelModel(root, trimmedPixelArray);
    }

    public static PixelModel fromString(String string) {
        String[] data = string.split(SPACE);
        List<Pixel> pixels = new ArrayList<>();
        for (String str : data) {
            pixels.add(Pixel.fromString(str));
        }
        return fromList(pixels);
    }

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
}
