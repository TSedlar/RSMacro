package rs.macro.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Tyler Sedlar
 * @since 4/30/15
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Manifest {

    String author();

    String name();

    String description();

    boolean banks();
}