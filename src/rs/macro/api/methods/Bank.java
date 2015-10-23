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

    public static BankModel model() {
        return RSMacro.instance().selector().dataSelector().bank();
    }

    public static boolean open() {
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

    public static boolean viewing() {
        return RuneScape.pixels().operator().builder()
                .bounds(22, 10, 319, 25)
                .model(VIEWING_BANK)
                .query().count() > 0;
    }
}
