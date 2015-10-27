import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.access.Bank;
import rs.macro.api.access.Inventory;
import rs.macro.api.util.Random;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.fx.MousePaint;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.api.util.fx.Text;
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

    private int[] bankIds = new int[Bank.SLOTS.length];
    private int[] invIds = new int[Inventory.SLOTS.length];

    @Override
    public void atStart() {
    }

    @Override
    public int loop() {
        for (int i = 0; i < bankIds.length; i++) {
            bankIds[i] = Bank.idAt(i);
        }
        for (int i = 0; i < invIds.length; i++) {
            invIds[i] = Inventory.idAt(i);
        }
        return Random.nextInt(50, 100);
    }

    private static final Color SHADE = new Color(40, 40, 40, 100);

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        MousePaint.drawMouseWaves(g, MOUSE_WAVE);
        MousePaint.drawTrail(g, MOUSE_TRAIL);
        MousePaint.drawOval(g, MOUSE_OUTER, MOUSE_INNER);
        for (int i = 0; i < bankIds.length; i++) {
            Rectangle bounds = Bank.SLOTS[i];
            g.setColor(SHADE);
            g.fillRect(bounds.x, bounds.y + 8, bounds.width, 15);
            String text = Integer.toString(bankIds[i]);
            int textWidth = g.getFontMetrics().stringWidth(text);
            Text.drawRuneString(g, text, (bounds.x + (bounds.width / 2)) - (textWidth / 2),
                    bounds.y + 20, Color.YELLOW);
            g.setColor(Color.YELLOW);
//            g.draw(bounds);
        }
        for (int i = 0; i < bankIds.length; i++) {
            Rectangle bounds = Bank.SLOTS[i];
            g.setColor(SHADE);
            g.fillRect(bounds.x, bounds.y + 8, bounds.width, 15);
            String text = Integer.toString(bankIds[i]);
            int textWidth = g.getFontMetrics().stringWidth(text);
            Text.drawRuneString(g, text, (bounds.x + (bounds.width / 2)) - (textWidth / 2),
                    bounds.y + 20, Color.YELLOW);
            g.setColor(Color.YELLOW);
//            g.draw(bounds);
        }
    }

    @Override
    public void onPixelsUpdated(PixelOperator operator) {

    }
}
