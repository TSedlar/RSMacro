package rs.macro.api.methods;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 5/16/15
 */
public class Viewport {

    public static final Rectangle BOUNDS = new Rectangle(4, 6, 511, 334);
    private static final Point CENTER = new Point(240, 165);
//    private static final Point CENTER = new Point(BOUNDS.x + (BOUNDS.width / 2),
//            BOUNDS.y + (BOUNDS.height / 2));

    public static int x() {
        return BOUNDS.x;
    }

    public static int y() {
        return BOUNDS.y;
    }

    public static int width() {
        return BOUNDS.width;
    }

    public static int height() {
        return BOUNDS.height;
    }

    public static Point center() {
        return CENTER;
    }
}