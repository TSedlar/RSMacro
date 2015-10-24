import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.methods.Environment;
import rs.macro.api.methods.RuneScape;
import rs.macro.api.methods.input.Mouse;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.fx.Colors;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.api.util.fx.Text;
import rs.macro.api.util.fx.listener.PixelListener;
import rs.macro.api.util.fx.model.Pixel;
import rs.macro.api.util.fx.model.PixelModel;
import rs.macro.util.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 4/30/2015
 */
@Manifest(author = "Tyler", name = "Color Inspector",
        description = "Helps gather data for macros",
        version = "1.0.0", banks = false)
public class ColorInspector extends Macro implements Renderable, PixelListener {

    private static final BasicStroke STROKE = new BasicStroke(2);
    private static final int DEFAULT_TOLERANCE = 10;
    private static final Rectangle LABEL_BOUNDS = new Rectangle(7, 460, 506, 14);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 9);

    private boolean bounding, modeling, viewingModel;
    private int boundsX = -1, boundsY = -1;
    private int boundsWidth = -1, boundsHeight = -1;

    private final List<Pixel> pixels = Collections.synchronizedList(new LinkedList<>());
    private int pixelRootX = -1, pixelRootY = -1;

    private final List<Point> modelMatches = Collections.synchronizedList(new LinkedList<>());

    @Override
    public void atStart() {
        Environment.setInput(Environment.INPUT_BOTH);
    }

    private PixelModel createPixelModel() {
        Pixel root = pixels.get(0);
        Pixel[] pixelArray = pixels.toArray(new Pixel[pixels.size()]);
        Pixel[] trimmedPixelArray = new Pixel[pixelArray.length - 1];
        System.arraycopy(pixelArray, 1, trimmedPixelArray, 0, trimmedPixelArray.length);
        return new PixelModel(root, trimmedPixelArray);
    }

    @Override
    public void onPixelsUpdated(PixelOperator operator) {
        if (viewingModel && !pixels.isEmpty()) {
            PixelModel model = createPixelModel();
            List<Point> points = operator.builder().model(model).all();
            if (points != null) {
                modelMatches.clear();
                modelMatches.addAll(points);
            }
        } else {
            modelMatches.clear();
        }
    }

    @Override
    public int loop() {
        return 1000;
    }

    @Override
    public void render(Graphics2D g) {
        if (bounding && boundsX >= 0 && boundsY >= 0 && boundsWidth > 0 && boundsHeight > 0) {
            g.setColor(Color.WHITE);
            g.setStroke(STROKE);
            g.drawRect(boundsX, boundsY, boundsWidth, boundsHeight);
        }
        if (modeling && !pixels.isEmpty()) {
            int idx = 0;
            for (Pixel pixel : pixels) {
                g.setColor(idx++ == 0 ? Color.GREEN : Color.RED);
                g.fillOval((pixelRootX + pixel.xOff) - 2, (pixelRootY + pixel.yOff) - 2, 4, 4);
            }
            if (viewingModel) {
                g.setColor(Color.WHITE);
                modelMatches.stream().filter(p -> p != null).forEach(p -> {
                    g.drawLine(p.x - 6, p.y - 6, p.x + 6, p.y + 6);
                    g.drawLine(p.x - 6, p.y + 6, p.x + 6, p.y - 6);
                });
            }
        }
        int mouseColor = RuneScape.rgbAt(Mouse.x(), Mouse.y());
        String string = String.format("%s %s %s", Mouse.x(), Mouse.y(), Colors.rgbToHex(mouseColor));
        Text.drawRuneString(g, string, 510 - g.getFontMetrics().stringWidth(string), 334,
                new Color(mouseColor));
        g.setColor(Color.BLACK);
        g.fill(LABEL_BOUNDS);
        g.setColor(Color.WHITE);
        g.setFont(LABEL_FONT);
        String info = "CTRL (C - COLOR) (P - POINT) (M - MODEL) (T - TEST MODEL) (S - SCREENSHOT) (B - BOUNDS)";
        int infoWidth = g.getFontMetrics().stringWidth(info);
        g.drawString(info, (LABEL_BOUNDS.x + (LABEL_BOUNDS.width / 2)) - infoWidth / 2, LABEL_BOUNDS.y + 10);
        g.setColor(Color.BLACK);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() || e.isMetaDown()) {
            if (e.getKeyCode() == KeyEvent.VK_C) {
                int mouseColor = RuneScape.rgbAt(Mouse.x(), Mouse.y());
                Environment.copyToClipboard(Colors.rgbToHex(mouseColor));
            } else if (e.getKeyCode() == KeyEvent.VK_P) {
                Environment.copyToClipboard(String.format("%s, %s", Mouse.x(), Mouse.y()));
            } else if (e.getKeyCode() == KeyEvent.VK_M) {
                if (modeling) {
                    PixelModel model = createPixelModel();
                    System.out.println(model);
                    Environment.copyToClipboard(model.toString());
                    pixelRootX = -1;
                    pixelRootY = -1;
                    pixels.clear();
                }
                modeling = !modeling;
            } else if (e.getKeyCode() == KeyEvent.VK_T) {
                viewingModel = !viewingModel;
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                try {
                    ImageIO.write(RuneScape.image(), "png", new File(Configuration.HOME + "image.png"));
                } catch (IOException err) {
                    err.printStackTrace();
                }
            } else {
                bounding = e.getKeyCode() == KeyEvent.VK_B;
            }
        } else {
            bounding = false;
            boundsWidth = -1;
            boundsHeight = -1;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (bounding) {
            e.consume();
            boundsX = e.getX();
            boundsY = e.getY();
        }
        if (modeling) {
            e.consume();
            int x = e.getX(), y = e.getY();
            int color = RuneScape.rgbAt(x, y);
            int offsetX = 0, offsetY = 0;
            if (!pixels.isEmpty()) {
                offsetX = x - pixelRootX;
                offsetY = y - pixelRootY;
            } else {
                pixelRootX = x;
                pixelRootY = y;
            }
            pixels.add(new Pixel(color, DEFAULT_TOLERANCE, offsetX, offsetY));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (bounding) {
            boundsWidth = e.getX() - boundsX;
            boundsHeight = e.getY() - boundsY;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (bounding && boundsX >= 0 && boundsY >= 0 && boundsWidth > 0 && boundsHeight > 0) {
            String bounds = String.format("%s, %s, %s, %s", boundsX, boundsY, boundsWidth, boundsHeight);
            System.out.println(bounds);
            Environment.copyToClipboard(bounds);
            bounding = false;
            boundsX = -1;
            boundsY = -1;
            boundsWidth = -1;
            boundsHeight = -1;
        }
    }
}