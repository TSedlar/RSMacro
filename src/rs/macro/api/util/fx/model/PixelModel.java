package rs.macro.api.util.fx.model;

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

    public static PixelModel fromString(String string) {
        String[] data = string.split(SPACE);
        List<Pixel> pixels = new ArrayList<>();
        for (String str : data) {
            pixels.add(Pixel.fromString(str));
        }
        Pixel root = pixels.get(0);
        Pixel[] pixelArray = pixels.toArray(new Pixel[pixels.size()]);
        Pixel[] trimmedPixelArray = new Pixel[pixelArray.length - 1];
        System.arraycopy(pixelArray, 1, trimmedPixelArray, 0, trimmedPixelArray.length);
        return new PixelModel(root, trimmedPixelArray);
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
