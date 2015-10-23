package rs.macro.internal.mdn;

import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.util.filter.Filter;

import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * @author Tyler Sedlar
 * @since 4/30/15
 */
public abstract class MacroLoader<T> implements Filter<Class<?>> {

    public abstract void parse(T t) throws IOException, ClassNotFoundException;

    public abstract MacroDefinition[] definitions();

    @Override
    public boolean accept(Class<?> c) {
        return !Modifier.isAbstract(c.getModifiers()) &&
                Macro.class.isAssignableFrom(c) &&
                c.isAnnotationPresent(Manifest.class);
    }
}