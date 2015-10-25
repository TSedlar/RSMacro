package rs.macro.api.util.fx;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 4/30/2015
 */
public class Text {

    /**
     * Draws a String using the following arguments:
     *
     * @param g             The Graphics2D object to draw with.
     * @param text          The text to display.
     * @param x             The x coordinate.
     * @param y             The y coordinate.
     * @param foreground    The foreground color.
     * @param background    The background color.
     */
    public static void drawRuneString(Graphics2D g, String text, int x, int y, Color foreground, Color background) {
        g.setColor(background);
        g.drawString(text, x + 1, y + 1);
        g.setColor(foreground);
        g.drawString(text, x, y);
    }

    /**
     * Draws a String using the following arguments;
     *
     * @param g             The Graphics2D object to draw with.
     * @param text          The text to display.
     * @param x             The x coordinate.
     * @param y             The y coordinate.
     * @param foreground    The foreground color.
     */
    public static void drawRuneString(Graphics2D g, String text, int x, int y, Color foreground) {
        drawRuneString(g, text, x, y, foreground, Color.BLACK);
    }
}