package rs.macro.api.util.fx;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class Colors {

    public static final String HEX_FORMAT = "#%02X%02X%02X";

    public static final int BLACK = Color.BLACK.getRGB();

    public static int hexToRGB(String hex) {
        int i = Integer.decode(hex);
        int r = (i >> 16) & 0xFF, g = (i >> 8) & 0xFF, b = i & 0xFF;
        return ((0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
    }

    public static String rgbToHex(int rgb) {
        int r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
        return String.format(HEX_FORMAT, r, g, b);
    }

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

    public static int tolerance(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF, r2 = (rgb2 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF, g2 = (rgb2 >> 8) & 0xFF;
        int b1 = (rgb1 & 0xFF), b2 = (rgb2 & 0xFF);
        return (Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2)) / 3;
    }
}
