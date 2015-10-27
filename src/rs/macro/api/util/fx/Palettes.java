package rs.macro.api.util.fx;

/**
 * @author Tyler Sedlar
 * @since 10/27/15
 */
public class Palettes {

    public static final int[] WEB_SAFE_PALETTE = new int[216];
    public static final int[] PALETTE_64 = new int[64];
    public static final int[] PALETTE_27 = new int[27];
    public static final int[] PALETTE_8 = new int[8];

    public static int[] COLOR_BLIND = {
            // blues
            Colors.hexToRGB("#EFF3FF"),
            Colors.hexToRGB("#BDD7E7"),
            Colors.hexToRGB("#6BAED6"),
            Colors.hexToRGB("#3182BD"),
            Colors.hexToRGB("#08519C"),

            // greens
            Colors.hexToRGB("#EDF8FB"),
            Colors.hexToRGB("#B2E2E2"),
            Colors.hexToRGB("#66C2A4"),
            Colors.hexToRGB("#2CA25F"),
            Colors.hexToRGB("#006D2C"),

            // purples
            Colors.hexToRGB("#B3CDE3"),
            Colors.hexToRGB("#8C96C6"),
            Colors.hexToRGB("#8856A7"),
            Colors.hexToRGB("#810F7C"),

            // blue-greens
            Colors.hexToRGB("#F0F9E8"),
            Colors.hexToRGB("#BAE4BC"),
            Colors.hexToRGB("#7BCCC4"),
            Colors.hexToRGB("#43A2CA"),
            Colors.hexToRGB("#0868AC"),

            // light greens
            Colors.hexToRGB("#EDF8E9"),
            Colors.hexToRGB("#BAE4B3"),
            Colors.hexToRGB("#74C476"),
            Colors.hexToRGB("#31A354"),

            // grays
            Colors.hexToRGB("#F7F7F7"),
            Colors.hexToRGB("#CCCCCC"),
            Colors.hexToRGB("#969696"),
            Colors.hexToRGB("#636363"),
            Colors.hexToRGB("#252525"),

            // oranges
            Colors.hexToRGB("#FEEDDE"),
            Colors.hexToRGB("#FDBE85"),
            Colors.hexToRGB("#FD8D3C"),
            Colors.hexToRGB("#E6550D"),
            Colors.hexToRGB("#A63603"),

            // reds
            Colors.hexToRGB("#FEF0D9"),
            Colors.hexToRGB("#FDCC8A"),
            Colors.hexToRGB("#FC8D59"),
            Colors.hexToRGB("#E34A33"),
            Colors.hexToRGB("#B30000"),

            // ligh blues
            Colors.hexToRGB("#F1EEF6"),
            Colors.hexToRGB("#BDC9E1"),
            Colors.hexToRGB("#74A9CF"),
            Colors.hexToRGB("#2B8CBE"),
            Colors.hexToRGB("#045A8D"),

            // dark blue-greens
            Colors.hexToRGB("#F6EFF7"),
            Colors.hexToRGB("#BDC9E1"),
            Colors.hexToRGB("#67A9CF"),
            Colors.hexToRGB("#1C9099"),
            Colors.hexToRGB("#016C59"),

            // pinks
            Colors.hexToRGB("#F1EEF6"),
            Colors.hexToRGB("#D7B5D8"),
            Colors.hexToRGB("#DF65B0"),
            Colors.hexToRGB("#DD1C77"),
            Colors.hexToRGB("#980043"),

            // lavenders
            Colors.hexToRGB("#F2F0F7"),
            Colors.hexToRGB("#CBC9E2"),
            Colors.hexToRGB("#9E9AC8"),
            Colors.hexToRGB("#756BB1"),
            Colors.hexToRGB("#54278F"),

            // pink-purplEs
            Colors.hexToRGB("#FEEBE2"),
            Colors.hexToRGB("#FBB4B9"),
            Colors.hexToRGB("#F768A1"),
            Colors.hexToRGB("#C51B8A"),
            Colors.hexToRGB("#7A0177"),

            // red-oranges
            Colors.hexToRGB("#FEE5D9"),
            Colors.hexToRGB("#FCAE91"),
            Colors.hexToRGB("#FB6A4A"),
            Colors.hexToRGB("#DE2D26"),
            Colors.hexToRGB("#A50F15"),

            // yellow-greens
            Colors.hexToRGB("#FFFFCC"),
            Colors.hexToRGB("#C2E699"),
            Colors.hexToRGB("#78C679"),
            Colors.hexToRGB("#31A354"),
            Colors.hexToRGB("#006837"),

            // yellow-blues
            Colors.hexToRGB("#FFFFCC"),
            Colors.hexToRGB("#A1DAB4"),
            Colors.hexToRGB("#41B6C4"),
            Colors.hexToRGB("#2C7FB8"),
            Colors.hexToRGB("#253494"),

            // browns
            Colors.hexToRGB("#FFFFD4"),
            Colors.hexToRGB("#FED98E"),
            Colors.hexToRGB("#FE9929"),
            Colors.hexToRGB("#D95F0E"),
            Colors.hexToRGB("#993404"),

            // yellow-reds
            Colors.hexToRGB("#FFFFB2"),
            Colors.hexToRGB("#FECC5C"),
            Colors.hexToRGB("#FD8D3C"),
            Colors.hexToRGB("#F03B20"),
            Colors.hexToRGB("#BD0026")
    };

    private static void insertIntoPalette(int[] palette, int include) {
        for (int i = 0, r = 0; r <= 255; r += include) {
            for (int g = 0; g <= 255; g += include) {
                for (int b = 0; b <= 255; b += include) {
                    palette[i++] = ((r << 16) | (g << 8) | b);
                }
            }
        }
    }

    static {
        insertIntoPalette(WEB_SAFE_PALETTE, 51);
        insertIntoPalette(PALETTE_64, 85);
        insertIntoPalette(PALETTE_27, 127);
        insertIntoPalette(PALETTE_8, 255);
    }


    /**
     * Selects the nearest matching RGB from the given palette.
     *
     * @param palette The palette to search.
     * @param rgb     The RGB value to search with.
     * @return The nearest matching RGB from the given palette.
     */
    public static int selectFromPalette(int[] palette, int rgb) {
        double lowestDistance = Double.MAX_VALUE;
        int nearestRGB = 0;
        for (int col : palette) {
            double distance = (double) Colors.distanceSquared(rgb, col) +
                    (double) Colors.tolerance(rgb, col) + Colors.average(rgb, col);
            if (lowestDistance == Double.MAX_VALUE || distance < lowestDistance) {
                lowestDistance = distance;
                nearestRGB = col;
            }
        }
        return nearestRGB;
    }

    /**
     * Selects the nearest matching RGB from a Web-Safe Palette.
     *
     * @param rgb The RGB value to search with.
     * @return The nearest matching RGB from the Web-Safe Palette.
     */
    public static int selectFromWebSafePalette(int rgb) {
        return selectFromPalette(WEB_SAFE_PALETTE, rgb);
    }

    /**
     * Selects the nearest matching RGB from a 64 color Palette.
     *
     * @param rgb The RGB value to search with.
     * @return The nearest matching RGB from a 64 color Palette.
     */
    public static int selectFromPalette64(int rgb) {
        return selectFromPalette(PALETTE_64, rgb);
    }

    /**
     * Selects the nearest matching RGB from a 27 color Palette.
     *
     * @param rgb The RGB value to search with.
     * @return The nearest matching RGB from a 27 color Palette.
     */
    public static int selectFromPalette27(int rgb) {
        return selectFromPalette(PALETTE_27, rgb);
    }

    /**
     * Selects the nearest matching RGB from an 8 color Palette.
     *
     * @param rgb The RGB value to search with.
     * @return The nearest matching RGB from an 8 color Palette.
     */
    public static int selectFromPalette8(int rgb) {
        return selectFromPalette(PALETTE_8, rgb);
    }

    /**
     * Selects the nearest matching RGB from a color-blind Palette.
     *
     * @param rgb The RGB value to search with.
     * @return The nearest matching RGB from an a color-blind Palette.
     */
    public static int selectFromColorBlind(int rgb) {
        return selectFromPalette(COLOR_BLIND, rgb);
    }
}
