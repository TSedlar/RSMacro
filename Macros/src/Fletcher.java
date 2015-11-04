import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.access.*;
import rs.macro.api.access.input.Keyboard;
import rs.macro.api.access.input.Mouse;
import rs.macro.api.access.minimap.Minimap;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.MousePaint;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.api.util.fx.Text;
import rs.macro.api.util.fx.listener.PixelListener;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Tyler Sedlar
 * @since 10/21/15
 */
@Manifest(name = "Fletcher", author = "Tyler", description = "Fletches bows",
        version = "1.0.0", banks = false)
public class Fletcher extends Macro implements Renderable, PixelListener {

    private static final Color MOUSE_OUTER = new Color(200, 85, 70);
    private static final Color MOUSE_INNER = new Color(240, 245, 45);
    private static final Color MOUSE_WAVE = new Color(240, 245, 45);
    private static final Color MOUSE_TRAIL = new Color(200, 85, 70);

    private static final int TARGET_ANGLE = 206;

    private static final PixelModel BANKER = PixelModel.fromString("#8E6523/10 #946926/10/8/-1 #976C26/10/3/-2 #C0956E/10/5/7");
    private static final PixelModel KNIFE = PixelModel.fromString("#766E6D/5 #766E6D/5/1/0 #766E6D/5/2/0 #726969/5/3/0 #A29695/5/4/0 #78706F/5/0/1 #78706F/5/1/1 #746B6A/5/2/1 #6D6565/5/3/1 #7A7171/5/-1/2 #7A7171/5/0/2 #766E6D/5/1/2 #6F6767/5/2/2 #A79B9A/5/3/2 #7C7373/5/-1/3 #7A7171/5/0/3 #726969/5/1/3 #998E8D/5/2/3 #7E7575/5/-2/4 #7C7373/5/-1/4 #766E6D/5/0/4 #6D6565/5/1/4 #AA9E9E/5/2/4 #7E7575/5/-2/5 #78706F/5/-1/5 #6F6767/5/0/5 #9C9191/5/1/5 #807777/5/-3/6 #7A7171/5/-2/6 #746B6A/5/-1/6 #6A6363/5/0/6 #AEA3A3/5/1/6 #7C7373/5/-3/7 #766E6D/5/-2/7 #6F6767/5/-1/7 #A29695/5/0/7 #553908/5/-4/8 #553908/5/-3/8 #A59999/5/-2/8 #938988/5/-1/8 #B2A7A6/5/0/8 #553908/5/-5/9 #553908/5/-4/9 #553908/5/-3/9 #553908/5/-2/9 #A59999/5/-1/9 #553908/5/-5/10 #553908/5/-4/10 #553908/5/-3/10 #553908/5/-2/10 #6B480D/5/-1/10 #553908/5/-6/11 #553908/5/-5/11 #553908/5/-4/11 #553908/5/-3/11 #6B480D/5/-2/11 #553908/5/-6/12 #553908/5/-5/12 #553908/5/-4/12 #6B480D/5/-3/12 #553908/5/-7/13 #553908/5/-6/13 #553908/5/-5/13 #6B480D/5/-4/13 #553908/5/-7/14 #553908/5/-6/14 #6B480D/5/-5/14 #553908/5/-8/15 #553908/5/-7/15 #6B480D/5/-6/15 #7C7373/5/-8/16 #9C9191/5/-7/16 #6F6767/5/-9/17 #8C8181/5/-8/17 #7A7171/5/-7/17 #625B5A/5/-10/18 #7A7171/5/-9/18 #887F7E/5/-8/18 #665E5E/5/-10/19 #766E6D/5/-9/19 #7E7575/5/-8/19 #5C5656/5/-10/20 #726969/5/-9/20");

    private static final PixelModel CREATE_IFACE = PixelModel.fromString("#800000/10 #9B9145/10/-128/0 #116861/10/318/-3");
    private static final Rectangle CREATE_IFACE_BOUNDS = new Rectangle(13, 353, 496, 39);

    private static final PixelModel ENTER_AMOUNT_IFACE = PixelModel.fromString("#AC9D81/10 #C9B693/10/106/1 #000000/10/175/3 #D8C5A2/10/211/5 #000000/10/257/3 #C9B693/10/352/0 #BDAD90/10/422/2");
    private static final Rectangle ENTER_AMOUNT_IFACE_BOUNDS = new Rectangle(23, 369, 484, 48);

    private static final PixelModel LEVEL_UP_IFACE = PixelModel.fromString("#AC9D81/10 #000080/10/110/3 #6C140D/10/74/-9 #B3A589/10/455/1");
    private static final Rectangle LEVEL_UP_IFACE_BOUNDS = new Rectangle(18, 360, 490, 72);

    private static final Log LOG = Log.WILLOW;
    private static final FletchType TYPE = FletchType.SHORT_BOW;

    private int fletched = 0;

    @Override
    public void atStart() {
    }

    private boolean setAngle = false;

    @Override
    public int loop() {
        if (!setAngle && Math.abs(Minimap.angle() - TARGET_ANGLE) > 5) {
            setAngle = Camera.setAngle(TARGET_ANGLE);
        } else {
            if (Bank.viewing()) {
                if (Inventory.findSlot(LOG.model) != null) {
                    Bank.close();
                } else {
                    if (Inventory.count() > 1) {
                        Mouse.move(Inventory.SLOTS[1]);
                        if (GameMenu.selectIndex(4)) { // Deposit-All
                            Time.waitFor(2500, () -> !Inventory.hasItem(1));
                        }
                    } else {
                        Rectangle bounds = Bank.findSlot(LOG.model);
                        if (bounds == null) {
                            System.err.println("You are out of logs.");
                            return -1;
                        }
                        Mouse.move(bounds);
                        if (GameMenu.selectIndex(4)) { // Withdraw-All
                            Time.waitFor(2500, () -> Inventory.findSlot(LOG.model) != null);
                        }
                    }
                }
            } else {
                Rectangle knife = Inventory.findSlot(KNIFE);
                if (knife == null) {
                    System.err.println("A knife is needed to fletch!");
                    return -1;
                }
                Rectangle log = Inventory.findSlot(LOG.model);
                if (log != null) {
                    fletch(knife, log);
                } else {
                    openBank();
                }
            }
        }
        return 0;
    }

    private boolean selecting() {
        return operator().builder()
                .bounds(CREATE_IFACE_BOUNDS)
                .model(CREATE_IFACE)
                .first() != null;
    }

    private boolean inputting() {
        return operator().builder()
                .bounds(ENTER_AMOUNT_IFACE_BOUNDS)
                .model(ENTER_AMOUNT_IFACE)
                .first() != null;
    }

    private boolean leveled() {
        return operator().builder()
                .bounds(LEVEL_UP_IFACE_BOUNDS)
                .model(LEVEL_UP_IFACE)
                .first() != null;
    }

    private void fletch(Rectangle knife, Rectangle log) {
        if (selecting()) {
            Mouse.move(TYPE.bounds);
            if (GameMenu.selectIndex(3)) { // Make-X
                Time.waitFor(2500, this::inputting);
            }
        } else if (inputting()) {
            Keyboard.send("99");
            if (Time.waitFor(2500, () -> !inputting())) {
                final AtomicReference<Rectangle> lastSlot =
                        new AtomicReference<>(null);
                Time.waitFor(60000, () -> {
                    if (leveled()) {
                        return true;
                    }
                    Rectangle slot = Inventory.findSlot(LOG.model);
                    if (slot != lastSlot.get() && lastSlot.get() != null) {
                        fletched++;
                    }
                    lastSlot.set(slot);
                    return slot == null;
                });
            }
        } else {
            Mouse.click(knife, true);
            Time.sleep(25, 50);
            Mouse.click(log, true);
            Time.waitFor(2500, this::selecting);
        }
    }

    private boolean openBank() {
        Point center = Viewport.center();
        Point banker = operator().builder()
                .model(BANKER).query()
                .sorted(
                        (a, b) -> Double.compare(a.distance(center), b.distance(center))
                ).findFirst().orElse(null);
        if (banker != null) {
            Mouse.move(banker);
            if (GameMenu.selectIndex(1)) { // Bank
                return Time.waitFor(2500, Bank::viewing);
            }
        }
        return false;
    }

    private static final String RUNTIME_FORMAT = "RUNTIME - %s";
    private static final String FLETCHED_FORMAT = "FLETCHED - %s (%s/HR)";

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        MousePaint.drawMouseWaves(g, MOUSE_WAVE);
        MousePaint.drawTrail(g, MOUSE_TRAIL);
        MousePaint.drawOval(g, MOUSE_OUTER, MOUSE_INNER);
        String runtime = Time.format(runtime());
        Text.drawRuneString(g, String.format(RUNTIME_FORMAT, runtime),
                8, Viewport.height() - 15, Color.YELLOW);
        int hourlyFletched = Time.hourly(runtime(), fletched);
        Text.drawRuneString(g, String.format(FLETCHED_FORMAT, fletched, hourlyFletched),
                8, Viewport.height(), Color.YELLOW);
    }

    @Override
    public void onPixelsUpdated(PixelOperator operator) {

    }

    private enum Log {
        WILLOW("#635B37/5 #69613B/5/1/0 #1F1C11/5/19/0 #70663E/5/-1/1 #70663E/5/0/1 #70663E/5/1/1 #564D1F/5/2/1 #3B3414/5/19/1 #70663E/5/-1/2 #70663E/5/0/2 #70663E/5/1/2 #564D1F/5/2/2 #564D1F/5/3/2 #564D1F/5/10/2 #564D1F/5/11/2 #564D1F/5/12/2 #564D1F/5/13/2 #564D1F/5/14/2 #564D1F/5/15/2 #70663E/5/-1/3 #70663E/5/0/3 #70663E/5/1/3 #6D643C/5/2/3 #564D1F/5/3/3 #564D1F/5/4/3 #564D1F/5/7/3 #564D1F/5/8/3 #564D1F/5/9/3 #564D1F/5/10/3 #564D1F/5/11/3 #564D1F/5/12/3 #564D1F/5/13/3 #564D1F/5/14/3 #564D1F/5/15/3 #3B3414/5/18/3 #70663E/5/-1/4 #70663E/5/0/4 #70663E/5/1/4 #6D643C/5/2/4 #564D1F/5/3/4 #564D1F/5/4/4 #564D1F/5/5/4 #564D1F/5/6/4 #564D1F/5/7/4 #564D1F/5/8/4 #564D1F/5/9/4 #564D1F/5/10/4 #564D1F/5/11/4 #564D1F/5/12/4 #564D1F/5/13/4 #362F11/5/18/4 #70663E/5/-1/5 #70663E/5/0/5 #70663E/5/1/5 #70663E/5/2/5 #70663E/5/3/5 #564D1F/5/4/5 #564D1F/5/5/5 #564D1F/5/6/5 #564D1F/5/7/5 #564D1F/5/8/5 #564D1F/5/9/5 #564D1F/5/10/5 #3B3414/5/16/5 #362F11/5/17/5 #362F11/5/18/5 #70663E/5/-1/6 #70663E/5/0/6 #70663E/5/1/6 #70663E/5/2/6 #564D1F/5/3/6 #564D1F/5/4/6 #564D1F/5/5/6 #564D1F/5/6/6 #564D1F/5/7/6 #6D643C/5/-4/7 #70663E/5/0/7 #70663E/5/1/7 #69613B/5/2/7 #3B3414/5/6/7 #3B3414/5/7/7 #564D1F/5/19/7 #6D643C/5/-4/8 #69613B/5/-3/8 #665E39/5/-2/8 #69613B/5/1/8 #564D1F/5/18/8 #564D1F/5/19/8 #756B42/5/-5/9 #70663E/5/-4/9 #69613B/5/-3/9 #665E39/5/-2/9 #635B37/5/1/9 #564D1F/5/17/9 #564D1F/5/18/9 #564D1F/5/19/9 #70663E/5/-5/10 #70663E/5/-4/10 #6D643C/5/-3/10 #665E39/5/-2/10 #70663E/5/1/10 #69613B/5/2/10 #635B37/5/3/10 #564D1F/5/16/10 #564D1F/5/17/10 #564D1F/5/18/10 #564D1F/5/19/10 #69613B/5/-5/11 #70663E/5/-4/11 #6D643C/5/-3/11 #69613B/5/-2/11 #635B37/5/0/11 #70663E/5/1/11 #70663E/5/2/11 #70663E/5/3/11 #6D643C/5/4/11 #564D1F/5/5/11 #564D1F/5/6/11 #564D1F/5/14/11 #564D1F/5/15/11 #564D1F/5/16/11 #564D1F/5/17/11 #564D1F/5/18/11 #564D1F/5/19/11 #665E39/5/-5/12 #70663E/5/-4/12 #6D643C/5/-3/12 #69613B/5/-2/12 #70663E/5/0/12 #70663E/5/1/12 #70663E/5/2/12 #70663E/5/3/12 #6D643C/5/4/12 #564D1F/5/5/12 #564D1F/5/6/12 #564D1F/5/13/12 #564D1F/5/14/12 #564D1F/5/15/12 #564D1F/5/16/12 #564D1F/5/17/12 #564D1F/5/18/12 #564D1F/5/19/12 #3B3414/5/22/12 #70663E/5/-4/13 #6D643C/5/-3/13 #69613B/5/-2/13 #635B37/5/-1/13 #635B37/5/0/13 #70663E/5/1/13 #70663E/5/2/13 #70663E/5/3/13 #6D643C/5/4/13 #564D1F/5/5/13 #564D1F/5/6/13 #564D1F/5/12/13 #564D1F/5/13/13 #564D1F/5/14/13 #564D1F/5/15/13 #564D1F/5/16/13 #362F11/5/21/13 #2F2B11/5/22/13 #665E39/5/-3/14 #665E39/5/-2/14 #70663E/5/1/14 #70663E/5/2/14 #70663E/5/3/14 #6D643C/5/4/14 #564D1F/5/5/14 #564D1F/5/6/14 #564D1F/5/7/14 #564D1F/5/11/14 #564D1F/5/12/14 #564D1F/5/13/14 #70663E/5/1/15 #70663E/5/2/15 #70663E/5/3/15 #70663E/5/4/15 #564D1F/5/5/15 #564D1F/5/6/15 #564D1F/5/7/15 #564D1F/5/8/15 #70663E/5/1/16 #70663E/5/2/16 #70663E/5/3/16 #6D643C/5/4/16 #6D643C/5/5/16 #564D1F/5/6/16 #564D1F/5/7/16 #70663E/5/2/17 #70663E/5/3/17 #6D643C/5/4/17 #564D1F/5/5/17 #564D1F/5/6/17 #70663E/5/3/18 #564D1F/5/4/18");

        public final PixelModel model;

        Log(String fromString) {
            this.model = PixelModel.fromString(fromString);
        }
    }

    private enum FletchType {
        SHORT_BOW(new Rectangle(43, 380, 119, 72)),
        LONG_BOW(new Rectangle(200, 382, 122, 70)),
        CROSS_BOW(new Rectangle(363, 383, 106, 72));

        public final Rectangle bounds;

        FletchType(Rectangle bounds) {
            this.bounds = bounds;
        }
    }
}
