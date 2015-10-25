package rs.macro.api.access;

import rs.macro.api.util.fx.Colors;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;
import java.util.*;
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

    /**
     * Creates a PixelModel of the given slot, excluding shadows/amount/borders.
     *
     * @param slot The slot index to create a PixelModel for.
     * @param tolerance The tolerance distance for each Pixel.
     * @return A PixelModel of the given slot, excluding shadows/amount/borders.
     */
    public static PixelModel createModelAt(int slot, int tolerance) {
        java.util.List<Point> points = RuneScape.pixels().operator().builder()
                .bounds(SLOTS[slot])
                .filter(rgb -> (Colors.tolerance(rgb, INV_BACKGROUND_RGB) > 15 &&
                        Colors.tolerance(rgb, INV_BORDER_RGB) > 2 &&
                        Colors.tolerance(rgb, INV_SHADOW_RGB) > 2 &&
                        Colors.tolerance(rgb, AMOUNT_RGB) > 5))
                .all();
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
}
