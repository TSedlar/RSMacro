package rs.macro.api.access;

import rs.macro.api.util.filter.Filter;
import rs.macro.api.util.fx.Colors;
import rs.macro.api.util.fx.Palettes;
import rs.macro.api.util.fx.PixelBuilder;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;
import java.util.List;

/**
 * @author Jacob Doiron, Tyler Sedlar
 * @since 10/26/2015
 */
public class Slots {

    public static final int INV_BACKGROUND_RGB = Colors.hexToRGB("#494035");
    public static final int ITEM_BORDER_RGB = Colors.hexToRGB("#000001");
    public static final int INV_SHADOW_RGB = Colors.hexToRGB("#302020");
    public static final int AMOUNT_RGB = Colors.hexToRGB("#FFFF00");
    public static final Filter<Integer> ITEM_MODEL_FILTER = (rgb) ->
            Colors.tolerance(rgb, INV_BACKGROUND_RGB) > 15 &&
                    Colors.tolerance(rgb, ITEM_BORDER_RGB) > 2 &&
                    Colors.tolerance(rgb, INV_SHADOW_RGB) > 2 &&
                    Colors.tolerance(rgb, AMOUNT_RGB) > 5;

    /**
     * Creates a PixelBuilder of the given slot, excluding shadows/amount/borders.
     *
     * @param slots The slots to check.
     * @param slot  The slot index to create a PixelBuilder for.
     * @return A PixelBuilder of the given slot, excluding shadows/amount/borders.
     */
    public static PixelBuilder createPixelBuilderAt(Rectangle[] slots, int slot) {
        Rectangle bounds = slots[slot];
        return RuneScape.pixels().operator().builder()
                .bounds(bounds.x, bounds.y + (int) (bounds.height * 0.32D), bounds.width,
                        (int) (bounds.height * 0.68D))
                .filter(ITEM_MODEL_FILTER);
    }

    /**
     * Creates a PixelModel of the given slot, excluding shadows/amount/borders.
     *
     * @param slots     The slots to check.
     * @param slot      The slot index to create a PixelModel for.
     * @param tolerance The tolerance distance for each Pixel.
     * @return A PixelModel of the given slot, excluding shadows/amount/borders.
     */
    public static PixelModel createModelAt(Rectangle[] slots, int slot, int tolerance) {
        List<Point> points = createPixelBuilderAt(slots, slot).all();
        return !points.isEmpty() ? PixelModel.fromPoints(points, tolerance) : null;
    }

    /**
     * Checks if the given slot has an item or not.
     *
     * @param slots The slots to check.
     * @param rgb   The rbg color to look for.
     * @param tol   The tolerance to search with.
     * @param slot  The slot index to check for.
     * @return <t>true</t> if the given slot has an item, otherwise <t>false</t>.
     */
    public static boolean hasItem(Rectangle[] slots, int rgb, int tol, int slot) {
        return RuneScape.pixels().operator().builder()
                .bounds(slots[slot])
                .tolFilter(rgb, tol)
                .query().findAny().orElse(null) != null;
    }

    /**
     * Finds a slot matching the given PixelModel.
     *
     * @param slots The slots to check.
     * @param model The model to search for.
     * @return A slot matching the given PixelModel.
     */
    public static Rectangle findSlot(Rectangle[] slots, PixelModel model) {
        Point match = RuneScape.pixels().operator().builder()
                .filterLocation((x, y) -> {
                    for (Rectangle slot : slots) {
                        if (slot.contains(x, y)) {
                            return true;
                        }
                    }
                    return false;
                })
                .model(model)
                .first();
        if (match == null) {
            return null;
        }
        for (Rectangle slot : slots) {
            if (slot.contains(match)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Gets a visual representation ID of the given slot.
     *
     * @param slots The slots to check.
     * @param slot  The slot index to get an ID for.
     * @return A visual representation ID of the given slot.
     */
    public static int idAt(Rectangle[] slots, int slot) {
        Rectangle bounds = slots[slot];
        int yLevel = (int) (bounds.y + (bounds.height * 0.75D));
        PixelBuilder builder = RuneScape.pixels().operator().builder()
                .filterLocation((x, y) -> bounds.contains(x, y) && x >= (bounds.x + 10) &&
                                x <= (bounds.x + bounds.width - 10) &&
                                (y == yLevel || (y >= (bounds.y + 7) && y <= (bounds.y + 10)))
                )
                .filter(ITEM_MODEL_FILTER);
        int[] pixels = builder.palette(Palettes.PALETTE_64);
        if (pixels.length == 0) {
            return -1;
        }
        int median = Colors.median(pixels);
        int r = Colors.red(median), g = Colors.green(median), b = Colors.blue(median);
        return (r + g + b);
    }

    /**
     * Finds the slot with the given item id.
     *
     * @param slots The slots to check.
     * @param id    The id of the item to look for.
     * @return The slot with the given item id.
     */
    public static Rectangle findSlot(Rectangle[] slots, int id) {
        for (int i = 0; i < slots.length; i++) {
            int slotId = idAt(slots, i);
            if (id == slotId) {
                return slots[i];
            }
        }
        return null;
    }
}
