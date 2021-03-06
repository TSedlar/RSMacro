package rs.macro.api.access.input;

import rs.macro.api.util.Arithmetic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Bezier {

    /**
     * Gets the cubic point using the following arguments:
     *
     * @param cp The control Point array.
     * @param t  Theta.
     * @return The cubic Point.
     */
    private static Point cubic(Point[] cp, double t) {
        double curve = 3D;
        double cx = curve * (cp[1].x - cp[0].x);
        double bx = curve * (cp[2].x - cp[1].x) - cx;
        double ax = cp[3].x - cp[0].x - cx - bx;
        double cy = curve * (cp[1].y - cp[0].y);
        double by = curve * (cp[2].y - cp[1].y) - cy;
        double ay = cp[3].y - cp[0].y - cy - by;
        double tSquared = t * t;
        double tCubed = tSquared * t;
        int resultX = (int) Math.round((ax * tCubed) + (bx * tSquared) + (cx * t) + cp[0].x);
        int resultY = (int) Math.round((ay * tCubed) + (by * tSquared) + (cy * t) + cp[0].y);
        return new Point(resultX, resultY);
    }

    /**
     * Generates a list of points along the Bezier curve from a start Point to the target Point.
     *
     * @param startX  The starting x position.
     * @param startY  The starting y position.
     * @param targetX The ending x position.
     * @param targetY The ending y position.
     * @param cp1     The first cubic Point.
     * @param cp2     The second cubic Point.
     * @return The list of points representing the Bezier curve.
     */
    public static List<Point> generate(int startX, int startY, int targetX, int targetY,
                                       Point cp1, Point cp2) {
        double dist = Point.distance(startX, startY, targetX, targetY);
        Point[] controls = {new Point(startX, startY), cp1, cp2, new Point(targetX, targetY)};
        double increment = 1D / dist;
        double theta = 0D;
        List<Point> pointList = new ArrayList<>();
        while (theta < 1D) {
            increment = Math.min(1D - theta, increment);
            theta = theta + increment;
            pointList.add(cubic(controls, theta));
        }
        return pointList;
    }

    /**
     * Generates a list of points along the Bezier curve to the target Point.
     *
     * @param targetX The ending x position.
     * @param targetY The ending y position.
     * @param cp1     The first cubic Point.
     * @param cp2     The second cubic Point.
     * @return The list of points representing the Bezier curve.
     */
    public static List<Point> generate(int targetX, int targetY, Point cp1, Point cp2) {
        return generate(Mouse.x(), Mouse.y(), targetX, targetY, cp1, cp2);
    }

    /**
     * Generates a list of points along the Bezier curve
     *
     * @param ex The ending x coordinate.
     * @param ey The ending y coordinate.
     * @return The list of points representing the Bezier curve.
     */
    public static List<Point> generate(int ex, int ey) {
        return generate(Mouse.x(), Mouse.y(), ex, ey);
    }

    /**
     * Generates a list of points along the Bezier curve
     *
     * @param sx The starting x coordinate.
     * @param sy The starting y coordinate.
     * @param ex The ending x coordinate.
     * @param ey The ending y coordinate.
     * @return The list of points representing the Bezier curve.
     */
    public static List<Point> generate(int sx, int sy, int ex, int ey) {
        int dist = (int) Arithmetic.distance(sx, sy, ex, ey);
        float angle = Arithmetic.angleBetween(sx, sy, ex, ey);
        int angOff = (ex > sx ? -6 : 6);
        int cp1Dist = (int) (dist * 0.3D);
        Point cp1 = Arithmetic.polarFrom(Mouse.x(), Mouse.y(), angle + angOff, cp1Dist);
        int cp2Dist = (int) (dist * 0.7D);
        Point cp2 = Arithmetic.polarFrom(Mouse.x(), Mouse.y(), angle - angOff, cp2Dist);
        return generate(sx, sy, ex, ey, cp1, cp2);
    }
}
