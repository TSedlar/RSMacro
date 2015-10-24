package rs.macro.util.io;

import rs.macro.util.OperatingSystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public class Internet {

    public static final String QUOTE = "\"";
    public static final String AMPERSAND = "&";

    public static final int BUFFER_SIZE = 8192;

    /**
     * A User-Agent based on the current OperatingSystem.
     */
    private static final String DEFAULT_USER_AGENT;

    private static int timeout = -1;

    static {
        DEFAULT_USER_AGENT = getDefaultUserAgent();
    }

    /**
     * Sets the max timeout to attempt to download for.
     *
     * @param timeout The max timeout to attempt to download for, in milliseconds.
     */
    public static void setTimeout(int timeout) {
        Internet.timeout = timeout;
    }

    /**
     * Fixes string encoding. (&quot; = ")
     *
     * @param string The string to fix encoding for.
     * @return The string with fixed encoding.
     */
    public static String fixEncoding(String string) {
        return string.replaceAll("&quot;", QUOTE).replaceAll("&amp;", AMPERSAND);
    }

    /**
     * Changes file path encoding.
     *
     * @param string The path of the file to change encoding for.
     * @return The given path with changed encoding.
     */
    public static String fileEncoding(String string) {
        return string.replaceAll(QUOTE, "'");
    }

    /**
     * The default User-Agent of the current OperatingSystem.
     *
     * @return The default User-Agent of the current OperatingSystem.
     */
    public static String getDefaultUserAgent() {
        return "Mozilla/5.0 (" + OperatingSystem.get().userAgentPart() +
                ") AppleWebKit/537.17 (KHTML, like Gecko) " +
                "Chrome/24.0.1312.57 Safari/537.17";
    }

    /**
     * Masks the given URLConnection with the default User-Agent.
     *
     * @param url The URLConnection to mask.
     * @return The given URLConnection masked with the default User-Agent.
     */
    public static URLConnection mask(URLConnection url) {
        url.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
        return url;
    }

    /**
     * Posts to the given url with the given arguments.
     *
     * @param url  The URL to post to.
     * @param args The arguments to post.
     * @return The result given back from the URL.
     */
    public static String post(String url, String... args) {
        try {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (String arg : args) {
                if (first) {
                    builder.append(arg);
                    first = false;
                } else {
                    builder.append("&").append(arg);
                }
            }
            URLConnection conn = new URL(url).openConnection();
            HttpURLConnection connection = (HttpURLConnection) conn;
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", getDefaultUserAgent());
            connection.setRequestMethod("POST");
            OutputStream out = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(builder.toString());
            writer.flush();
            InputStream in = connection.getInputStream();
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inReader);
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads content from the given site with each line as an element in the returned List.
     *
     * @param site The URL to read from.
     * @param mask <t>true</t> to mask with the default User-Agent, otherwise <t>false</t>.
     * @return A List with each line as an element.
     */
    public static List<String> read(String site, boolean mask) {
        try {
            URL url = new URL(site);
            URLConnection connection = url.openConnection();
            if (mask) {
                connection = mask(connection);
            }
            if (timeout != -1) {
                connection.setConnectTimeout(timeout);
            }
            try (InputStream stream = connection.getInputStream()) {
                InputStreamReader inReader = new InputStreamReader(stream);
                BufferedReader reader = new BufferedReader(inReader);
                List<String> source = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    source.add(line);
                }
                return source;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads content from the given site with each line as an element in the returned List.
     *
     * @param site The URL to read from.
     * @return A List with each line as an element.
     */
    public static List<String> read(String site) {
        return read(site, true);
    }

    /**
     * Reads content from the given site as one String.
     *
     * @param site The URL to read from.
     * @return Content from the given site as one String.
     */
    public static String readFully(String site) {
        try (InputStream stream = new URL(site).openStream()) {
            return new String(downloadBinary(stream, null));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts the given InputStream into a byte array.
     *
     * @param in       The InputStream to convert.
     * @param callback The callback to call upon download percentage change.
     * @return The byte array content of the given InputStream.
     * @throws IOException
     */
    public static byte[] downloadBinary(InputStream in, InternetCallback callback)
            throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[BUFFER_SIZE];
            int downloaded = 0;
            int n;
            while ((n = in.read(buf, 0, BUFFER_SIZE)) > 0) {
                out.write(buf, 0, n);
                downloaded += n;
                if (callback != null && callback.length != -1) {
                    callback.onDownload(((downloaded * 100) / callback.length));
                }
            }
            if (callback != null) {
                callback.length = -1;
            }
            return out.toByteArray();
        }
    }

    /**
     * Downloads the file from the given URL to the given path.
     *
     * @param site     The URL of the file to download.
     * @param target   The target to write the file to.
     * @param callback The callback to call upon download percentage change.
     * @param mask     <t>true</t> to mask with the default User-Agent, otherwise <t>false</t>.
     * @return The File that was downloaded, if successful, otherwise <t>null</t>.
     */
    public static File download(String site, String target,
                                InternetCallback callback, boolean mask) {
        try {
            URL url = new URL(site);
            URLConnection connection = url.openConnection();
            if (mask) {
                connection = mask(connection);
            }
            if (timeout != -1) {
                connection.setConnectTimeout(timeout);
            }
            if (callback != null) {
                callback.length = connection.getContentLength();
            }
            try (InputStream stream = connection.getInputStream()) {
                File file = new File(target);
                file.getParentFile().mkdirs();
                try (FileOutputStream out = new FileOutputStream(file)) {
                    out.write(downloadBinary(stream, callback));
                    if (callback != null) {
                        callback.length = -1;
                    }
                    return file;
                }
            } catch (IOException ignored) {
                return null;
            }
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Downloads the file from the given URL to the given path.
     *
     * @param site     The URL of the file to download.
     * @param target   The target to write the file to.
     * @param callback The callback to call upon download percentage change.
     * @return The File that was downloaded, if successful, otherwise <t>null</t>.
     */
    public static File download(String site, String target, InternetCallback callback) {
        return download(site, target, callback, true);
    }

    /**
     * Reads the given URL into a BufferedImage.
     *
     * @param url The URL of the image.
     * @return A BufferedImage from the given URL.
     */
    public static BufferedImage readImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException ignored) {
            return null;
        }
    }
}