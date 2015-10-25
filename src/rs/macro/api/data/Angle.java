package rs.macro.api.data;

/**
 * @author Tyler Sedlar
 * @since 5/16/15
 */
public enum Angle {

    NORTH(0),
    EAST(90),
    SOUTH(180),
    WEST(270);

    public final int value;

    /**
     * Creates an Angle enum with the following argument:
     *
     * @param value The angle to represent.
     */
    Angle(int value) {
        this.value = value;
    }
}