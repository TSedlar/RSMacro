package rs.macro.api.util;

import rs.macro.api.util.filter.TriFilter;
import rs.macro.api.util.fx.Colors;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class Imaging {

    private static final Stream<Point> EMPTY_STREAM = new ArrayList<Point>().stream();

    /**
     * Gets the equivalent argb value.
     *
     * @param pixels The pixel byte array.
     * @param idx The index.
     * @param alpha The alpha layer.
     * @return The equivalent arbg value.
     */
    public static int pixelBytesToARGB(byte[] pixels, int idx, boolean alpha) {
        int startIndex = (alpha ? 0 : -1);
        int argb = 0;
        if (alpha) {
            argb += (((int) pixels[idx] & 0xff) << 24);
        } else {
            argb += -16777216;
        }
        argb += ((int) pixels[idx + (startIndex + 1)] & 0xff);
        argb += (((int) pixels[idx + (startIndex + 2)] & 0xff) << 8);
        argb += (((int) pixels[idx + (startIndex + 3)] & 0xff) << 16);
        return argb;
    }

    /**
     * Compresses pixel bytes to an integer array.
     *
     * @param image The BufferedImage to query.
     * @param pixels The pixel byte array.
     * @return An integer array of compressed pixel bytes.
     */
    public static int[] compressPixelBytes(BufferedImage image, byte[] pixels) {
        boolean alpha = image.getAlphaRaster() != null;
        int length = (alpha ? 4 : 3);
        int[] compressed = new int[pixels.length / length];
        for (int i = 0; i < (pixels.length / length); i += length) {
            int argb = pixelBytesToARGB(pixels, i, alpha);
            compressed[i / length] = argb;
        }
        return compressed;
    }

    /**
     * Queries a BufferedImage using the following arguments:
     *
     * @param image The BufferedImage.
     * @param pixels The pixel integer array.
     * @param pixelFilter The TriFilter to query with.
     * @return The Stream of points that are valid using the specified arguments.
     */
    public static Stream<Point> query(BufferedImage image, int[] pixels,
                                      TriFilter<Integer, Integer, Integer> pixelFilter) {
        if (image == null) {
            return EMPTY_STREAM;
        }
        int w = image.getWidth(), h = image.getHeight();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < pixels.length; i++) {
            int x = i % w;
            int y = (i / w) % h;
            int argb = pixels[i];
            if (pixelFilter.accept(x, y, argb)) {
                points.add(new Point(x, y));
            }
        }
        return points.stream();
    }

    /**
     * Gets an integer array of pixels from a BufferedImage.
     *
     * @param image The BufferedImage.
     * @return An integer array of pixels from the BufferedImage.
     */
    @SuppressWarnings("unchecked")
    public static int[] pixelsFor(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        byte[] pixelBytes = new byte[0];
        int[] pixelInts = new int[0];
        DataBuffer buffer = raster.getDataBuffer();
        if (buffer instanceof DataBufferByte) {
            pixelBytes = ((DataBufferByte) buffer).getData();
        } else if (buffer instanceof DataBufferInt) {
            pixelInts = ((DataBufferInt) buffer).getData();
        }
        if (pixelBytes.length > 0) {
            pixelInts = compressPixelBytes(image, pixelBytes);
        }
        return pixelInts;
    }

    /**
     * Gets the arbg value at the specified coordinates.
     *
     * @param image     The BufferedImage.
     * @param pixels    The pixel integer array.
     * @param x         The x coordinate.
     * @param y         The y coordinate.
     * @return The arbg value at the specified coordinates.
     */
    public static int argbAt(BufferedImage image, int[] pixels, int x, int y) {
        if (image == null) {
            return Colors.BLACK;
        }
        try {
            return pixels[x + y * image.getWidth()];
        } catch (ArrayIndexOutOfBoundsException e) {
            return Colors.BLACK;
        }
    }
}
