package rs.macro.api.methods;

import rs.macro.RSMacro;
import rs.macro.api.data.BankModel;
import rs.macro.api.methods.input.Mouse;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 10/22/15
 */
public class Bank {

    public static final PixelModel VIEWING_BANK = PixelModel.fromString("#FF981F/10 #FF981F/10/25/-1 #FF981F/10/83/0 #FF981F/10/118/-2 #FF981F/10/-147/-2");
    public static final PixelModel VIEWING_PIN = PixelModel.fromString("#FFFF00/10 #FFFF00/10/-2/10 #FFFF00/10/15/5 #FFFF00/10/31/5 #FFFF00/10/46/10");

    public static BankModel model() {
        return RSMacro.instance().selector().dataSelector().bank();
    }

    /**
     * Tells us if the bank is currently open.
     *
     * @return <t>true</t> if the bank is currently open; otherwise, <t>false</t>.
     */
    public static boolean open() {
        if (!Camera.setAngle(model().angle().value)) {
            return false;
        }
        Point center = Viewport.center();
        Point nearest = RuneScape.pixels().operator().builder()
                .model(model().model()).query().sorted(
                        (a, b) -> Double.compare(a.distance(center), b.distance(center))
                ).findFirst().orElse(null);
        if (nearest != null) {
            Mouse.click(nearest, true);
            return Time.waitFor(2500, Bank::viewing);
        } else {
            return false;
        }
    }

    /**
     * Tells us if the bank is currently being viewed.
     *
     * @return <t>true</t> if we are viewing the bank; otherwise, <t>false</t>.
     */
    public static boolean viewing() {
        return RuneScape.pixels().operator().builder()
                .bounds(22, 10, 319, 25)
                .model(VIEWING_BANK)
                .query().count() > 0;
    }

    /**
     * Tells us if the bank pin screen is being shown.
     *
     * @return <t>true</t> if the bank pin screen is open; otherwise, <t>false</t>.
     */
    public static boolean viewingPin() {
        return RuneScape.pixels().operator().builder()
                .bounds(425, 32, 67, 19)
                .model(VIEWING_PIN)
                .query().count() > 0;
    }
}
