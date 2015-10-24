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

    /**
     * Creates a ResourceLoader at the given directory.
     *
     * @param dir The directory that holds local resources.
     */
    public ResourceLoader(String dir) {
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        this.dir = dir;
    }

    /**
     * Gets the full path to the given item.
     *
     * @param path The path relative to the resource directory.
     * @return The full path to the given item.
     */
    public String fullPathFor(String path) {
        return dir + path;
    }

    /**
     * Gets the InputStream of the given item.
     *
     * @param path The path relative to the resource directory.
     * @return The InputStream of the given item.
     */
    public InputStream get(String path) {
        return ClassLoader.getSystemResourceAsStream(dir + path);
    }

    /**
     * A byte array of the given item's content.
     *
     * @param path The path relative to the resource directory.
     * @return A byte array of the given item's content.
     */
    public byte[] binaryFor(String path) {
        try {
            return Internet.downloadBinary(get(path), null);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Gets the BufferedImage of the given item.
     *
     * @param img The path relative to the resource directory.
     * @return The BufferedImage of the given item.
     */
    public BufferedImage imageFor(String img) {
        try (InputStream in = get(img)) {
            return ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Gets the Font of the given item.
     *
     * @param fontName The path relative to the resource directory.
     * @return The Font of the given item.
     */
    public Font fontFor(String fontName) {
        return fontFor(fontName, Font.TRUETYPE_FONT, 12F);
    }

    /**
     * Gets the Font of the given item with the given arguments.
     *
     * @param fontName The path relative to the resource directory.
     * @param fontType The type of font. (TTF/OTF)
     * @return The Font of the given item with the given arguments.
     */
    public Font fontFor(String fontName, int fontType) {
        return fontFor(fontName, fontType, 12F);
    }

    /**
     * Gets the Font of the given item with the given arguments.
     *
     * @param fontName The path relative to the resource directory.
     * @param fontType The type of font. (TTF/OTF)
     * @param size     The size of the font.
     * @return The Font of the given item with the given arguments.
     */
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