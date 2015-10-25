package rs.macro.api.util;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Vector2D {

    public double xUnits, yUnits;

    /**
     * Constructs a Vector2D with 0.0 values.
     */
    public Vector2D() {
        this(0.0, 0.0);
    }

    /**
     * Constructs a Vector2D with the following arguments:
     *
     * @param xUnits The x unit.
     * @param yUnits The y unit.
     */
    public Vector2D(double xUnits, double yUnits) {
        this.xUnits = xUnits;
        this.yUnits = yUnits;
    }

    /**
     * Adds the specified Vector2D.
     *
     * @param vector The Vector2D to add.
     */
    public void add(Vector2D vector) {
        xUnits += vector.xUnits;
        yUnits += vector.yUnits;
    }

    /**
     * Creates a clone of the Vector2D.
     *
     * @return The cloned Vector2D.
     */
    public Vector2D copy() {
        return new Vector2D(xUnits, yUnits);
    }

    /**
     * Gets the angle to the Vector2D.
     *
     * @return The angle to the Vector2D.
     */
    public double angle() {
        return Math.atan2(yUnits, xUnits);
    }

    /**
     * Gets the length of the Vector2D.
     *
     * @return The length of the Vector2D.
     */
    public double length() {
        return Math.sqrt(xUnits * xUnits + yUnits * yUnits);
    }

    /**
     * Gets the squared length of the Vector2D.
     *
     * @return The squared length of the Vector2D.
     */
    public double lengthSquared() {
        return xUnits * xUnits + yUnits * yUnits;
    }

    /**
     * Multiplies the Vector2D by the specified factor.
     *
     * @param factor The factor to scale the Vector2D by.
     */
    public void multiply(double factor) {
        xUnits *= factor;
        yUnits *= factor;
    }
}