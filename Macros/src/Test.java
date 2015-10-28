import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.access.RuneScape;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.fx.MousePaint;
import rs.macro.api.util.fx.Palettes;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.api.util.fx.listener.PixelListener;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    private BufferedImage image = null;

    @Override
    public void atStart() {
        BufferedImage game = RuneScape.fullImage();
        image = new BufferedImage(game.getWidth(), game.getHeight(), game.getType());
    }

    @Override
    public int loop() {
        int width = RuneScape.image().getWidth(), height = RuneScape.image().getHeight();
        long start = System.nanoTime();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y,
                        Palettes.selectFromColorBlind(RuneScape.rgbAt(x, y)));
            }
        }
        long end = System.nanoTime();
        return 0;
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        MousePaint.drawMouseWaves(g, MOUSE_WAVE);
        MousePaint.drawTrail(g, MOUSE_TRAIL);
        MousePaint.drawOval(g, MOUSE_OUTER, MOUSE_INNER);
        g.drawImage(image, 0, 0, null);
    }

    @Override
    public void onPixelsUpdated(PixelOperator operator) {

    }
}
