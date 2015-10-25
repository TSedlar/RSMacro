package rs.macro.util;

import java.net.URLDecoder;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Strings {

    private static final String A = "A", TEN = "10";
    private static final String B = "B", ELEVEN = "11";
    private static final String C = "C", TWELVE = "12";
    private static final String D = "D", THIRTEEN = "13";
    private static final String E = "E", FOURTEEN = "14";
    private static final String F = "F", FIFTEEN = "15";

    /**
     * Replaces letters in a hex string with their number. (A -> 10)
     *
     * @param hex The hex string.
     * @return A string without hex letters. (AC -> 1012)
     */
    public static String replaceHexLetters(String hex) {
        return hex.replaceAll(A, TEN)
                .replaceAll(B, ELEVEN)
                .replaceAll(C, TWELVE)
                .replaceAll(D, THIRTEEN)
                .replaceAll(E, FOURTEEN)
                .replaceAll(F, FIFTEEN);
    }

    /**
     * Cleans up a string's UTF8 content.
     *
     * @param string The string to clean up.
     * @return A string with cleaned up UTF8 content. (%20 = " ", \\\\ = /)
     */
    public static String cleanUTF8(String string) {
        string = string.replaceAll("\\\\", "/");
        string = string.replaceAll("%20", " ");
        try {
            string = URLDecoder.decode(string, "UTF-8")
                    .replaceAll("\\\\", "/")
                    .replaceAll("%20", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }
}
