package rs.macro.api.access;

import rs.macro.api.util.fx.PixelTask;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class RuneScape {

    public static final PixelModel LOGGED_IN_MODEL = PixelModel.fromString("#BFB490/10 #BFB490/10/8/4");

    private static final PixelTask pixelTask = new PixelTask();

    /**
     * @return The PixelTask.
     */
    public static PixelTask pixels() {
        return pixelTask;
    }

    /**
     * Gets the rgb color at the specified coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The rbg color at the specified coordinates.
     */
    public static int rgbAt(int x, int y) {
        return pixels().operator().at(x, y);
    }

    /**
     * @return The raw BufferedImage of the game.
     */
    public static BufferedImage image() {
        return GameCanvas.raw;
    }

    /**
     * @return The game's current buffered image.
     */
    public static BufferedImage fullImage() {
        return GameCanvas.buffer;
    }

    /**
     * Validates given coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return <t>true</t> if the coordinates are within the game's viewport; otherwise, <t>false</t>.
     */
    public static boolean validatePoint(int x, int y) {
        return x >= 0 && x <= GameCanvas.GAME_SIZE.width && y >= 0 && y <= GameCanvas.GAME_SIZE.height;
    }

    /**
     * Validates a given Point.
     *
     * @param p The point.
     * @return <t>true</t> if the specified Point is within the game's viewport; otherwise, <t>false</t>.
     */
    public static boolean validatePoint(Point p) {
        return validatePoint(p.x, p.y);
    }

    /**
     * Checks whether or not the player is currently logged in.
     *
     * @return <t>true</t> if the player is logged in; otherwise, <t>false</t>.
     */
    public static boolean playing() {
        return pixels().operator().builder()
                .model(LOGGED_IN_MODEL)
                .bounds(509, 14, 40, 41)
                .query().count() > 0;
    }

    /**
     * Gets the currently opened GameTab.
     *
     * @return The currently opened GameTab.
     */
    public GameTab currentTab() {
        for (GameTab tab : GameTab.values()) {
            if (tab.viewing()) {
                return tab;
            }
        }
        return null;
    }
}
