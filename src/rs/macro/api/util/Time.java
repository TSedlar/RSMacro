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

    /**
     * Sleeps for the specified number of milliseconds.
     *
     * @param millis Number of milliseconds.
     * @return <t>true</t> if the Thread slept; otherwise, <t>false</t>.
     */
    public static boolean sleep(int millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException ignored) {
            return false;
        }
    }

    /**
     * Sleeps for a random length of time within the specified min and max values.
     *
     * @param min The minimum length of time in milliseconds.
     * @param max The maximum length of time in milliseconds.
     * @return <t>true</t> if the Thread slept; otherwise, <t>false</t>.
     */
    public static boolean sleep(int min, int max) {
        return sleep(Random.nextInt(min, max));
    }

    /**
     * Sleeps until the specified timeout or until the Condition is met.
     *
     * @param timeout   The timeout.
     * @param threshold The threshold to sleep for each iteration.
     * @param condition The Condition(s) to meet.
     * @return <t>true</t> if the Condition was met; otherwise <t>false</t>.
     */
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

    /**
     * Sleeps until the specified timeout or until the Condition is met;
     *
     * @param timeout   The timeout.
     * @param condition The Condition(s) to meet.
     * @return <t>true</t> if the Condition was met; otherwise, <t>false</t>.
     */
    public static boolean waitFor(int timeout, Condition condition) {
        return waitFor(timeout, Random.nextInt(50, 100), condition);
    }

    /**
     * Gets the millisecond equivalent to the specified number of nanoseconds.
     *
     * @param nanos The nanoseconds.
     * @return The equivalent number of millseconds.
     */
    public static long toMillis(long nanos) {
        return nanos / 1_000_000;
    }

    /**
     * Gets the nanosecond equivalent to the specified number of milliseconds.
     *
     * @param millis The milliseconds.
     * @return The equivalent number of nanoseconds.
     */
    public static long toNanos(long millis) {
        return millis * 1_000_000;
    }

    /**
     * Gets the system's nano time and converts it to milliseconds.
     *
     * @return The system's millisecond equivalent of its nanoseconds.
     */
    public static long millis() {
        return toMillis(System.nanoTime());
    }

    /**
     * Formats a length of time.
     *
     * @param time The time to format.
     * @return The String of time formatted into days/hours/minutes/seconds.
     */
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

    /**
     * Gets the number of items completed hourly using the specified length of time elapsed and actions performed.
     *
     * @param elapsed The length of time elapsed.
     * @param actions The number of actions performed thus far.
     * @return The number of hourly items completed.
     */
    public static int hourly(long elapsed, int actions) {
        return elapsed > 0 ? (int) (actions * 3600000D / elapsed) : 0;
    }

    /**
     * Gets the machine's time.
     *
     * @param colon <t>true</t> to use colons; otherwise, <t>false</t>.
     * @return The String representation of the machine's time.
     */
    public static String machineTime(boolean colon) {
        return new SimpleDateFormat(colon ? COLON_FORMAT :
                NON_COLON_FORMAT).format(new Date());
    }

    /**
     * Gets the machine's time.
     *
     * @return The String representation of the machine's time using colons.
     */
    public static String machineTime() {
        return machineTime(true);
    }

    /**
     * Determines if the time is overdue.
     *
     * @return <t>true</t> if the time is overdue; otherwise, <t>false</t>.
     */
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