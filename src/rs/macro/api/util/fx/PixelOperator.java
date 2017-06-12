package rs.macro.api.util.fx;

import rs.macro.api.util.Imaging;

import java.awt.*;
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
     * Gets the pixels within the given region.
     *
     * @param rx The x-coordinate.
     * @param ry The y-coordinate.
     * @param rw The width.
     * @param rh THe Height.
     * @return The pixels within the given region.
     */
    public int[] subPixels(int rx, int ry, int rw, int rh) {
        int[] subPixels = new int[rw * rh];
        for (int x = rx; x < rx + rw; x++) {
            for (int y = ry; y < ry + rh; y++) {
                int index = (x - rx) + (y - ry) * rw;
                subPixels[index] = Imaging.argbAt(image, pixels, x, y);
            }
        }
        return subPixels;
    }

    /**
     * Gets the pixels within the given region.
     *
     * @param bounds The region to search within.
     * @return The pixels within the given region.
     */
    public int[] subPixels(Rectangle bounds) {
        return subPixels(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * @return The PixelBuilder representation of the PixelOperator.
     */
    public PixelBuilder builder() {
        return new PixelBuilder(this);
    }
}
