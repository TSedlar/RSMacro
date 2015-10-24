package rs.macro.internal.mdn;

import rs.macro.api.Macro;
import rs.macro.api.Manifest;

/**
 * @author Tyler Sedlar
 * @since 4/30/15
 */
public class MacroDefinition {

    private final Class<? extends Macro> mainClass;
    private final Manifest manifest;

    public MacroDefinition(Class<? extends Macro> mainClass) {
        this.mainClass = mainClass;
        this.manifest = mainClass.getAnnotation(Manifest.class);
    }

    /**
     * The Class that extends Macro.
     *
     * @return The Class that extends Macro.
     */
    public Class<? extends Macro> mainClass() {
        return mainClass;
    }

    /**
     * The @Manifest annotation that the Class holds.
     *
     * @return The @Manifest annotation that the Class holds.
     */
    public Manifest manifest() {
        return manifest;
    }
}