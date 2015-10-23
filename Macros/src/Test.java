import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.methods.Bank;
import rs.macro.api.methods.Camera;
import rs.macro.api.methods.Minimap;
import rs.macro.api.methods.RuneScape;
import rs.macro.api.methods.input.Keyboard;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.MousePaint;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.api.util.fx.Text;
import rs.macro.api.util.fx.listener.PixelListener;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
@Manifest(name = "Test", author = "Tyler", description = "For debugging purposes", banks = true)
public class Test extends Macro implements Renderable, PixelListener {

    @Override
    public void atStart() {
        Camera.setPitch(false);
    }

    @Override
    public int loop() {
        if (!Bank.viewing()) {
            if (Bank.open()) {
                System.out.println("OPENED BANK");
            }
        } else {
            System.out.println("OPENED BANK ALREADY");
        }
        return 1000;
    }

    private static final Color MOUSE_OUTER = new Color(200, 85, 70);
    private static final Color MOUSE_INNER = new Color(240, 245, 45);
    private static final Color MOUSE_WAVE = new Color(240, 245, 45);
    private static final Color MOUSE_TRAIL = new Color(200, 85, 70);

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        MousePaint.drawMouseWaves(g, MOUSE_WAVE);
        MousePaint.drawTrail(g, MOUSE_TRAIL);
        MousePaint.drawOval(g, MOUSE_OUTER, MOUSE_INNER);
    }

    @Override
    public void onPixelsUpdated(PixelOperator operator) {

    }
}
