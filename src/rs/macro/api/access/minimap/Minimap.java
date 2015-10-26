package rs.macro.api.access.minimap;

import rs.macro.api.access.RuneScape;
import rs.macro.api.util.fx.Colors;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyler Sedlar
 * @since 10/23/15
 */
public class Minimap {

    public static final int ANGLE_UNDEFINED = Integer.MIN_VALUE;

    public static final Rectangle COMPASS_BOUNDS = new Rectangle(542, 1, 38, 39);
    public static final int COMPASS_RGB = Colors.hexToRGB("#31291D");

    public static final Ellipse2D BOUNDS = new Ellipse2D.Double(579, 12, 130, 130);
    public static final Point CENTER = new Point((int) BOUNDS.getCenterX() - 2,
            (int) BOUNDS.getCenterY() + 8);

    /**
     * @return The compass's center Point.
     */
    public static Point compass() {
        return new Point(COMPASS_BOUNDS.x + (COMPASS_BOUNDS.width / 2), COMPASS_BOUNDS.y + (COMPASS_BOUNDS.height / 2));
    }

    /**
     * Gets the compass's current angle.
     *
     * @return The angle which the compass is facing.
     */
    public static int angle() {
        List<Point> points = RuneScape.pixels().operator().builder()
                .bounds(COMPASS_BOUNDS)
                .tolFilter(COMPASS_RGB, 0)
                .all();
        if (points != null) {
            Polygon polygon = new Polygon();
            for (Point p : points) {
                polygon.addPoint(p.x, p.y);
            }
            Rectangle bounds = polygon.getBounds();
            Point center = new Point(bounds.x + (bounds.width / 2),
                    bounds.y + (bounds.height / 2));
            Point compass = compass();
            double angle = Math.toDegrees(Math.atan2(center.x - compass.x,
                    center.y - compass.y)) - 180;
            if (angle < 0) {
                angle += 360;
            }
            return (int) angle;
        }
        return ANGLE_UNDEFINED;
    }
}
