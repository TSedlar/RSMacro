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

    /**
     * Sets data for the PixelOperator.
     *
     * @param image  The BufferedImage to be set.
     * @param pixels The pixel array.
     */
    public void setData(BufferedImage image, int[] pixels) {
        this.image = image;
        this.pixels = pixels;
    }

    /**
     * @return The BufferedImage.
     */
    public BufferedImage image() {
        return image;
    }

    /**
     * @return The pixel array.
     */
    public int[] pixels() {
        return pixels;
    }

    /**
     * Gets the argb color at the specified coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The argb color at the specified coordinates.
     */
    public int at(int x, int y) {
        return Imaging.argbAt(image, pixels, x, y);
    }

    /**
     * @return The PixelBuilder representation of the PixelOperator.
     */
    public PixelBuilder builder() {
        return new PixelBuilder(this);
    }
}
