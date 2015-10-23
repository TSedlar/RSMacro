package rs.macro.api.data;

import rs.macro.api.util.fx.model.PixelModel;

/**
 * @author Tyler Sedlar
 * @since 5/16/15
 */
public enum BankModel {

    CATHERBY,
    EDGEVILLE,
    FALADOR_EAST,
    FALADOR_WEST,
    GRAND_EXCHANGE_EAST,
    GRAND_EXCHANGE_WEST,
    LUMBRIDGE_TOP,
    LUMBRIDGE_BASEMENT,
    LUNAR_ISLE,
    SEERS,
    VARROCK_WEST,
    VARROCK_EAST(Angle.NORTH, "#5A4913/10 #5A4913/10/67/-2 #827B65/10/66/127 #5A4913/10/-51/2"),
    YANILLE;

    private final Angle angle;
    private final PixelModel model;

    BankModel(Angle angle, String model) {
        this.angle = angle;
        this.model = (model != null ? PixelModel.fromString(model) : null);
    }

    BankModel() {
        this(null, null);
    }

    public Angle angle() {
        return angle;
    }

    public PixelModel model() {
        return model;
    }

    private static final String UNDERSCORE = "_";
    private static final String SPACE = " ";

    @Override
    public String toString() {
        String superString = super.toString();
        return (superString.charAt(0) + superString.substring(1).toLowerCase())
                .replaceAll(UNDERSCORE, SPACE);
    }
}