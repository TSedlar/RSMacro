package rs.macro.api.util.fx;

import rs.macro.api.util.fx.model.Pixel;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tyler Sedlar
 * @since 6/11/2017
 */
public class CannyEdgeModel {

    private final int width, height;
    private final int[] data;

    private BufferedImage image;
    private PixelModel model;

    /**
     * Creates a CannyEdgeModel with the given arguments.
     *
     * @param width  The width of the image.
     * @param height The height of the image.
     * @param data   The one dimensional pixel array.
     */
    public CannyEdgeModel(int width, int height, int[] data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    /**
     * Creates a BufferedImage from this model.
     *
     * @return A BufferedImage of this model.
     */
    public BufferedImage toImage() {
        if (image == null) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        image.getWritableTile(0, 0).setDataElements(0, 0, width, height, data);
        return image;
    }

    /**
     * Gets the RGB value at the given coordinate.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return The RGB value at the given coordinate.
     */
    public int rgbAt(int x, int y) {
        return data[x + (y * width)];
    }

    /**
     * The starting point where this model has an edge.
     * Since the PixelModel contains only edges, this can be used to draw the model.
     *
     * @return The point where the first edge appears.
     */
    public Point start() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = rgbAt(x, y);
                if (rgb != Color.BLACK.getRGB()) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Converts this model into a PixelModel.
     *
     * @return A PixelModel of this model.
     */
    public Optional<PixelModel> toPixelModel() {
        if (model != null) {
            return Optional.of(model);
        }
        List<Pixel> pixels = new ArrayList<>();
        int rootX = -1, rootY = -1;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = rgbAt(x, y);
                if (rgb != Color.BLACK.getRGB()) {
                    if (rootX == -1 && rootY == -1) {
                        rootX = x;
                        rootY = y;
                        pixels.add(new Pixel(rgb, 0));
                    } else {
                        pixels.add(new Pixel(rgb, 0, x - rootX, y - rootY));
                    }
                }
            }
        }
        return pixels.isEmpty() ? Optional.empty() : Optional.of((model = PixelModel.fromList(pixels)));
    }

    /**
     * Checks if this model matches the given polygon with a variance of the given similarity.
     *
     * @param poly       The polygon to match.
     * @param similarity The similarity variance.
     * @return <tt>true</tt> if the polygon matches, otherwise <tt>false</tt>.
     */
    public boolean matchesEMF(Polygon poly, double similarity) {
        AtomicBoolean matches = new AtomicBoolean(false);
        toPixelModel().ifPresent(model -> matches.set(
                PolyTool.similarity(poly, model.toPolygon()) >= similarity
        ));
        return matches.get();
    }

    /**
     * Loads the given file into a Polygon object.
     *
     * @param file The file to load.
     * @return A polygon from the given file.
     */
    public static Optional<Polygon> loadPolyEMF(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return Optional.of((Polygon) ois.readObject());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Loads the given file into a Polygon object.
     *
     * @param file The file to load.
     * @return A polygon from the given file.
     */
    public static Optional<Polygon> loadPolyEMF(String file) {
        return loadPolyEMF(new File(file));
    }
}
