package rs.macro.api.util.fx;

import rs.macro.api.methods.RuneScape;
import rs.macro.api.methods.input.Mouse;
import rs.macro.api.util.Time;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class MousePaint {

    private static Point previous = null;
    private static final List<MouseTrailPoint> TRAIL = new ArrayList<>();
    private static final ArrayList<MouseWave> WAVES = new ArrayList<>();
    private static final BasicStroke OVAL_STROKE = new BasicStroke(2);

    /**
     * Draws an oval mouse.
     *
     * @param g     The Graphics2D object to draw with.
     * @param outer The oval's outer color.
     * @param inner The oval's inner color.
     */
    public static void drawOval(Graphics2D g, Color outer, Color inner) {
        int mx = Mouse.x(), my = Mouse.y();
        g.setStroke(OVAL_STROKE);
        g.setColor(outer);
        g.drawOval(mx - 5, my - 5, 10, 10);
        g.setColor(inner);
        g.fillOval((int) (mx - 0.5D), (int) (my - 0.5D), 3, 3);
    }

    /**
     * Draws mouse waves.
     *
     * @param g     The Graphics2D object to draw with.
     * @param color The color to be set.
     */
    public static void drawMouseWaves(Graphics2D g, Color color) {
        Point mouse = Mouse.location();
        long pressTime = (Time.millis() - Mouse.pressTime());
        if (pressTime > 0 && pressTime < 100) {
            if (WAVES.size() > 0) {
                MouseWave previous = WAVES.get(WAVES.size() - 1);
                if (WAVES.size() < 5 && previous != null && previous.location().distance(mouse) >= 5) {
                    WAVES.add(new MouseWave(color, mouse, 10));
                }
            } else {
                WAVES.add(new MouseWave(color, mouse, 10));
            }
        }
        List<MouseWave> fails = WAVES.stream().filter(wave -> wave != null && !wave.draw(g))
                .collect(Collectors.toList());
        WAVES.removeAll(fails);
    }

    /**
     * Draws a mouse trail.
     *
     * @param g     The Graphics2D object to draw with.
     * @param color The mouse trail's color.
     */
    public static void drawTrail(Graphics2D g, Color color) {
        Point mouse = Mouse.location();
        List<MouseTrailPoint> keeps = TRAIL.stream().filter(p -> p != null && p.draw(g))
                .collect(Collectors.toList());
        TRAIL.clear();
        TRAIL.addAll(keeps);
        if (previous != null) {
            TRAIL.add(new MouseTrailPoint(color, previous, mouse));
        }
        previous = mouse;
    }

    private static class MouseWave {

        private static final BasicStroke WAVE_STROKE = new BasicStroke(2);
        private static final double SIZE = 10D;
        private static final double ALPHA_STEP = (255D / SIZE);

        private final Color color;
        private final Point point;

        private int size;
        private int alpha = 255;

        /**
         * Constructs a MouseWave with the following arguments:
         *
         * @param color     The color to be set.
         * @param point     The wave's location.
         * @param startSize The starting size of the wave.
         */
        public MouseWave(Color color, Point point, int startSize) {
            this.color = color;
            this.point = point;
            this.size = startSize;
        }

        /**
         * @return The location of the wave.
         */
        public Point location() {
            return point;
        }

        /**
         * Draws a MouseWave.
         *
         * @param g The Graphics2D object to draw with.
         * @return <t>true</t> if the wave was drawn; otherwise, <t>false</t>.
         */
        public boolean draw(Graphics2D g) {
            if (!RuneScape.validatePoint(point)) {
                return false;
            }
            if (alpha > 15) {
                g.setStroke(WAVE_STROKE);
                g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                size += 3.5;
                g.drawOval(point.x - size / 2, point.y - size / 2, size, size);
                alpha -= ALPHA_STEP;
                return true;
            }
            return false;
        }
    }

    private static class MouseTrailPoint {

        private static final double SIZE = 10D;
        private static final double ALPHA_STEP = (255D / SIZE);
        private static final BasicStroke TRAIL_STROKE = new BasicStroke(2);

        private final Color color;
        private final Point start;
        private final Point end;

        private int alpha = 255;

        /**
         * Constructs a MouseTrailPoint with the following arguments:
         *
         * @param color The color to be set.
         * @param start The starting Point.
         * @param end   The end Point.
         */
        public MouseTrailPoint(Color color, Point start, Point end) {
            this.color = color;
            this.start = start;
            this.end = end;
        }

        /**
         * Draws the MouseTrailPoint.
         *
         * @param g The Graphics2D object to draw with.
         * @return <t>true</t> if the MouseTrailPoint was drawn; otherwise, <t>false</t>.
         */
        public boolean draw(Graphics2D g) {
            if (!RuneScape.validatePoint(start) || !RuneScape.validatePoint(end)) {
                return false;
            }
            if (alpha > 15) {
                g.setStroke(TRAIL_STROKE);
                g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                g.drawLine(start.x, start.y, end.x, end.y);
                alpha -= ALPHA_STEP;
                return true;
            }
            return false;
        }
    }
}
