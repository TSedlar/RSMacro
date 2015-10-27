package rs.macro.api.util;

/**
 * @author Tyler Sedlar, Jacob Doiron
 * @since 4/30/15
 */
public interface Condition {

    boolean met();

    default Condition inverse() {
        return () -> !met();
    }
}