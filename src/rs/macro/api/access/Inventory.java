package rs.macro.api.access;

import rs.macro.api.util.filter.Filter;
import rs.macro.api.util.fx.Colors;
import rs.macro.api.util.fx.PixelBuilder;
import rs.macro.api.util.fx.model.PixelModel;
import rs.macro.util.Strings;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/24/15
 */
public class Inventory {

    public static final Rectangle[] SLOTS = {new Rectangle(562, 212, 32, 32), new Rectangle(604, 212, 32, 32), new Rectangle(646, 212, 32, 32), new Rectangle(688, 212, 32, 32), new Rectangle(562, 248, 32, 32), new Rectangle(604, 248, 32, 32), new Rectangle(646, 248, 32, 32), new Rectangle(688, 248, 32, 32), new Rectangle(562, 284, 32, 32), new Rectangle(604, 284, 32, 32), new Rectangle(646, 284, 32, 32), new Rectangle(688, 284, 32, 32), new Rectangle(562, 320, 32, 32), new Rectangle(604, 320, 32, 32), new Rectangle(646, 320, 32, 32), new Rectangle(688, 320, 32, 32), new Rectangle(562, 356, 32, 32), new Rectangle(604, 356, 32, 32), new Rectangle(646, 356, 32, 32), new Rectangle(688, 356, 32, 32), new Rectangle(562, 392, 32, 32), new Rectangle(604, 392, 32, 32), new Rectangle(646, 392, 32, 32), new Rectangle(688, 392, 32, 32), new Rectangle(562, 428, 32, 32), new Rectangle(604, 428, 32, 32), new Rectangle(646, 428, 32, 32), new Rectangle(688, 428, 32, 32)};
    public static final int INV_BACKGROUND_RGB = Colors.hexToRGB("#494035");
    public static final int INV_BORDER_RGB = Colors.hexToRGB("#000001");
    public static final int INV_SHADOW_RGB = Colors.hexToRGB("#302020");
    public static final int AMOUNT_RGB = Colors.hexToRGB("#FFFF00");
    public static final Filter<Integer> ITEM_MODEL_FILTER = (rgb) ->
            Colors.tolerance(rgb, INV_BACKGROUND_RGB) > 15 &&
            Colors.tolerance(rgb, INV_BORDER_RGB) > 2 &&
            Colors.tolerance(rgb, INV_SHADOW_RGB) > 2 &&
            Colors.tolerance(rgb, AMOUNT_RGB) > 5;

    private static final String TAG = "#", EMPTY = "";

    /**
     * Creates a PixelBuilder of the given slot, excluding shadows/amount/borders.
     *
     * @param slot The slot index to create a PixelBuilder for.
     * @return A PixelBuilder of the given slot, excluding shadows/amount/borders.
     */
    public static PixelBuilder createPixelBuilderAt(int slot) {
        Rectangle bounds = SLOTS[slot];
        return RuneScape.pixels().operator().builder()
                .bounds(bounds.x, bounds.y + (int) (bounds.height * 0.32D), bounds.width,
                        (int) (bounds.height * 0.68D))
                .filter(ITEM_MODEL_FILTER);
    }

    /**
     * Creates a PixelModel of the given slot, excluding shadows/amount/borders.
     *
     * @param slot The slot index to create a PixelModel for.
     * @param tolerance The tolerance distance for each Pixel.
     * @return A PixelModel of the given slot, excluding shadows/amount/borders.
     */
    public static PixelModel createModelAt(int slot, int tolerance) {
        List<Point> points = createPixelBuilderAt(slot).all();
        return !points.isEmpty() ? PixelModel.fromPoints(points, tolerance) : null;
    }

    /**
     * Checks if the given slot has an item or not.
     *
     * @param slot The slot index to check for.
     * @return <t>true</t> if the given slot has an item, otherwise <t>false</t>.
     */
    public static boolean hasItem(int slot) {
        return RuneScape.pixels().operator().builder()
                .bounds(SLOTS[slot])
                .tolFilter(INV_BORDER_RGB, 1)
                .first() != null;
    }

    /**
     * Finds a slot matching the given PixelModel.
     *
     * @param model The model to search for.
     * @return A slot matching the given PixelModel.
     */
    public static Rectangle findSlot(PixelModel model) {
        Point match = RuneScape.pixels().operator().builder()
                .filterLocation((x, y) -> {
                    for (Rectangle slot : SLOTS) {
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
        for (Rectangle slot : SLOTS) {
            if (slot.contains(match)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Gets the count of items in the inventory.
     *
     * @return The count of items in the inventory.
     */
    public static int count() {
        List<Rectangle> matched = new ArrayList<>();
        return ((int) RuneScape.pixels().operator().builder()
                .filterLocation((x, y) -> {
                    for (Rectangle slot : matched) {
                        if (slot.contains(x, y)) {
                            return false;
                        }
                    }
                    for (Rectangle slot : SLOTS) {
                        if (slot.contains(x, y)) {
                            matched.add(slot);
                            return true;
                        }
                    }
                    return false;
                })
                .tolFilter(INV_BORDER_RGB, 1)
                .query().count());
    }

    /**
     * Checks whether the inventory is empty or not.
     *
     * @return <t>true</t> if the inventory is empty, otherwise <t>false</t>.
     */
    public static boolean empty() {
        return count() == 0;
    }

    /**
     * Checks whether the inventory is full or not.
     *
     * @return <t>true</t> if the inventory is full, otherwise <t>false</t>.
     */
    public static boolean full() {
        return count() == 28;
    }

    /**
     * Gets a visual representation ID of the given slot.
     *
     * @param slot The slot index to get an ID for.
     * @return A visual representation ID of the given slot.
     */
    public static int idAt(int slot) {
        PixelBuilder builder = Inventory.createPixelBuilderAt(slot);
        List<Point> points = builder.all();
        if (points.isEmpty()) {
            return -1;
        }
        int length = points.size();
        int minX = points.stream().sorted((a, b) -> a.x - b.x).findFirst().get().x;
        int maxX = points.stream().sorted((a, b) -> b.x - a.x).findFirst().get().x;
        int width = (maxX - minX);
        int minY = points.stream().sorted((a, b) -> a.y - b.y).findFirst().get().y;
        int maxY = points.stream().sorted((a, b) -> b.y - a.y).findFirst().get().y;
        int height = (maxY - minY);
        int median = builder.median();
        String hex = Colors.rgbToHex(median).replace(TAG, EMPTY);
        String first = Strings.replaceHexLetters(hex.substring(0, 2));
        String second = Strings.replaceHexLetters(hex.substring(2, 4));
        String third = Strings.replaceHexLetters(hex.substring(4, 6));
        return length + width + height + Integer.parseInt(first) +
                Integer.parseInt(second) + Integer.parseInt(third);
    }

    /**
     * Finds the slot with the given item id.
     *
     * @param id The id of the item to look for.
     * @return The slot with the given item id.
     */
    public static Rectangle findSlot(int id) {
        for (int i = 0; i < SLOTS.length; i++) {
            int slotId = idAt(i);
            if (id == slotId) {
                return SLOTS[i];
            }
        }
        return null;
    }
}
