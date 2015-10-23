package rs.macro.api.util;

import rs.macro.api.util.filter.TriFilter;
import rs.macro.api.util.fx.Colors;

import java.awt.*;
import java.awt.image.*;
import java.util.stream.Stream;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class Imaging {

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

    public static int[] compressPixelBytes(BufferedImage image, byte[] pixels) {
        boolean alpha = image.getAlphaRaster() != null;
        int length = (alpha ? 4 : 3);
        int[] compressed = new int[pixels.length / length];
        Stream.iterate(0, n -> n + length)
                .limit(pixels.length / length)
                .map(i -> {
                    int argb = pixelBytesToARGB(pixels, i, alpha);
                    compressed[i / length] = argb;
                    return argb;
                }).count();
        return compressed;
    }

    public static Stream<Point> query(BufferedImage image, int[] pixels,
                                      TriFilter<Integer, Integer, Integer> pixelFilter) {
        int w = image.getWidth(), h = image.getHeight();
        return Stream.iterate(0, n -> ++n)
                .limit(pixels.length)
                .filter(i -> {
                    int x = i % w;
                    int y = (i / w) % h;
                    int argb = pixels[i];
                    return pixelFilter.accept(x, y, argb);
                })
                .map(i -> {
                    int x = i % w;
                    int y = (i / w) % h;
                    return new Point(x, y);
                });
    }

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
