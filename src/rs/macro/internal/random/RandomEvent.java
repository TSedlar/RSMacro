package rs.macro.internal.random;

/**
 * @author Tyler Sedlar
 * @since 10/23/15
 */
public abstract class RandomEvent {

    protected boolean solving = false;

    public abstract boolean activate();

    public abstract boolean solve();

    public boolean solving() {
        return solving;
    }
}
