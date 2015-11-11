package rs.macro.internal.random;

/**
 * @author Tyler Sedlar
 * @since 10/23/15
 */
public abstract class RandomEvent {

    protected boolean solving = false;

    /**
     * The method that determines whether this RandomEvent is active or not.
     *
     * @return <t>true</t> to activate this event, otherwise <t>false</t>.
     */
    public abstract boolean activate();

    /**
     * Solves the RandomEvent.
     *
     * @return <t>true</t> if the event has been solved, otherwise <t>false</t>.
     */
    public abstract boolean solve();

    /**
     * Finishes up the random handler's necessary steps.
     *
     * @return <t>true</t> if the event was solved and finished its finish method; otherwise, <t>false</t>.
     */
    public abstract boolean atFinish();

    /**
     * Checks whether the RandomEvent is being solved or not.
     *
     * @return <t>true</t> if the RandomEvent is being solved, otherwise <t>false</t>.
     */
    public boolean solving() {
        return solving;
    }
}
