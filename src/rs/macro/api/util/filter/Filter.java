package rs.macro.api.util.filter;

/**
 * @author Tyler Sedlar
 * @since 4/30/15
 */
public interface Filter<E> {

    boolean accept(E e);
}