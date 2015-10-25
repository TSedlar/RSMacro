package rs.macro.api.util.fx;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 4/30/15
 */
public class Shapes {

    /**
     * Gets the list of points within the specified Shape.
     *
     * @param shape The Shape.
     * @return A list of points within the specified Shape.
     */
    public static List<Point> pointsFor(Shape shape) {
        List<Point> points = new LinkedList<>();
        Rectangle bounds = shape.getBounds();
        for (int x = bounds.x; x < bounds.getMaxX(); x++) {
            for (int y = bounds.y; y < bounds.getMaxY(); y++) {
                if (shape.contains(x, y)) {
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }
}