package rs.macro.api.util.fx;

import rs.macro.api.util.Imaging;
import rs.macro.api.util.filter.DualFilter;
import rs.macro.api.util.filter.Filter;
import rs.macro.api.util.fx.model.Pixel;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class PixelBuilder {

    private final PixelOperator operator;

    private int sx, sy, sw, sh;
    private final DualFilter<Integer, Integer> boundFilter =
            (x, y) -> (x >= sx && x < sx + sw && y >= sy && y < sy + sh);
    private Filter<Integer> rgbFilter;
    private DualFilter<Integer, Integer> locationFilter;
    private PixelModel model;

    /**
     * Constructs a PixelBuilder with the following argument:
     *
     * @param operator The PixelOperator.
     */
    public PixelBuilder(PixelOperator operator) {
        this.operator = operator;
        clear();
    }

    /**
     * Constructs an empty PixelBuilder.
     *
     * @return A cleared PixelBuilder.
     */
    public PixelBuilder clear() {
        this.sx = 0;
        this.sy = 0;
        this.sw = GameCanvas.GAME_SIZE.width;
        this.sh = GameCanvas.GAME_SIZE.height;
        this.rgbFilter = null;
        this.locationFilter = null;
        this.model = null;
        return this;
    }

    /**
     * Constructs a PixelBuilder with the following arguments:
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param w The width.
     * @param h The height.
     * @return A PixelBuilder representation of the specified arguments.
     */
    public PixelBuilder bounds(int x, int y, int w, int h) {
        this.sx = x;
        this.sy = y;
        this.sw = w;
        this.sh = h;
        return this;
    }

    /**
     * Constructs a PixelBuilder with the following argument:
     *
     * @param bounds The bounding Rectangle.
     * @return A PixelBuilder representation using the specified Rectangle.
     */
    public PixelBuilder bounds(Rectangle bounds) {
        return bounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * Constructs a PixelBuilder with the following argument:
     *
     * @param rgbFilter The rbg filter.
     * @return A PixelBuilder representation using the specified Filter.
     */
    public PixelBuilder filter(Filter<Integer> rgbFilter) {
        this.rgbFilter = rgbFilter;
        return this;
    }

    /**
     * Constructs a PixelBuilder with the following arguments:
     *
     * @param rgb The rbg color.
     * @param avg The average color.
     * @return The PixelBuilder representation using the specified arguments.
     */
    public PixelBuilder avgFilter(int rgb, int avg) {
        this.rgbFilter = (i) -> Colors.average(i, rgb) <= avg;
        return this;
    }

    /**
     * Constructs a PixelBuilder with the following arguments:
     *
     * @param rgb The rbg color.
     * @param tol The color tolerance.
     * @return The PixelBuilder representation using the specified arguments.
     */
    public PixelBuilder tolFilter(int rgb, int tol) {
        this.rgbFilter = (i) -> Colors.tolerance(i, rgb) <= tol;
        return this;
    }

    /**
     * Constructs a PixelBuilder with the following argument:
     *
     * @param locationFilter The location DualFilter.
     * @return The PixelBuilder representation using the specified argument.
     */
    public PixelBuilder filterLocation(DualFilter<Integer, Integer> locationFilter) {
        this.locationFilter = locationFilter;
        return this;
    }

    /**
     * Constructs a PixelBuilder with the following argument:
     *
     * @param model The PixelModel to be set.
     * @return The PixelBuilder representation using the specified PixelModel.
     */
    public PixelBuilder model(PixelModel model) {
        this.model = model;
        this.rgbFilter = (i) -> Colors.tolerance(model.root.rgb, i) <= model.root.tolerance;
        this.locationFilter = (x, y) -> {
            for (Pixel pixel : model.pixels) {
                int pixelColor = operator.at(x + pixel.xOff, y + pixel.yOff);
                if (Colors.tolerance(pixel.rgb, pixelColor) > pixel.tolerance) {
                    return false;
                }
            }
            return true;
        };
        return this;
    }

    /**
     * Constructs a PixelBuilder using the following argument:
     *
     * @param ticks The number of ticks.
     * @return The PixelBuilder representation using the specified number of ticks.
     */
    public PixelBuilder rotatable(int ticks) {
        if (model == null) {
            throw new IllegalStateException("A PixelModel must be supplied.");
        }
        this.locationFilter = (x, y) -> {
            loop:
            for (int i = 0; i < 360; i += (360 / ticks)) {
                double rads = Math.toRadians(i);
                double cos = Math.cos(rads);
                double sin = Math.sin(rads);
                for (Pixel pixel : model.pixels) {
                    int bx = x + pixel.xOff;
                    int by = y + pixel.yOff;
                    bx += (int) ((bx - x) * cos - (by - y) * sin);
                    by += (int) ((bx - x) * sin + (by - y) * cos);
                    int rgb = operator.at(bx, by);
                    if (Colors.tolerance(pixel.rgb, rgb) > pixel.tolerance) {
                        continue loop;
                    }
                }
                return true;
            }
            return false;
        };
        return this;
    }

    /**
     * Queries the PixelOperator using the PixelBuilder's instance variables.
     *
     * @return A Stream of points that match the criteria of the PixelBuilder.
     */
    public Stream<Point> query() {
        return Imaging.query(operator.image(), operator.pixels(), (x, y, rgb) ->
                boundFilter.accept(x, y) && (rgbFilter == null || rgbFilter.accept(rgb)) &&
                        (locationFilter == null || locationFilter.accept(x, y)));
    }

    /**
     * Gets the first valid Point from the query.
     *
     * @return The first point of the Stream.
     */
    public Point first() {
        return query().findFirst().get();
    }

    /**
     * Gets a list of all valid points from the query.
     *
     * @return The list of valid points of the Stream.
     */
    public List<Point> all() {
        return query().collect(Collectors.toCollection(ArrayList::new));
    }
}
