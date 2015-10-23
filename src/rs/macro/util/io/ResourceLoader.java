package rs.macro.util.io;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class ResourceLoader {

    private final String dir;
    private final Map<String, Font> fontCache = new HashMap<>();

    public ResourceLoader(String dir) {
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        this.dir = dir;
    }

    public String fullPathFor(String path) {
        return dir + path;
    }

    public InputStream get(String path) {
        return ClassLoader.getSystemResourceAsStream(dir + path);
    }

    public byte[] binaryFor(String path) {
        try {
            return Internet.downloadBinary(get(path), null);
        } catch (IOException e) {
            return null;
        }
    }

    public BufferedImage imageFor(String img) {
        try (InputStream in = get(img)) {
            return ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }

    public Font fontFor(String fontName) {
        return fontFor(fontName, Font.TRUETYPE_FONT, 12F);
    }

    public Font fontFor(String fontName, int fontType) {
        return fontFor(fontName, fontType, 12F);
    }

    public Font fontFor(String fontName, int fontType, float size) {
        if (!fontCache.containsKey(fontName)) {
            try (InputStream in = get(fontName)) {
                Font font = Font.createFont(fontType, in).deriveFont(size);
                fontCache.put(fontName, font);
            } catch (IOException | FontFormatException e) {
                return null;
            }
        }
        return fontCache.get(fontName);
    }
}