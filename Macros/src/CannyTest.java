import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.access.Inventory;
import rs.macro.api.access.RuneScape;
import rs.macro.api.access.Slots;
import rs.macro.api.util.Imaging;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.fx.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyler Sedlar
 * @since 6/11/17
 */
@Manifest(name = "Canny Test", author = "Tyler", description = "Testing Canny Edge Detector",
        version = "1.0.0", banks = false)
public class CannyTest extends Macro implements Renderable {

    private Map<Rectangle, CannyEdgeModel> slots = new ConcurrentHashMap<>();
    private Map<Rectangle, Color> medians = new ConcurrentHashMap<>();

    private Polygon cabbage;

    private CannyEdgeModel modelOf(Rectangle bounds) {
        BufferedImage subImg = RuneScape.image().getSubimage(bounds.x, bounds.y, bounds.width, bounds.height);
        return CannyEdgeDetector.model(subImg, 2.5F, 7.5F, 0.5F, 32);
    }

    private Color median(Rectangle bounds) {
        int[] pixels = operator().subPixels(bounds);
        return new Color(Colors.median(pixels, rgb -> Colors.tolerance(rgb, Slots.INV_BACKGROUND_RGB) > 15));
    }

    @Override
    public void atStart() {
        CannyEdgeModel.loadPolyEMF("./test/models/cabbage-top.emf")
                .ifPresent(poly -> cabbage = poly);
    }

    @Override
    public int loop() {
        Map<Rectangle, CannyEdgeModel> slots = new HashMap<>();
        Map<Rectangle, Color> medians = new HashMap<>();
        long start = System.nanoTime();
        for (Rectangle slot : Inventory.SLOTS) {
            CannyEdgeModel model = modelOf(slot);
            slots.put(slot, model);
            medians.put(slot, median(slot));
//            if (model.matchesEMF(cabbage, 0.95D)) {
//                slots.put(slot, model);
//                medians.put(slot, median(slot));
//            }
        }
        long end = System.nanoTime();
        System.out.printf("Found cabbage in %.02f seconds\n", (end - start) / 1e9);
        this.slots.clear();
        this.medians.clear();
        this.slots.putAll(slots);
        this.medians.putAll(medians);
        return 50;
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        slots.forEach((slot, model) -> {
            Point start = model.start();
            if (start != null) {
                model.toPixelModel().ifPresent(pixels -> {
                    g.setColor(Color.BLACK);
                    g.fill(slot);
                    Polygon poly = PolyTool.translate(pixels.toPolygon(), slot.x + start.x, slot.y + start.y);
                    g.setColor(medians.get(slot));
                    for (int n = 0; n < poly.npoints; n++) {
                        g.drawLine(poly.xpoints[n], poly.ypoints[n], poly.xpoints[n], poly.ypoints[n]);
                    }
                });
            }
        });
    }
}
