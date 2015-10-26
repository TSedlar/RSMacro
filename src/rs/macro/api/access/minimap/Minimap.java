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

    private static final List<MinimapItem> items = new ArrayList<>();
    private static final Map<MinimapItem, List<Point>> render = new HashMap<>();

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

    /**
     * Removes all submitted MinimapItems
     */
    public static void clearItems() {
        items.clear();
    }

    public static void submitItem(MinimapItem item) {
        items.add(item);
    }

    /**
     * Collects all data (points, etc) according to the submitted MinimapItems
     */
    public static void collectItemRenderData() {
        for (MinimapItem item : items) {
            List<Point> points = RuneScape.pixels().operator().builder()
                    .filterLocation(BOUNDS::contains)
                    .tolFilter(item.rgb, item.tolerance)
                    .all();
            if (render.containsKey(item)) {
                render.get(item).clear();
                render.get(item).addAll(points);
            } else {
                render.put(item, points);
            }
        }
    }

    /**
     * Renders the MinimapItems onto the in-game minimap.
     *
     * @param g The graphics to render onto.
     */
    public static void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fill(BOUNDS);
        for (Map.Entry<MinimapItem, List<Point>> entry : render.entrySet()) {
            List<Point> points = entry.getValue();
            Point[] array = points.toArray(new Point[points.size()]);
            g.setColor(entry.getKey().display);
            for (Point p : array) {
                g.fillRect(p.x, p.y, 1, 1);
            }
        }
    }
}
