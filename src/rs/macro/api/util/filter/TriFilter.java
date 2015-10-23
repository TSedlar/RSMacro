package rs.macro.api.util.filter;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public interface TriFilter<K, V, M> {

    boolean accept(K k, V v, M m);
}
