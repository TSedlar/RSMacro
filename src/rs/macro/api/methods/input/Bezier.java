package rs.macro.api.methods.input;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Bezier {

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

    public static List<Point> generate(int targetX, int targetY, Point cp1, Point cp2) {
        return generate(Mouse.x(), Mouse.y(), targetX, targetY, cp1, cp2);
    }

    public static List<Point> generate(int ex, int ey) {
        Point mid = new Point((Mouse.x() + ex) / 2, (Mouse.y() + ey) / 2);
        Point quarterStart = new Point((Mouse.x() + mid.x) / 2, (Mouse.y() + mid.y) / 2);
        Point quarterEnd = new Point((ex + mid.x) / 2, (ex + mid.y) / 2);
        if (ex > Mouse.x()) {
            quarterStart.y -= (Math.abs(Mouse.y() - quarterStart.y));
        } else {
            quarterStart.y += Math.abs(Mouse.y() - quarterStart.y);
        }
        return generate(ex, ey, quarterStart, quarterEnd);
    }
}
