package rs.macro.api.access;

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

    /**
     * @return The starting x coordinate of the viewport.
     */
    public static int x() {
        return BOUNDS.x;
    }

    /**
     * @return The starting y coordinate of the viewport.
     */
    public static int y() {
        return BOUNDS.y;
    }

    /**
     * @return The viewport's width.
     */
    public static int width() {
        return BOUNDS.width;
    }

    /**
     * @return The viewport's height.
     */
    public static int height() {
        return BOUNDS.height;
    }

    /**
     * @return The viewport's center Point.
     */
    public static Point center() {
        return CENTER;
    }
}