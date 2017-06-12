package rs.macro.api.util.fx;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Tyler Sedlar
 * @since 6/11/17
 */
public class PolyTool {

    public static void drawPolyAt(Graphics2D g, Polygon poly, int x, int y) {
        for (int n = 0; n < poly.npoints; n++) {
            int px = (x + poly.xpoints[n]);
            int py = (y + poly.ypoints[n]);
            g.drawLine(px, py, px, py);
        }
    }

    public static BufferedImage toImage(Polygon poly) throws Exception {
        Rectangle bounds = poly.getBounds();
        System.out.println(bounds);
        BufferedImage img = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(Color.WHITE);
        drawPolyAt(g, poly, Math.abs(bounds.x), Math.abs(bounds.y));
        return img;
    }

    public static boolean hasN(Polygon poly, int n) {
        return n < poly.npoints;
    }

    public static boolean hasPoint(Polygon poly, int x, int y) {
        for (int n = 0; n < poly.npoints; n++) {
            if (poly.xpoints[n] ==x && poly.ypoints[n] == y) {
                return true;
            }
        }
        return false;
    }

    public static double similarity(Polygon a, Polygon b) {
        if (a == null || b == null) {
            return 0D;
        }
        int max = Math.max(a.npoints, b.npoints);
        int matched = 0;
        for (int n = 0; n < max; n++) {
            if (hasN(a, n) && hasN(b, n)) {
                int aX = a.xpoints[n];
                int aY = a.ypoints[n];
                if (hasPoint(b, aX, aY)) {
                    matched++;
                }
            }
        }
        return ((double) matched) / ((double) max);
    }
}
