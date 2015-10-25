package rs.macro.api.util.fx.model;

import rs.macro.api.util.fx.Colors;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 4/30/15
 */
public class Pixel {

    private static final String HEX_TOL_FORMAT = "%s/%s";
    private static final String TO_STRING_FORMAT = "%s/%s/%s/%s";
    private static final String SLASH = "/";
    private static final String HASHTAG = "#";

    public final int rgb, tolerance, xOff, yOff;
    public final Color color;

    /**
     * Constructs a Pixel with the following arguments:
     *
     * @param color     The rbg color.
     * @param tolerance The pixel's tolerance.
     * @param xOff      The x offset.
     * @param yOff      The y offset.
     */
    public Pixel(int color, int tolerance, int xOff, int yOff) {
        this.rgb = color;
        this.tolerance = tolerance;
        this.xOff = xOff;
        this.yOff = yOff;
        this.color = new Color(color);
    }

    /**
     * Constructs a Pixel with the following arguments:
     *
     * @param rgb       The rbg color.
     * @param tolerance The pixel's tolerance.
     */
    public Pixel(int rgb, int tolerance) {
        this(rgb, tolerance, 0, 0);
    }

    /**
     * Constructs a Pixel with the following arguments:
     *
     * @param hex       The hexadecimal String representation of the color.
     * @param tolerance The pixel's tolerance.
     * @param xOff      The x offset.
     * @param yOff      The y offset.
     */
    public Pixel(String hex, int tolerance, int xOff, int yOff) {
        this(Colors.hexToRGB(hex), tolerance, xOff, yOff);
    }

    /**
     * Constructs a Pixel with the following arguments:
     *
     * @param hex       The hexadecimal String representation of the color.
     * @param tolerance The pixel's tolerance.
     */
    public Pixel(String hex, int tolerance) {
        this(hex, tolerance, 0, 0);
    }

    @Override
    public String toString() {
        String hex = Colors.rgbToHex(rgb);
        return xOff == 0 && yOff == 0 ? String.format(HEX_TOL_FORMAT, hex, tolerance) :
                String.format(TO_STRING_FORMAT, hex, tolerance, xOff, yOff);
    }

    /**
     * Constructs a Pixel from the following argument:
     *
     * @param string The String representation of the color.
     * @return The Pixel representation of the String.
     */
    public static Pixel fromString(String string) {
        String[] strings = string.split(SLASH);
        String color = strings[0];
        int tolerance = Integer.parseInt(strings[1]);
        int xOff = 0, yOff = 0;
        if (strings.length > 2) {
            xOff = Integer.parseInt(strings[2]);
            yOff = Integer.parseInt(strings[3]);
        }
        return color.contains(HASHTAG) ? new Pixel(color, tolerance, xOff, yOff) :
                new Pixel(Integer.parseInt(color), tolerance, xOff, yOff);
    }
}