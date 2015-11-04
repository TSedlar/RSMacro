import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.access.GameMenu;
import rs.macro.api.access.input.Bezier;
import rs.macro.api.access.input.Mouse;
import rs.macro.api.access.minimap.Minimap;
import rs.macro.api.util.Arithmetic;
import rs.macro.api.util.Random;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.fx.Colors;
import rs.macro.api.util.fx.MousePaint;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.api.util.fx.listener.PixelListener;
import rs.macro.api.util.fx.model.PixelModel;

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

    private List<Point> points = new ArrayList<>();

    @Override
    public void atStart() {
    }

    @Override
    public int loop() {
        List<Point> points = operator().builder()
                .bounds(206, 390, 103, 22)
                .tolFilter(Colors.hexToRGB("#000000"), 2)
                .all();
        this.points.clear();
        this.points.addAll(points);
        System.out.println(PixelModel.fromPoints(points, 2));
        return 0;
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        MousePaint.drawMouseWaves(g, MOUSE_WAVE);
        MousePaint.drawTrail(g, MOUSE_TRAIL);
        MousePaint.drawOval(g, MOUSE_OUTER, MOUSE_INNER);
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
