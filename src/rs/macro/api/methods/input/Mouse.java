package rs.macro.api.methods.input;

import rs.macro.RSMacro;
import rs.macro.api.methods.RuneScape;
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

    public static Point target() {
        return target;
    }

    public static List<Point> path() {
        return path;
    }

    public static void setSpeed(int speed) {
        Mouse.speed = speed;
    }

    public static int x() {
        return RSMacro.instance().dispatcher().mouseX;
    }

    public static int y() {
        return RSMacro.instance().dispatcher().mouseY;
    }

    public static Point location() {
        return new Point(x(), y());
    }

    public static long pressTime() {
        return RSMacro.instance().dispatcher().pressTime;
    }

    public static void hop(int x, int y) {
        RSMacro.instance().dispatcher().moveMouse(x, y);
    }

    public static void hop(Point p) {
        hop(p.x, p.y);
    }

    public static void press(boolean left) {
        RSMacro.instance().dispatcher().pressMouse(left);
    }

    public static void press(int x, int y, boolean left) {
        move(x, y);
        press(left);
    }

    public static void press(Point p, boolean left) {
        press(p.x, p.y, left);
    }

    public static void release(boolean left) {
        RSMacro.instance().dispatcher().releaseMouse(left);
    }

    public static void click(boolean left) {
        RSMacro.instance().dispatcher().clickMouse(left);
    }

    public static void click(int x, int y, boolean left) {
        move(x, y);
        click(left);
    }

    public static void click(Point p, boolean left) {
        click(p.x, p.y, left);
    }

    public static void click(Shape shape, boolean left) {
        List<Point> points = Shapes.pointsFor(shape);
        Point p = Random.nextElement(points);
        click(p, left);
    }

    public static void scroll(boolean up, int clicks) {
        RSMacro.instance().dispatcher().scrollMouse(up, clicks);
    }

    public static void scroll(boolean up) {
        scroll(up, 1);
    }

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
}
