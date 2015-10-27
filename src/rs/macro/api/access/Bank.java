package rs.macro.api.access;

import rs.macro.RSMacro;
import rs.macro.api.access.input.Mouse;
import rs.macro.api.data.BankModel;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.PixelBuilder;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;

/**
 * @author Tyler Sedlar, Jacob Doiron
 * @since 10/22/15
 */
public class Bank {

    public static final PixelModel VIEWING_BANK = PixelModel.fromString("#FF981F/10 #FF981F/10/25/-1 #FF981F/10/83/0 #FF981F/10/118/-2 #FF981F/10/-147/-2");
    public static final PixelModel VIEWING_PIN = PixelModel.fromString("#FFFF00/10 #FFFF00/10/-2/10 #FFFF00/10/15/5 #FFFF00/10/31/5 #FFFF00/10/46/10");

    public static final Rectangle[] SLOTS = {new Rectangle(73, 83, 35, 31), new Rectangle(121, 83, 35, 31), new Rectangle(169, 83, 35, 31), new Rectangle(217, 83, 35, 31), new Rectangle(265, 83, 35, 31), new Rectangle(313, 83, 35, 31), new Rectangle(361, 83, 35, 31), new Rectangle(409, 83, 35, 31), new Rectangle(73, 119, 35, 31), new Rectangle(121, 119, 35, 31), new Rectangle(169, 119, 35, 31), new Rectangle(217, 119, 35, 31), new Rectangle(265, 119, 35, 31), new Rectangle(313, 119, 35, 31), new Rectangle(361, 119, 35, 31), new Rectangle(409, 119, 35, 31), new Rectangle(73, 155, 35, 31), new Rectangle(121, 155, 35, 31), new Rectangle(169, 155, 35, 31), new Rectangle(217, 155, 35, 31), new Rectangle(265, 155, 35, 31), new Rectangle(313, 155, 35, 31), new Rectangle(361, 155, 35, 31), new Rectangle(409, 155, 35, 31), new Rectangle(73, 191, 35, 31), new Rectangle(121, 191, 35, 31), new Rectangle(169, 191, 35, 31), new Rectangle(217, 191, 35, 31), new Rectangle(265, 191, 35, 31), new Rectangle(313, 191, 35, 31), new Rectangle(361, 191, 35, 31), new Rectangle(409, 191, 35, 31), new Rectangle(73, 227, 35, 31), new Rectangle(121, 227, 35, 31), new Rectangle(169, 227, 35, 31), new Rectangle(217, 227, 35, 31), new Rectangle(265, 227, 35, 31), new Rectangle(313, 227, 35, 31), new Rectangle(361, 227, 35, 31), new Rectangle(409, 227, 35, 31), new Rectangle(73, 263, 35, 31), new Rectangle(121, 263, 35, 31), new Rectangle(169, 263, 35, 31), new Rectangle(217, 263, 35, 31), new Rectangle(265, 263, 35, 31), new Rectangle(313, 263, 35, 31), new Rectangle(361, 263, 35, 31), new Rectangle(409, 263, 35, 31)};

    /**
     * Gets the currently selected bank's BankModel.
     *
     * @return The currently selected bank's BankModel.
     */
    public static BankModel model() {
        return RSMacro.instance().selector().dataSelector().bank();
    }

    /**
     * Tells us if the bank is currently open.
     *
     * @return <t>true</t> if the bank is currently open; otherwise, <t>false</t>.
     */
    public static boolean open() {
        if (!Camera.setAngle(model().angle().value)) {
            return false;
        }
        Point center = Viewport.center();
        Point nearest = RuneScape.pixels().operator().builder()
                .model(model().model()).query().sorted(
                        (a, b) -> Double.compare(a.distance(center), b.distance(center))
                ).findFirst().orElse(null);
        if (nearest != null) {
            Mouse.click(nearest, true);
            return Time.waitFor(2500, Bank::viewing);
        } else {
            return false;
        }
    }

    /**
     * Tells us if the bank is currently being viewed.
     *
     * @return <t>true</t> if we are viewing the bank; otherwise, <t>false</t>.
     */
    public static boolean viewing() {
        return RuneScape.pixels().operator().builder()
                .bounds(22, 10, 319, 25)
                .model(VIEWING_BANK)
                .query().count() > 0;
    }

    /**
     * Tells us if the bank pin screen is being shown.
     *
     * @return <t>true</t> if the bank pin screen is open; otherwise, <t>false</t>.
     */
    public static boolean viewingPin() {
        return RuneScape.pixels().operator().builder()
                .bounds(425, 32, 67, 19)
                .model(VIEWING_PIN)
                .query().count() > 0;
    }

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
     * @return A slot matching the given PixelModel.
     */
    public static Rectangle findSlot(PixelModel model) {
        return Slots.findSlot(SLOTS, model);
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
