package rs.macro.api.util;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Vector2D {

    public double xUnits, yUnits;

    public Vector2D() {
        this(0.0, 0.0);
    }

    public Vector2D(double xUnits, double yUnits) {
        this.xUnits = xUnits;
        this.yUnits = yUnits;
    }

    public void add(Vector2D vector) {
        xUnits += vector.xUnits;
        yUnits += vector.yUnits;
    }

    public Vector2D copy() {
        return new Vector2D(xUnits, yUnits);
    }

    public double angle() {
        return Math.atan2(yUnits, xUnits);
    }

    public double length() {
        return Math.sqrt(xUnits * xUnits + yUnits * yUnits);
    }

    public double lengthSquared() {
        return xUnits * xUnits + yUnits * yUnits;
    }

    public void multiply(double factor) {
        xUnits *= factor;
        yUnits *= factor;
    }
}