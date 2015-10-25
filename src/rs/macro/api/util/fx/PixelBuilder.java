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
     * Creates a default PixelBuilder with the given PixelOperator.
     *
     * @param operator The PixelOperator.
     */
    public PixelBuilder(PixelOperator operator) {
        this.operator = operator;
        clear();
    }

    /**
     * Clears out this PixelBuilder.
     *
     * @return This PixelBuilder with the default variables.
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
     * Allows this PixelBuilder to only query within the given bounds.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param w The width.
     * @param h The height.
     * @return This PixelBuilder chained with the given bounds.
     */
    public PixelBuilder bounds(int x, int y, int w, int h) {
        this.sx = x;
        this.sy = y;
        this.sw = w;
        this.sh = h;
        return this;
    }

    /**
     * Allows this PixelBuilder to only query within the given bounds.
     *
     * @param bounds The bounding Rectangle.
     * @return This PixelBuilder chained with the given bounds.
     */
    public PixelBuilder bounds(Rectangle bounds) {
        return bounds(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * Allows this PixelBuilder to only query with the given filter.
     *
     * @param rgbFilter The rbg filter.
     * @return This PixelBuilder chained with the given filter.
     */
    public PixelBuilder filter(Filter<Integer> rgbFilter) {
        this.rgbFilter = rgbFilter;
        return this;
    }

    /**
     * Allows this PixelBuilder to only query pixels matching the given arguments.
     *
     * @param rgb The rbg color.
     * @param avg The average color distance.
     * @return This PixelBuilder chained with a filter matching the given arguments.
     */
    public PixelBuilder avgFilter(int rgb, int avg) {
        this.rgbFilter = (i) -> Colors.average(i, rgb) <= avg;
        return this;
    }

    /**
     * Allows this PixelBuilder to only query pixels matching the given arguments.
     *
     * @param rgb The rbg color.
     * @param tol The color tolerance distance.
     * @return This PixelBuilder chained with a filter matching the given arguments.
     */
    public PixelBuilder tolFilter(int rgb, int tol) {
        this.rgbFilter = (i) -> Colors.tolerance(i, rgb) <= tol;
        return this;
    }

    /**
     * Allows this PixelBuilder to only query pixels matching the given filter.
     *
     * @param locationFilter The location DualFilter.
     * @return This PixelBuilder chained with the given filter.
     */
    public PixelBuilder filterLocation(DualFilter<Integer, Integer> locationFilter) {
        this.locationFilter = locationFilter;
        return this;
    }

    /**
     * Allows this PixelBuilder to only query pixels matching the given PixelModel.
     *
     * @param model The PixelModel to be set.
     * @return This PixelBuilder chained with a filter matching the given PixelModel.
     */
    public PixelBuilder model(PixelModel model) {
        this.model = model;
        this.rgbFilter = (i) -> model != null && Colors.tolerance(model.root.rgb, i) <=
                model.root.tolerance;
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
     * @param ticks The number of ticks. (loops 360/ticks)
     * @return This chained PixelBuilder allowing the PixelModel to be rotatable while querying.
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
        return query().findFirst().orElse(null);
    }

    /**
     * Gets a List of all valid points from the query.
     *
     * @return The List of valid points of the Stream.
     */
    public List<Point> all() {
        return query().collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Obtains the median RGB value
     *
     * @return The median RGB value.
     */
    public int median() {
        int area = sw * sh;
        int alpha = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        for (Point p : all()) {
            int rgb = operator.at(p.x, p.y);
            alpha += ((rgb >> 24) & 0xFF);
            red += ((rgb) & 0xFF);
            green += ((rgb >> 8) & 0xFF);
            blue += ((rgb >> 16) & 0xFF);
        }
        return (((alpha / area) << 24) | ((red / area) << 16) | ((green / area) << 8) |
                ((blue / area)));
    }
}
