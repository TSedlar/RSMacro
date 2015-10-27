package rs.macro.api.util.fx;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class Colors {

    public static final String HEX_FORMAT = "#%02X%02X%02X";

    public static final int BLACK = Color.BLACK.getRGB();

    /**
     * Obtains the alpha value out of the given ARGB.
     *
     * @param argb The ARGB value to parse.
     * @return The alpha value from the given ARGB.
     */
    public static int alpha(int argb) {
        return (argb >> 24) & 0xFF;
    }

    /**
     * Obtains the red value out of the given ARGB.
     *
     * @param argb The ARGB value to parse.
     * @return The red value from the given ARGB.
     */
    public static int red(int argb) {
        return (argb >> 16) & 0xFF;
    }

    /**
     * Obtains the green value out of the given ARGB.
     *
     * @param argb The ARGB value to parse.
     * @return The green value from the given ARGB.
     */
    public static int green(int argb) {
        return (argb >> 8) & 0xFF;
    }

    /**
     * Obtains the blue value out of the given ARGB.
     *
     * @param argb The ARGB value to parse.
     * @return The blue value from the given ARGB.
     */
    public static int blue(int argb) {
        return (argb & 0xFF);
    }

    /**
     * Turns a hexadecimal color into its rbg equivalent.
     *
     * @param hex The hex color.
     * @return The rbg representation of the hex color.
     */
    public static int hexToRGB(String hex) {
        int i = Integer.decode(hex);
        int a = (i >> 24) & 0xFF, r = (i >> 16) & 0xFF, g = (i >> 8) & 0xFF, b = i & 0xFF;
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
    }

    /**
     * Turns an rbg color into its hexadecimal equivalent.
     *
     * @param rgb The rbg color.
     * @return The String hex representation of the rbg color.
     */
    public static String rgbToHex(int rgb) {
        int r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
        return String.format(HEX_FORMAT, r, g, b);
    }

    /**
     * Gets the average of two colors.
     *
     * @param rgb1 The first rbg color.
     * @param rgb2 The second rbg color.
     * @return The average of the two colors.
     */
    public static double average(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF, r2 = (rgb2 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF, g2 = (rgb2 >> 8) & 0xFF;
        int b1 = (rgb1 & 0xFF), b2 = (rgb2 & 0xFF);
        long rmean = ((long) r1 + (long) r2) / 2;
        long r = (long) r1 - (long) r2;
        long g = (long) g1 - (long) g2;
        long b = (long) b1 - (long) b2;
        return Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
    }

    /**
     * Gets the average tolerance of two colors.
     *
     * @param rgb1 The first rbg color.
     * @param rgb2 The second rbg color.
     * @return The average tolerance of the two colors.
     */
    public static int tolerance(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF, r2 = (rgb2 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF, g2 = (rgb2 >> 8) & 0xFF;
        int b1 = (rgb1 & 0xFF), b2 = (rgb2 & 0xFF);
        return (Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2)) / 3;
    }

    /**
     * Gets the squared distance between the two given RGB values.
     *
     * @param rgb1 The first RGB to compare.
     * @param rgb2 The second RGB to compare.
     * @return The squared distance between the two given RGB values.
     */
    public static int distanceSquared(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF, r2 = (rgb2 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF, g2 = (rgb2 >> 8) & 0xFF;
        int b1 = (rgb1 & 0xFF), b2 = (rgb2 & 0xFF);
        int rDist = (r2 - r1);
        int gDist = (g2 - g1);
        int bDist = (b2 - b1);
        return (rDist * rDist) + (gDist * gDist) + (bDist * bDist);
    }

    /**
     * Obtains the median color from the given array of pixels.
     *
     * @param pixels The pixels to find the median for.
     * @return The median color from the given array of pixels.
     */
    public static int median(int[] pixels) {
        int area = pixels.length;
        int alpha = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int rgb : pixels) {
            alpha += ((rgb >> 24) & 0xFF);
            red += ((rgb) & 0xFF);
            green += ((rgb >> 8) & 0xFF);
            blue += ((rgb >> 16) & 0xFF);
        }
        return (((alpha / area) << 24) | ((red / area) << 16) | ((green / area) << 8) |
                ((blue / area)));
    }
}
