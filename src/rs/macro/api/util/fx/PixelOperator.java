package rs.macro.api.util.fx;

import rs.macro.api.util.Imaging;

import java.awt.image.BufferedImage;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class PixelOperator {

    private BufferedImage image;
    private int[] pixels;
    private PixelBuilder builder;

    public void setData(BufferedImage image, int[] pixels) {
        this.image = image;
        this.pixels = pixels;
    }

    public BufferedImage image() {
        return image;
    }

    public int[] pixels() {
        return pixels;
    }

    public int at(int x, int y) {
        return Imaging.argbAt(image, pixels, x, y);
    }

    public PixelBuilder builder() {
        return (builder != null ? builder.clear() : (builder = new PixelBuilder(this)));
    }
}
