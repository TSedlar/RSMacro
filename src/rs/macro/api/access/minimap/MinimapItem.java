package rs.macro.api.access.minimap;

import rs.macro.api.util.fx.Colors;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 10/25/15
 */
public class MinimapItem {

    public final String label, hex;
    public final int rgb, tolerance;
    public final Color display;

    /**
     * Creates a MinimapItem
     *
     * @param label The name of this MinimapItem.
     * @param hex The color of the in-game minimap element. (sidewalk, gravel, etc.)
     * @param tolerance The tolerance distance to check for.
     * @param display The color to render onto the minimap.
     */
    public MinimapItem(String label, String hex, int tolerance, Color display) {
        this.label = label;
        this.hex = hex;
        this.rgb = Colors.hexToRGB(hex);
        this.tolerance = tolerance;
        this.display = display;
    }
}
