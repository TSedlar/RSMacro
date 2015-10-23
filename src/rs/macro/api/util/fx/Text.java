package rs.macro.api.util.fx;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 4/30/2015
 */
public class Text {

    public static void drawRuneString(Graphics2D g, String text, int x, int y, Color foreground, Color background) {
        g.setColor(background);
        g.drawString(text, x + 1, y + 1);
        g.setColor(foreground);
        g.drawString(text, x, y);
    }

    public static void drawRuneString(Graphics2D g, String text, int x, int y, Color foreground) {
        drawRuneString(g, text, x, y, foreground, Color.BLACK);
    }
}