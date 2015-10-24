package rs.macro.internal.random;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Tyler Sedlar
 * @since 10/23/15
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomManifest {

    String name();

    String author();

    String version();
}
