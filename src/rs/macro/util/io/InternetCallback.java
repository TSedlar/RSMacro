package rs.macro.util.io;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public abstract class InternetCallback {

    public int length = -1;

    /**
     * A callback of what to do upon a portion of content being downloaded.
     *
     * @param percent The percentage of the content that is downloaded.
     */
    public abstract void onDownload(int percent);
}