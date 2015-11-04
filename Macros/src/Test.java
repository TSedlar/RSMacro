import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.access.GameMenu;
import rs.macro.api.access.input.Bezier;
import rs.macro.api.access.input.Mouse;
import rs.macro.api.access.minimap.Minimap;
import rs.macro.api.util.Arithmetic;
import rs.macro.api.util.Random;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.fx.MousePaint;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.api.util.fx.listener.PixelListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
@Manifest(name = "Test", author = "Tyler", description = "For debugging purposes",
        version = "1.0.0", banks = false)
public class Test extends Macro implements Renderable, PixelListener {

    private static final Color MOUSE_OUTER = new Color(200, 85, 70);
    private static final Color MOUSE_INNER = new Color(240, 245, 45);
    private static final Color MOUSE_WAVE = new Color(240, 245, 45);
    private static final Color MOUSE_TRAIL = new Color(200, 85, 70);

    @Override
    public void atStart() {
    }

    List<Point> points = new ArrayList<>();
    Point rand = new Point(400, 400);
    Point cp1, cp2;

    @Override
    public int loop() {
        int dist = (int) rand.distance(Mouse.x(), Mouse.y());
        float angle = Arithmetic.angleBetween(Mouse.x(), Mouse.y(), rand.x, rand.y);
        int angOff = (rand.x > Mouse.x() ? -6 : 6);
        int cp1Dist = (int) (dist * 0.3D);
        cp1 = Arithmetic.polarFrom(Mouse.x(), Mouse.y(), angle + angOff, cp1Dist);
        int cp2Dist = (int) (dist * 0.7D);
        cp2 = Arithmetic.polarFrom(Mouse.x(), Mouse.y(), angle - angOff, cp2Dist);
        List<Point> points = Bezier.generate(rand.x, rand.y, cp1, cp2);
        this.points.clear();
        this.points.addAll(points);
        return 0;
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        MousePaint.drawMouseWaves(g, MOUSE_WAVE);
        MousePaint.drawTrail(g, MOUSE_TRAIL);
        MousePaint.drawOval(g, MOUSE_OUTER, MOUSE_INNER);
        if (rand != null) {
            g.setColor(Color.RED);
            g.fillOval(rand.x - 4, rand.y - 4, 8, 8);
        }
        if (cp1 != null) {
            g.setColor(Color.ORANGE);
            g.fillOval(cp1.x - 4, cp1.y - 4, 8, 8);
        }
        if (cp2 != null) {
            g.setColor(Color.BLUE);
            g.fillOval(cp2.x - 4, cp2.y - 4, 8, 8);
        }
        g.setColor(Color.CYAN);
        Point[] array = points.toArray(new Point[points.size()]);
        for (Point p : array) {
            if (p == null) {
                continue;
            }
            g.fillOval(p.x, p.y , 1, 1);
        }
    }

    @Override
    public void onPixelsUpdated(PixelOperator operator) {

    }
}
