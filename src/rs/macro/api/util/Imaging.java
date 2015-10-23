package rs.macro.api.util;

import rs.macro.api.util.filter.TriFilter;
import rs.macro.api.util.fx.Colors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
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

    public static Stream<Point> query(BufferedImage image, byte[] pixels,
                                      TriFilter<Integer, Integer, Integer> pixelFilter) {
        int w = image.getWidth(), h = image.getHeight();
        int[] compressed = compressPixelBytes(image, pixels);
        return Stream.iterate(0, n -> ++n)
                .limit(compressed.length)
                .filter(i -> {
                    int x = i % w;
                    int y = (i / w) % h;
                    int argb = compressed[i];
                    return pixelFilter.accept(x, y, argb);
                })
                .map(i -> {
                    int x = i % w;
                    int y = (i / w) % h;
                    return new Point(x, y);
                });
    }

    public static byte[] pixelsFor(BufferedImage image) {
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
        return buffer.getData();
    }

    public static int argbAt(BufferedImage image, byte[] pixels, int x, int y) {
        if (image == null) {
            return Colors.BLACK;
        }
        try {
            boolean hasAlphaChannel = image.getAlphaRaster() != null;
            int width = image.getWidth();
            int argb = 0;
            if (hasAlphaChannel) {
                argb += (((int) pixels[4 * (x + y * width)] & 0xff) << 24);
            } else {
                argb += -16777216;
            }
            argb += ((int) pixels[3 * (x + y * width)] & 0xff);
            argb += (((int) pixels[1 + 3 * (x + y * width)] & 0xff) << 8);
            argb += (((int) pixels[2 + 3 * (x + y * width)] & 0xff) << 16);
            return argb;
        } catch (ArrayIndexOutOfBoundsException e) {
            return Colors.BLACK;
        }
    }
}
