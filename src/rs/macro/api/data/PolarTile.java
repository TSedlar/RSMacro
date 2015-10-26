package rs.macro.api.data;

import rs.macro.api.access.minimap.Minimap;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 10/26/15
 */
public class PolarTile {
 
    public static final int NORTH = 0;
    public static final int NORTH_EAST = 45;
    public static final int EAST = 90;
    public static final int SOUTH_EAST = 135;
    public static final int SOUTH = 180;
    public static final int SOUTH_WEST = 225;
    public static final int WEST = 270;
    public static final int NORTH_WEST = 315;
    
    public static final int MINIMAP_RADIUS = 72;
 
    public final int angle;
    public final int distance;
 
    /**
     * Creates a vector tile based on the game's compass angle.
     *
     * @param angle    The angle at which to focus towards.
     * @param distance The distance away from the center of the minimap.
     */
    public PolarTile(int angle, int distance) {
        if (distance < 0 || distance > MINIMAP_RADIUS) {
            throw new IllegalArgumentException("Distance out of bounds: [0, 72]");
        }
        this.angle = angle;
        this.distance = distance;
    }
 
    /**
     * Gets the point on the minimap relative to the given constructor's parameters.
     *
     * @return A point on the minimap relative to the given constructor's parameters.
     */
    public Point point() {
        double rads = Math.toRadians(angle + 90 - Minimap.angle());
        return new Point((int) (Minimap.CENTER.x - distance * Math.cos(rads)),
                (int) (Minimap.CENTER.y - distance * Math.sin(rads)));
    }
 
    /**
     * Gets the angle of a point on the minimap relative to the minimap's center and the compass angle.
     *
     * @param point A point on the minimap.
     * @return The angle of a point on the minimap relative to the minimap's center and the compass angle.
     */
    public static int angleTo(Point point) {
        if (!Minimap.BOUNDS.contains(point) || point.distance(Minimap.CENTER) >
                PolarTile.MINIMAP_RADIUS) {
            return -1;
        }
        double rads = Math.atan2(point.x - Minimap.CENTER.x,
                point.y - Minimap.CENTER.y) - (Math.PI);
        double angle = Minimap.angle() - Math.toDegrees(rads);
        while (angle < 0) {
            angle += 360;
        }
        angle %= 360;
        return (int) angle;
    }
 
    /**
     * Constructs a PolarTile based off the given point.
     *
     * @param point The point to construct a PolarTile off of.
     * @return A PolarTile based off the given point.
     */
    public static PolarTile fromPoint(Point point) {
        if (point == null) {
            return null;
        }
        return new PolarTile(angleTo(point), (int) point.distance(Minimap.CENTER));
    }
}