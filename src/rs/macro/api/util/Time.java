package rs.macro.api.util;

import rs.macro.RSMacro;
import rs.macro.internal.ui.MacroDataSelector;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tyler Sedlar, Finn Thompson, Jacob Doiron
 * @since 10/21/15
 */
public class Time {

    private static final String ZERO = "0";
    private static final String COLON = ":";
    private static final String COLON_FORMAT = "hh:mm a";
    private static final String NON_COLON_FORMAT = "hh mm a";

    public static boolean sleep(int millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException ignored) {
            return false;
        }
    }

    public static boolean sleep(int min, int max) {
        return sleep(Random.nextInt(min, max));
    }

    public static boolean waitFor(int timeout, int threshold,
                                  Condition condition) {
        long start = millis();
        while (millis() - start < timeout) {
            if (condition.met()) {
                return true;
            }
            sleep(threshold);
        }
        return false;
    }

    public static boolean waitFor(int timeout, Condition condition) {
        return waitFor(timeout, Random.nextInt(50, 100), condition);
    }

    public static long toMillis(long nanos) {
        return nanos / 1_000_000;
    }

    public static long toNanos(long millis) {
        return millis * 1_000_000;
    }

    public static long millis() {
        return toMillis(System.nanoTime());
    }

    public static String format(long time) {
        StringBuilder t = new StringBuilder(11);
        long totalSeconds = time / 1000;
        long totalMinutes = totalSeconds / 60;
        long totalHours = totalMinutes / 60;
        long totalDays = totalHours / 24;
        int seconds = (int) totalSeconds % 60;
        int minutes = (int) totalMinutes % 60;
        int hours = (int) totalHours % 24;
        int days = (int) totalDays;
        if (days > 0) {
            if (days < 10) {
                t.append(ZERO);
            }
            t.append(days);
            t.append(COLON);
        }
        if (hours < 10) {
            t.append(ZERO);
        }
        t.append(hours);
        t.append(COLON);
        if (minutes < 10) {
            t.append(ZERO);
        }
        t.append(minutes);
        t.append(COLON);
        if (seconds < 10) {
            t.append(ZERO);
        }
        t.append(seconds);
        return t.toString();
    }

    public static int hourly(long elapsed, int actions) {
        return elapsed > 0 ? (int) (actions * 3600000D / elapsed) : 0;
    }

    public static String machineTime(boolean colon) {
        return new SimpleDateFormat(colon ? COLON_FORMAT :
                NON_COLON_FORMAT).format(new Date());
    }

    public static String machineTime() {
        return machineTime(true);
    }

    public static boolean overdue() {
        String time = machineTime(false);
        String[] splits = time.split(" ");
        String hour = splits[0], min = splits[1], period = splits[2];
        MacroDataSelector data = RSMacro.instance().selector().dataSelector();
        return data.hour() > 0 && period.equals(data.period()) &&
                Integer.parseInt(hour) == data.hour() &&
                Integer.parseInt(min) >= data.minute();
    }
}