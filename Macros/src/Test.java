import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.access.GameMenu;
import rs.macro.api.access.input.Keyboard;
import rs.macro.api.util.Random;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.MousePaint;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.api.util.fx.listener.PixelListener;

import java.awt.*;

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

    private Rectangle bounds;

    @Override
    public void atStart() {
    }

    @Override
    public int loop() {
//        bounds = GameMenu.bounds();
        Keyboard.typeKey('A');
        Time.sleep(1000);
        return Random.nextInt(50, 100);
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        MousePaint.drawMouseWaves(g, MOUSE_WAVE);
        MousePaint.drawTrail(g, MOUSE_TRAIL);
        MousePaint.drawOval(g, MOUSE_OUTER, MOUSE_INNER);
        if (bounds != null) {
            g.setColor(Color.GREEN);
            g.draw(bounds);
        }
    }

    @Override
    public void onPixelsUpdated(PixelOperator operator) {

    }
}
