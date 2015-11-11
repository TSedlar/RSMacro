package rs.macro.api.access;

import rs.macro.api.util.fx.Colors;
import rs.macro.api.util.fx.PixelBuilder;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/24/15
 */
public class Inventory {

    public static final Rectangle[] SLOTS = {new Rectangle(562, 212, 32, 32), new Rectangle(604, 212, 32, 32), new Rectangle(646, 212, 32, 32), new Rectangle(688, 212, 32, 32), new Rectangle(562, 248, 32, 32), new Rectangle(604, 248, 32, 32), new Rectangle(646, 248, 32, 32), new Rectangle(688, 248, 32, 32), new Rectangle(562, 284, 32, 32), new Rectangle(604, 284, 32, 32), new Rectangle(646, 284, 32, 32), new Rectangle(688, 284, 32, 32), new Rectangle(562, 320, 32, 32), new Rectangle(604, 320, 32, 32), new Rectangle(646, 320, 32, 32), new Rectangle(688, 320, 32, 32), new Rectangle(562, 356, 32, 32), new Rectangle(604, 356, 32, 32), new Rectangle(646, 356, 32, 32), new Rectangle(688, 356, 32, 32), new Rectangle(562, 392, 32, 32), new Rectangle(604, 392, 32, 32), new Rectangle(646, 392, 32, 32), new Rectangle(688, 392, 32, 32), new Rectangle(562, 428, 32, 32), new Rectangle(604, 428, 32, 32), new Rectangle(646, 428, 32, 32), new Rectangle(688, 428, 32, 32)};

    public static final int INV_BORDER_RGB = Colors.hexToRGB("#000001");

    /**
     * Creates a PixelBuilder of the given slot, excluding shadows/amount/borders.
     *
     * @param slot The slot index to create a PixelBuilder for.
     * @return A PixelBuilder of the given slot, excluding shadows/amount/borders.
     */
    public static PixelBuilder createPixelBuilderAt(int slot) {
        return Slots.createPixelBuilderAt(SLOTS, slot);
    }

    /**
     * Creates a PixelModel of the given slot, excluding shadows/amount/borders.
     *
     * @param slot      The slot index to create a PixelModel for.
     * @param tolerance The tolerance distance for each Pixel.
     * @return A PixelModel of the given slot, excluding shadows/amount/borders.
     */
    public static PixelModel createModelAt(int slot, int tolerance) {
        return Slots.createModelAt(SLOTS, slot, tolerance);
    }

    /**
     * Checks if the given slot has an item or not.
     *
     * @param slot The slot index to check for.
     * @return <t>true</t> if the given slot has an item, otherwise <t>false</t>.
     */
    public static boolean hasItem(int slot) {
        return Slots.hasItem(SLOTS, Slots.ITEM_BORDER_RGB, 1, slot);
    }

    /**
     * Finds a slot matching the given PixelModel.
     *
     * @param model The model to search for.
     * @param reverse The order to search in.
     * @return A slot matching the given PixelModel.
     */
    public static Rectangle findSlot(PixelModel model, boolean reverse) {
        return Slots.findSlot(SLOTS, model, reverse);
    }

    /**
     * Finds a slot matching the given PixelModel.
     *
     * @param model The model to search for.
     * @return A slot matching the given PixelModel.
     */
    public static Rectangle findSlot(PixelModel model) {
        return findSlot(model, false);
    }

    /**
     * Finds the last slot with an item in it.
     *
     * @return The last slot with an item in it.
     */
    public static Rectangle findLastValidSlot() {
        for (int i = (SLOTS.length - 1); i > 0; i--) {
            if (hasItem(i)) {
                return SLOTS[i];
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
        return Slots.idAt(SLOTS, slot);
    }

    /**
     * Finds the slot with the given item id.
     *
     * @param lowId  The lowest id of the item to look for.
     * @param highId The highest id of the item to look for.
     * @return The slot with the given item id.
     */
    public static Rectangle findSlot(int lowId, int highId) {
        return Slots.findSlot(SLOTS, lowId, highId);
    }
}
