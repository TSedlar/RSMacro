package rs.macro.api.access;

import rs.macro.api.access.input.Mouse;
import rs.macro.api.util.Random;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.Colors;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author Tyler Sedlar, Jacob
 * @since 5/1/2015
 */
public class GameMenu {

    public static final int MENU_BORDER_RGB = Colors.hexToRGB("#5D5447");
    public static final PixelModel MENU_MODEL =
            PixelModel.fromString("#5D5447/2 #5D5447/2/0/3 #5D5447/2/1/17 #000000/2/1/1 #000000/2/1/16");
    public static final int MENU_ITEM_HEIGHT = 15;

    /**
     * Gets the root point of the game menu bounds.
     *
     * @return The root point of the game menu bounds.
     */
    public static Point location() {
        return RuneScape.pixels().operator().builder()
                .model(MENU_MODEL)
                .first();
    }

    /**
     * Checks whether the game menu is open or not.
     *
     * @return <t>true</t> if the game menu is open, otherwise <t>false</t>.
     */
    public static boolean viewing() {
        return location() != null;
    }

    /**
     * Finds the bounds of the game menu.
     *
     * @return The bounds of the game menu.
     */
    public static Rectangle bounds() {
        Point location = location();
        if (location != null) {
            int width = RuneScape.fullImage().getWidth(), height = RuneScape.fullImage().getHeight();
            int w;
            for (w = 0; w < width; w++) {
                int color = RuneScape.rgbAt(location.x + w, location.y);
                if (Colors.average(MENU_BORDER_RGB, color) > 2) {
                    break;
                }
            }
            int h;
            for (h = 0; h < height; h++) {
                int color = RuneScape.rgbAt(location.x, location.y + h);
                if (Colors.average(MENU_BORDER_RGB, color) > 2) {
                    break;
                }
            }
            return new Rectangle(location.x, location.y, w, h);
        }
        return null;
    }

    /**
     * Gets the amount of options in the game menu.
     *
     * @return The amount of options in the game menu.
     */
    public static int optionCount() {
        Rectangle bounds = bounds();
        return bounds != null ? (bounds.height - 22) / MENU_ITEM_HEIGHT : 0;
    }

    /**
     * Gets the bounds of the given game menu option index.
     *
     * @param index The index of the option in the game menu.
     * @return The bounds of the given game menu option index.
     */
    public static Rectangle boundsFor(int index) {
        Rectangle bounds = bounds();
        if (bounds != null) {
            return new Rectangle(bounds.x + 1, bounds.y + 20 + (index * MENU_ITEM_HEIGHT),
                    bounds.width - 3, MENU_ITEM_HEIGHT);
        }
        return null;
    }

    /**
     * Clicks within the bounds of the given game menu option index.
     *
     * @param index The index of the option in the game menu.
     * @return <t>true</t> if the option was clicked, otherwise <t>false</t>.
     */
    public static boolean selectIndex(int index) {
        if (!viewing()) {
            Mouse.click(false);
            Time.waitFor(Random.nextInt(500, 750), GameMenu::viewing);
        }
        Rectangle bounds = boundsFor(index);
        if (bounds != null) {
            Mouse.click(bounds.x + Random.nextInt(bounds.width - 1),
                    bounds.y + Random.nextInt(bounds.height - 1), true);
            return true;
        }
        return false;
    }
}
