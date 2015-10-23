package rs.macro.util;

import java.net.URLDecoder;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Strings {

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
