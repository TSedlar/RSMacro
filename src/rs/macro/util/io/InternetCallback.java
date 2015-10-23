package rs.macro.util.io;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
public abstract class InternetCallback {

    public int length = -1;

    public abstract void onDownload(int percent);
}