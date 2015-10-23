package rs.macro.api.util.filter;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public interface DualFilter<K, V> {

    boolean accept(K k, V v);
}
