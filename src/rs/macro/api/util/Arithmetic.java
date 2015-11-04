package rs.macro.api.util;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 11/4/15
 */
public class Arithmetic {

    /**
     * Obtains the angle between the two given coordinates.
     *
     * @param sx The starting x coordinate.
     * @param sy The starting y coordinate.
     * @param ex The ending x coordinate.
     * @param ey The ending y coordinate.
     * @return The angle between the two given coordinates.
     */
    public static float angleBetween(int sx, int sy, int ex, int ey) {
        float angle = (float) Math.toDegrees(Math.atan2(ey - sy, ex - sx));
        if (angle < 0F) {
            angle += 360F;
        }
        return angle;
    }

    /**
     * Obtains the angle between the two given coordinates.
     *
     * @param start The starting coordinate.
     * @param end   The ending coordinate.
     * @return The angle between the two given coordinates.
     */
    public static float angleBetween(Point start, Point end) {
        return angleBetween(start.x, start.y, end.x, end.y);
    }

    /**
     * Obtains the point calculated from the given arguments.
     *
     * @param cx       The point's x coordinate to base the result off of.
     * @param cy       The point's y coordinate to base the result off of.
     * @param angle    The angle to direct at.
     * @param distance The distance to go.
     * @return The point calculated from the given arguments.
     */
    public static Point polarFrom(int cx, int cy, float angle, int distance) {
        double rads = Math.toRadians(angle + 180);
        return new Point((int) (cx - distance * Math.cos(rads)),
                (int) (cy - distance * Math.sin(rads)));
    }

    /**
     * Obtains the point calculated from the given arguments.
     *
     * @param center   The point to base the result off of.
     * @param angle    The angle to direct at.
     * @param distance The distance to go.
     * @return The point calculated from the given arguments.
     */
    public static Point polarFrom(Point center, float angle, int distance) {
        return polarFrom(center.x, center.y, angle, distance);
    }

    /**
     * Obtains the distance between the two given coordinates.
     *
     * @param sx The starting x coordinate.
     * @param sy The starting y coordinate.
     * @param ex The ending x coordinate.
     * @param ey The ending y coordinate.
     * @return The distance between the two given coordinates.
     */
    public static double distance(int sx, int sy, int ex, int ey) {
        return Math.sqrt((sx - ex) * (sx - ex) + (sy - ey) * (sy - ey));
    }

    /**
     * Obtains the distance between the two given coordinates.
     *
     * @param start The starting coordinate.
     * @param end The ending coordinate.
     * @return The distance between the two given coordinates.
     */
    public static double distance(Point start, Point end) {
        return distance(start.x, start.y, end.x, end.y);
    }
}
