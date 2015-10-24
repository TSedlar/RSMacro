package rs.macro.internal.random.event;

import rs.macro.api.methods.Bank;
import rs.macro.api.methods.RuneScape;
import rs.macro.api.methods.input.Mouse;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.model.PixelModel;
import rs.macro.internal.CacheData;
import rs.macro.internal.random.RandomEvent;

import java.awt.*;

/**
 * @author Tyler Sedlar
 * @since 10/23/15
 */
public class BankPin extends RandomEvent implements Renderable {

    private static final PixelModel QUESTION_MODEL = PixelModel.fromString("#FFFF00/5 #FFFF00/5/1/0 #FFFF00/5/2/0 #FFFF00/5/-1/1 #FFFF00/5/3/1 #FFFF00/5/-1/2 #FFFF00/5/3/2 #FFFF00/5/3/3 #FFFF00/5/2/4 #FFFF00/5/1/5 #FFFF00/5/0/6 #FFFF00/5/0/7 #FFFF00/5/0/9 #FFFF00/5/0/10");

    private static final Rectangle QUESTION_BOUNDS = new Rectangle(426, 31, 65, 20);

    private Rectangle bounds = null;
    private int stage = -1;

    /**
     * Bounds of the pin numbers, 94x/72y pixels in between.
     * The last box is only 84x pixels in between.
     */
    private static final Rectangle[] NUMBER_BOUNDS = {
            new Rectangle(37, 107, 63, 63),
            new Rectangle(37, 179, 63, 63),
            new Rectangle(37, 251, 63, 63),

            new Rectangle(131, 107, 63, 63),
            new Rectangle(131, 179, 63, 63),
            new Rectangle(131, 251, 63, 63),

            new Rectangle(225, 107, 63, 63),
            new Rectangle(225, 179, 63, 63),
            new Rectangle(225, 251, 63, 63),

            new Rectangle(309, 107, 63, 63),
    };

    /**
     * Number PixelModels in order (0-9)
     */
    private static final PixelModel[] NUMBER_MODELS = {
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/2/0 #FF7F00/5/-1/1 #FF7F00/5/0/1 #FF7F00/5/2/1 #FF7F00/5/3/1 #FF7F00/5/-2/2 #FF7F00/5/-1/2 #FF7F00/5/2/2 #FF7F00/5/3/2 #FF7F00/5/4/2 #FF7F00/5/-2/3 #FF7F00/5/-1/3 #FF7F00/5/2/3 #FF7F00/5/3/3 #FF7F00/5/4/3 #FF7F00/5/-2/4 #FF7F00/5/-1/4 #FF7F00/5/1/4 #FF7F00/5/3/4 #FF7F00/5/4/4 #FF7F00/5/-2/5 #FF7F00/5/-1/5 #FF7F00/5/1/5 #FF7F00/5/3/5 #FF7F00/5/4/5 #FF7F00/5/-2/6 #FF7F00/5/-1/6 #FF7F00/5/0/6 #FF7F00/5/3/6 #FF7F00/5/4/6 #FF7F00/5/-2/7 #FF7F00/5/-1/7 #FF7F00/5/0/7 #FF7F00/5/3/7 #FF7F00/5/4/7 #FF7F00/5/-1/8 #FF7F00/5/0/8 #FF7F00/5/2/8 #FF7F00/5/3/8 #FF7F00/5/0/9 #FF7F00/5/1/9 #FF7F00/5/2/9"),
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/-1/1 #FF7F00/5/0/1 #FF7F00/5/1/1 #FF7F00/5/-2/2 #FF7F00/5/-1/2 #FF7F00/5/0/2 #FF7F00/5/1/2 #FF7F00/5/0/3 #FF7F00/5/1/3 #FF7F00/5/0/4 #FF7F00/5/1/4 #FF7F00/5/0/5 #FF7F00/5/1/5 #FF7F00/5/0/6 #FF7F00/5/1/6 #FF7F00/5/0/7 #FF7F00/5/1/7 #FF7F00/5/0/8 #FF7F00/5/1/8 #FF7F00/5/-2/9 #FF7F00/5/-1/9 #FF7F00/5/0/9 #FF7F00/5/1/9 #FF7F00/5/2/9 #FF7F00/5/3/9"),
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/2/0 #FF7F00/5/3/0 #FF7F00/5/-1/1 #FF7F00/5/0/1 #FF7F00/5/3/1 #FF7F00/5/4/1 #FF7F00/5/4/2 #FF7F00/5/5/2 #FF7F00/5/4/3 #FF7F00/5/5/3 #FF7F00/5/3/4 #FF7F00/5/4/4 #FF7F00/5/2/5 #FF7F00/5/3/5 #FF7F00/5/1/6 #FF7F00/5/2/6 #FF7F00/5/0/7 #FF7F00/5/1/7 #FF7F00/5/-1/8 #FF7F00/5/0/8 #FF7F00/5/-1/9 #FF7F00/5/0/9 #FF7F00/5/1/9 #FF7F00/5/2/9 #FF7F00/5/3/9 #FF7F00/5/4/9 #FF7F00/5/5/9"),
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/2/0 #FF7F00/5/3/0 #FF7F00/5/-1/1 #FF7F00/5/0/1 #FF7F00/5/3/1 #FF7F00/5/4/1 #FF7F00/5/3/2 #FF7F00/5/4/2 #FF7F00/5/3/3 #FF7F00/5/4/3 #FF7F00/5/0/4 #FF7F00/5/1/4 #FF7F00/5/2/4 #FF7F00/5/3/4 #FF7F00/5/3/5 #FF7F00/5/4/5 #FF7F00/5/3/6 #FF7F00/5/4/6 #FF7F00/5/3/7 #FF7F00/5/4/7 #FF7F00/5/-1/8 #FF7F00/5/0/8 #FF7F00/5/3/8 #FF7F00/5/4/8 #FF7F00/5/0/9 #FF7F00/5/1/9 #FF7F00/5/2/9 #FF7F00/5/3/9"),
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/0/1 #FF7F00/5/1/1 #FF7F00/5/0/2 #FF7F00/5/1/2 #FF7F00/5/0/3 #FF7F00/5/1/3 #FF7F00/5/0/4 #FF7F00/5/1/4 #FF7F00/5/3/4 #FF7F00/5/4/4 #FF7F00/5/0/5 #FF7F00/5/1/5 #FF7F00/5/3/5 #FF7F00/5/4/5 #FF7F00/5/0/6 #FF7F00/5/1/6 #FF7F00/5/3/6 #FF7F00/5/4/6 #FF7F00/5/0/7 #FF7F00/5/1/7 #FF7F00/5/2/7 #FF7F00/5/3/7 #FF7F00/5/4/7 #FF7F00/5/5/7 #FF7F00/5/6/7 #FF7F00/5/3/8 #FF7F00/5/4/8 #FF7F00/5/3/9 #FF7F00/5/4/9"),
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/2/0 #FF7F00/5/3/0 #FF7F00/5/4/0 #FF7F00/5/5/0 #FF7F00/5/0/1 #FF7F00/5/1/1 #FF7F00/5/0/2 #FF7F00/5/1/2 #FF7F00/5/0/3 #FF7F00/5/1/3 #FF7F00/5/2/3 #FF7F00/5/3/3 #FF7F00/5/3/4 #FF7F00/5/4/4 #FF7F00/5/4/5 #FF7F00/5/5/5 #FF7F00/5/4/6 #FF7F00/5/5/6 #FF7F00/5/4/7 #FF7F00/5/5/7 #FF7F00/5/0/8 #FF7F00/5/1/8 #FF7F00/5/4/8 #FF7F00/5/5/8 #FF7F00/5/1/9 #FF7F00/5/2/9 #FF7F00/5/3/9 #FF7F00/5/4/9"),
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/2/0 #FF7F00/5/-1/1 #FF7F00/5/0/1 #FF7F00/5/2/1 #FF7F00/5/3/1 #FF7F00/5/-2/2 #FF7F00/5/-1/2 #FF7F00/5/-2/3 #FF7F00/5/-1/3 #FF7F00/5/-2/4 #FF7F00/5/-1/4 #FF7F00/5/0/4 #FF7F00/5/1/4 #FF7F00/5/2/4 #FF7F00/5/-2/5 #FF7F00/5/-1/5 #FF7F00/5/0/5 #FF7F00/5/2/5 #FF7F00/5/3/5 #FF7F00/5/-2/6 #FF7F00/5/-1/6 #FF7F00/5/3/6 #FF7F00/5/4/6 #FF7F00/5/-2/7 #FF7F00/5/-1/7 #FF7F00/5/3/7 #FF7F00/5/4/7 #FF7F00/5/-1/8 #FF7F00/5/0/8 #FF7F00/5/2/8 #FF7F00/5/3/8 #FF7F00/5/0/9 #FF7F00/5/1/9 #FF7F00/5/2/9"),
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/2/0 #FF7F00/5/3/0 #FF7F00/5/4/0 #FF7F00/5/5/0 #FF7F00/5/4/1 #FF7F00/5/5/1 #FF7F00/5/3/2 #FF7F00/5/4/2 #FF7F00/5/3/3 #FF7F00/5/4/3 #FF7F00/5/2/4 #FF7F00/5/3/4 #FF7F00/5/2/5 #FF7F00/5/3/5 #FF7F00/5/1/6 #FF7F00/5/2/6 #FF7F00/5/1/7 #FF7F00/5/2/7 #FF7F00/5/0/8 #FF7F00/5/1/8 #FF7F00/5/0/9 #FF7F00/5/1/9"),
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/2/0 #FF7F00/5/-1/1 #FF7F00/5/0/1 #FF7F00/5/2/1 #FF7F00/5/3/1 #FF7F00/5/-2/2 #FF7F00/5/-1/2 #FF7F00/5/3/2 #FF7F00/5/4/2 #FF7F00/5/-1/3 #FF7F00/5/0/3 #FF7F00/5/2/3 #FF7F00/5/3/3 #FF7F00/5/0/4 #FF7F00/5/1/4 #FF7F00/5/2/4 #FF7F00/5/-1/5 #FF7F00/5/0/5 #FF7F00/5/2/5 #FF7F00/5/3/5 #FF7F00/5/-2/6 #FF7F00/5/-1/6 #FF7F00/5/3/6 #FF7F00/5/4/6 #FF7F00/5/-2/7 #FF7F00/5/-1/7 #FF7F00/5/3/7 #FF7F00/5/4/7 #FF7F00/5/-1/8 #FF7F00/5/0/8 #FF7F00/5/2/8 #FF7F00/5/3/8 #FF7F00/5/0/9 #FF7F00/5/1/9 #FF7F00/5/2/9"),
            PixelModel.fromString("#FF7F00/5 #FF7F00/5/1/0 #FF7F00/5/2/0 #FF7F00/5/3/0 #FF7F00/5/4/0 #FF7F00/5/-1/1 #FF7F00/5/0/1 #FF7F00/5/4/1 #FF7F00/5/5/1 #FF7F00/5/-1/2 #FF7F00/5/0/2 #FF7F00/5/4/2 #FF7F00/5/5/2 #FF7F00/5/-1/3 #FF7F00/5/0/3 #FF7F00/5/4/3 #FF7F00/5/5/3 #FF7F00/5/0/4 #FF7F00/5/1/4 #FF7F00/5/4/4 #FF7F00/5/5/4 #FF7F00/5/1/5 #FF7F00/5/2/5 #FF7F00/5/3/5 #FF7F00/5/4/5 #FF7F00/5/5/5 #FF7F00/5/4/6 #FF7F00/5/5/6 #FF7F00/5/4/7 #FF7F00/5/5/7 #FF7F00/5/4/8 #FF7F00/5/5/8 #FF7F00/5/4/9 #FF7F00/5/5/9")
    };

    private Rectangle findNumberBounds(int number) {
        PixelModel model = NUMBER_MODELS[number];
        for (Rectangle bounds : NUMBER_BOUNDS) {
            if (RuneScape.pixels().operator().builder()
                    .bounds(bounds)
                    .model(model)
                    .query().count() > 0) {
                return bounds;
            }
        }
        return null;
    }

    private int questions() {
        return (int) RuneScape.pixels().operator().builder()
                .bounds(QUESTION_BOUNDS)
                .model(QUESTION_MODEL)
                .query().count();
    }

    private int stage() {
        return (4 - questions());
    }

    @Override
    public boolean activate() {
        return Bank.viewingPin();
    }

    @Override
    public boolean solve() {
        stage = stage();
        String pin = CacheData.pin();
        if (pin == null) {
            CacheData.parseLogin();
            pin = CacheData.pin();
        }
        int currentNumber = Character.getNumericValue(pin.charAt(stage));
        bounds = findNumberBounds(currentNumber);
        if (bounds != null) {
            Mouse.click(bounds, true);
            if (Time.waitFor(2500, () -> stage() != stage)) {
                if (questions() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(Graphics2D g) {
        if (bounds != null) {
            g.setColor(Color.GREEN);
            g.draw(bounds);
        }
    }
}
