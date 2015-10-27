package rs.macro.api.access.input;

import rs.macro.RSMacro;
import rs.macro.api.access.RuneScape;
import rs.macro.api.util.Random;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.Shapes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Mouse {

    public static final int DEFAULT_SPEED = 2;

    private static Point target;
    private static List<Point> path = new ArrayList<>();

    private static int speed = DEFAULT_SPEED;

    /**
     * @return The mouse's target Point.
     */
    public static Point target() {
        return target;
    }

    /**
     * @return The mouse's Point path.
     */
    public static List<Point> path() {
        return path;
    }

    /**
     * Sets the mouse's speed.
     *
     * @param speed The speed to be set.
     */
    public static void setSpeed(int speed) {
        Mouse.speed = speed;
    }

    /**
     * @return The x coordinate of the EventDispatcher.
     */
    public static int x() {
        return RSMacro.instance().dispatcher().mouseX;
    }

    /**
     * @return The y coordinate of the EventDispatcher.
     */
    public static int y() {
        return RSMacro.instance().dispatcher().mouseY;
    }

    /**
     * @return The Point representation of the EventDispatcher's x and y coordinates.
     */
    public static Point location() {
        return new Point(x(), y());
    }

    /**
     * @return The press time between the EventDispatcher's mouse clicks.
     */
    public static long pressTime() {
        return RSMacro.instance().dispatcher().pressTime;
    }

    /**
     * Hops the mouse to the specified coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public static void hop(int x, int y) {
        RSMacro.instance().dispatcher().moveMouse(x, y);
    }

    /**
     * Hops the mouse to the specified Point.
     *
     * @param p The point to hop to.
     */
    public static void hop(Point p) {
        hop(p.x, p.y);
    }

    /**
     * Presses the mouse according to the specified boolean.
     *
     * @param left <t>true</t> to left click; otherwise, <t>false</t>.
     */
    public static void press(boolean left) {
        RSMacro.instance().dispatcher().pressMouse(left);
    }

    /**
     * Moves the mouse to the specified coordinates and clicks the mouse.
     *
     * @param x    The x coordinate.
     * @param y    The y coordinate.
     * @param left <t>true</t> to left click; otherwise, <t>false</t>.
     */
    public static void press(int x, int y, boolean left) {
        move(x, y);
        press(left);
    }

    /**
     * Presses the mouse at the specified Point.
     *
     * @param p    The Point to move to.
     * @param left <t>true</t> to left click; otherwise, <t>false</t>.
     */
    public static void press(Point p, boolean left) {
        press(p.x, p.y, left);
    }

    /**
     * Releases the mouse button.
     *
     * @param left <t>true</t> to release the left mouse button; otherwise, <t>false</t>.
     */
    public static void release(boolean left) {
        RSMacro.instance().dispatcher().releaseMouse(left);
    }

    /**
     * Clicks the mouse.
     *
     * @param left <t>true</t> to left click; otherwise, <t>false</t>.
     */
    public static void click(boolean left) {
        RSMacro.instance().dispatcher().clickMouse(left);
    }

    /**
     * Clicks the mouse at the specified coordinates.
     *
     * @param x    The x coordinate.
     * @param y    The y coordinate.
     * @param left <t>true</t> to left click; otherwise, <t>false</t>.
     */
    public static void click(int x, int y, boolean left) {
        move(x, y);
        click(left);
    }

    /**
     * Clicks the mouse at the specified Point.
     *
     * @param p    The point.
     * @param left <t>true</t> to left click; otherwise, <t>false</t>.
     */
    public static void click(Point p, boolean left) {
        click(p.x, p.y, left);
    }

    /**
     * Clicks the mouse within the specified Shape.
     *
     * @param shape The Shape.
     * @param left  <t>true</t> to left click; otherwise, <t>false</t>.
     */
    public static void click(Shape shape, boolean left) {
        click(Random.nextElement(Shapes.pointsFor(shape)), left);
    }

    /**
     * Scrolls the mouse.
     *
     * @param up     <t>true</t> to scroll up; otherwise, <t>false</t>.
     * @param clicks The number of clicks to scroll the mouse.
     */
    public static void scroll(boolean up, int clicks) {
        RSMacro.instance().dispatcher().scrollMouse(up, clicks);
    }

    /**
     * Scrolls the mouse.
     *
     * @param up <t>true</t> to scroll up; otherwise, <t>false</t>.
     */
    public static void scroll(boolean up) {
        scroll(up, 1);
    }

    /**
     * Moves the mouse to the specified coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public static void move(int x, int y) {
        if (!RuneScape.validatePoint(x, y)) {
            return;
        }
        path = Bezier.generate(x, y);
        if (!path.isEmpty()) {
            target = path.get(path.size() - 1);
        }
        path.stream().filter(p -> RuneScape.validatePoint(p.x, p.y)).forEach(p -> {
            hop(p.x, p.y);
            Time.sleep(speed);
        });
        target = null;
        path.clear();
    }

    /**
     * Moves the mouse to the specified Point.
     *
     * @param p The point.
     */
    public static void move(Point p) {
        move(p.x, p.y);
    }

    /**
     * Moves the mouse to the specified Shape.
     *
     * @param shape The Shape.
     */
    public static void move(Shape shape) {
        move(Random.nextElement(Shapes.pointsFor(shape)));
    }
}
