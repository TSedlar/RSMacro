import rs.macro.api.Macro;
import rs.macro.api.Manifest;
import rs.macro.api.access.*;
import rs.macro.api.access.input.Keyboard;
import rs.macro.api.access.input.Mouse;
import rs.macro.api.access.minimap.Minimap;
import rs.macro.api.util.Random;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.MousePaint;
import rs.macro.api.util.fx.PixelOperator;
import rs.macro.api.util.fx.Text;
import rs.macro.api.util.fx.listener.PixelListener;
import rs.macro.api.util.fx.model.PixelModel;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Tyler Sedlar
 * @since 11/11/15
 */
@Manifest(name = "Yew Stringer", author = "Tyler", description = "Strings bows",
        version = "1.0.0", banks = false)
public class Stringer extends Macro implements Renderable, PixelListener {

    private static final Color MOUSE_OUTER = new Color(200, 85, 70);
    private static final Color MOUSE_INNER = new Color(240, 245, 45);
    private static final Color MOUSE_WAVE = new Color(240, 245, 45);
    private static final Color MOUSE_TRAIL = new Color(200, 85, 70);

    private static final int TARGET_ANGLE = 206;

    private static final PixelModel BANKER = PixelModel.fromString("#8E6523/10 #946926/10/8/-1 #976C26/10/3/-2 #C0956E/10/5/7");
    private static final PixelModel BOW_STRING = PixelModel.fromString("#72694E/10 #72694E/10/1/0 #85795B/10/2/0 #161212/10/3/0 #161212/10/4/0 #72694E/10/-2/1 #72694E/10/-1/1 #85795B/10/0/1 #161212/10/1/1 #161212/10/2/1 #72694E/10/-3/2 #72694E/10/-2/2 #85795B/10/-1/2 #85795B/10/0/2 #72694E/10/-3/3 #72694E/10/-2/3 #85795B/10/-1/3 #85795B/10/0/3 #72694E/10/-3/4 #72694E/10/-2/4 #85795B/10/-1/4 #85795B/10/0/4 #85795B/10/1/4 #161212/10/-3/5 #161212/10/-2/5 #72694E/10/-1/5 #72694E/10/0/5 #85795B/10/1/5 #85795B/10/2/5 #85795B/10/3/5 #161212/10/0/6 #72694E/10/1/6 #72694E/10/2/6 #85795B/10/3/6 #85795B/10/4/6 #85795B/10/5/6 #72694E/10/4/7 #85795B/10/5/7 #85795B/10/6/7 #85795B/10/7/7 #85795B/10/8/7 #85795B/10/9/7 #85795B/10/10/7 #85795B/10/11/7 #85795B/10/12/7 #85795B/10/13/7 #85795B/10/14/7 #85795B/10/15/7 #85795B/10/16/7 #85795B/10/17/7 #85795B/10/18/7 #161212/10/9/8 #161212/10/10/8 #72694E/10/15/8 #72694E/10/16/8 #85795B/10/17/8 #85795B/10/18/8 #85795B/10/19/8 #161212/10/15/9 #72694E/10/16/9 #72694E/10/17/9 #72694E/10/18/9 #85795B/10/19/9 #85795B/10/20/9 #85795B/10/21/9 #161212/10/16/10 #161212/10/17/10 #72694E/10/18/10 #72694E/10/19/10 #72694E/10/20/10 #72694E/10/21/10 #85795B/10/22/10 #85795B/10/23/10 #161212/10/18/11 #161212/10/19/11 #72694E/10/20/11 #85795B/10/21/11 #85795B/10/22/11 #161212/10/23/11 #72694E/10/19/12 #85795B/10/20/12 #85795B/10/21/12 #85795B/10/22/12 #72694E/10/18/13 #85795B/10/19/13 #85795B/10/20/13 #85795B/10/21/13 #161212/10/22/13 #72694E/10/16/14 #72694E/10/17/14 #72694E/10/18/14 #85795B/10/19/14 #85795B/10/20/14 #85795B/10/21/14 #72694E/10/14/15 #72694E/10/15/15 #85795B/10/16/15 #85795B/10/17/15 #85795B/10/18/15 #85795B/10/19/15 #85795B/10/20/15 #161212/10/21/15 #72694E/10/12/16 #85795B/10/13/16 #85795B/10/14/16 #85795B/10/15/16 #85795B/10/16/16 #85795B/10/17/16 #85795B/10/18/16 #85795B/10/19/16 #85795B/10/20/16 #85795B/10/-2/17 #85795B/10/-1/17 #85795B/10/0/17 #85795B/10/1/17 #85795B/10/2/17 #85795B/10/3/17 #85795B/10/4/17 #85795B/10/5/17 #85795B/10/6/17 #85795B/10/7/17 #85795B/10/8/17 #85795B/10/9/17 #85795B/10/10/17 #85795B/10/11/17 #85795B/10/12/17 #85795B/10/13/17 #85795B/10/14/17 #161212/10/15/17 #161212/10/16/17 #161212/10/17/17 #161212/10/18/17 #161212/10/19/17 #161212/10/20/17 #161212/10/-2/18 #161212/10/-1/18 #161212/10/0/18 #161212/10/1/18 #161212/10/2/18 #161212/10/3/18 #161212/10/4/18 #161212/10/5/18 #161212/10/6/18 #161212/10/7/18 #161212/10/8/18 #161212/10/9/18 #161212/10/10/18 #161212/10/11/18 #161212/10/12/18 #161212/10/13/18 #161212/10/14/18");

    private static final PixelModel CREATE_IFACE = PixelModel.fromString("#746A22/10 #2F746E/10/0/-2 #B2A384/10/440/0 #3F9F9C/10/444/-2 #800000/10/257/0 #800000/10/183/-2 #800000/10/105/-3");
    private static final Rectangle CREATE_IFACE_BOUNDS = new Rectangle(14, 349, 496, 41);

    private static final PixelModel ENTER_AMOUNT_IFACE = PixelModel.fromString("#000000/2 #000000/2/1/0 #000000/2/2/0 #000000/2/3/0 #000000/2/4/0 #000000/2/5/0 #000000/2/0/1 #000000/2/1/1 #000000/2/15/1 #000000/2/16/1 #000000/2/81/1 #000000/2/82/1 #000000/2/0/2 #000000/2/1/2 #000000/2/15/2 #000000/2/16/2 #000000/2/81/2 #000000/2/82/2 #000000/2/0/3 #000000/2/1/3 #000000/2/15/3 #000000/2/16/3 #000000/2/81/3 #000000/2/82/3 #000000/2/0/4 #000000/2/1/4 #000000/2/2/4 #000000/2/3/4 #000000/2/7/4 #000000/2/8/4 #000000/2/9/4 #000000/2/10/4 #000000/2/11/4 #000000/2/15/4 #000000/2/16/4 #000000/2/17/4 #000000/2/18/4 #000000/2/22/4 #000000/2/23/4 #000000/2/24/4 #000000/2/25/4 #000000/2/29/4 #000000/2/30/4 #000000/2/31/4 #000000/2/32/4 #000000/2/40/4 #000000/2/41/4 #000000/2/42/4 #000000/2/43/4 #000000/2/48/4 #000000/2/49/4 #000000/2/50/4 #000000/2/51/4 #000000/2/52/4 #000000/2/53/4 #000000/2/58/4 #000000/2/59/4 #000000/2/60/4 #000000/2/61/4 #000000/2/65/4 #000000/2/66/4 #000000/2/69/4 #000000/2/70/4 #000000/2/73/4 #000000/2/74/4 #000000/2/75/4 #000000/2/76/4 #000000/2/77/4 #000000/2/81/4 #000000/2/82/4 #000000/2/83/4 #000000/2/84/4 #000000/2/87/4 #000000/2/88/4 #000000/2/0/5 #000000/2/1/5 #000000/2/7/5 #000000/2/8/5 #000000/2/9/5 #000000/2/11/5 #000000/2/12/5 #000000/2/15/5 #000000/2/16/5 #000000/2/21/5 #000000/2/22/5 #000000/2/25/5 #000000/2/26/5 #000000/2/29/5 #000000/2/30/5 #000000/2/31/5 #000000/2/39/5 #000000/2/40/5 #000000/2/43/5 #000000/2/44/5 #000000/2/47/5 #000000/2/48/5 #000000/2/50/5 #000000/2/51/5 #000000/2/53/5 #000000/2/54/5 #000000/2/57/5 #000000/2/58/5 #000000/2/61/5 #000000/2/62/5 #000000/2/65/5 #000000/2/66/5 #000000/2/69/5 #000000/2/70/5 #000000/2/73/5 #000000/2/74/5 #000000/2/75/5 #000000/2/77/5 #000000/2/78/5 #000000/2/81/5 #000000/2/82/5 #000000/2/0/6 #000000/2/1/6 #000000/2/7/6 #000000/2/8/6 #000000/2/11/6 #000000/2/12/6 #000000/2/15/6 #000000/2/16/6 #000000/2/21/6 #000000/2/22/6 #000000/2/25/6 #000000/2/26/6 #000000/2/29/6 #000000/2/30/6 #000000/2/40/6 #000000/2/41/6 #000000/2/42/6 #000000/2/43/6 #000000/2/44/6 #000000/2/47/6 #000000/2/48/6 #000000/2/50/6 #000000/2/51/6 #000000/2/53/6 #000000/2/54/6 #000000/2/57/6 #000000/2/58/6 #000000/2/61/6 #000000/2/62/6 #000000/2/65/6 #000000/2/66/6 #000000/2/69/6 #000000/2/70/6 #000000/2/73/6 #000000/2/74/6 #000000/2/77/6 #000000/2/78/6 #000000/2/81/6 #000000/2/82/6 #000000/2/0/7 #000000/2/1/7 #000000/2/7/7 #000000/2/8/7 #000000/2/11/7 #000000/2/12/7 #000000/2/15/7 #000000/2/16/7 #000000/2/21/7 #000000/2/22/7 #000000/2/23/7 #000000/2/24/7 #000000/2/25/7 #000000/2/29/7 #000000/2/30/7 #000000/2/39/7 #000000/2/40/7 #000000/2/43/7 #000000/2/44/7 #000000/2/47/7 #000000/2/48/7 #000000/2/50/7 #000000/2/51/7 #000000/2/53/7 #000000/2/54/7 #000000/2/57/7 #000000/2/58/7 #000000/2/61/7 #000000/2/62/7 #000000/2/65/7 #000000/2/66/7 #000000/2/69/7 #000000/2/70/7 #000000/2/73/7 #000000/2/74/7 #000000/2/77/7 #000000/2/78/7 #000000/2/81/7 #000000/2/82/7 #000000/2/0/8 #000000/2/1/8 #000000/2/7/8 #000000/2/8/8 #000000/2/11/8 #000000/2/12/8 #000000/2/15/8 #000000/2/16/8 #000000/2/21/8 #000000/2/22/8 #000000/2/29/8 #000000/2/30/8 #000000/2/39/8 #000000/2/40/8 #000000/2/43/8 #000000/2/44/8 #000000/2/47/8 #000000/2/48/8 #000000/2/50/8 #000000/2/51/8 #000000/2/53/8 #000000/2/54/8 #000000/2/57/8 #000000/2/58/8 #000000/2/61/8 #000000/2/62/8 #000000/2/65/8 #000000/2/66/8 #000000/2/69/8 #000000/2/70/8 #000000/2/73/8 #000000/2/74/8 #000000/2/77/8 #000000/2/78/8 #000000/2/81/8 #000000/2/82/8 #000000/2/0/9 #000000/2/1/9 #000000/2/2/9 #000000/2/3/9 #000000/2/4/9 #000000/2/5/9 #000000/2/7/9 #000000/2/8/9 #000000/2/11/9 #000000/2/12/9 #000000/2/16/9 #000000/2/17/9 #000000/2/18/9 #000000/2/22/9 #000000/2/23/9 #000000/2/24/9 #000000/2/25/9 #000000/2/26/9 #000000/2/29/9 #000000/2/30/9 #000000/2/40/9 #000000/2/41/9 #000000/2/42/9 #000000/2/43/9 #000000/2/44/9 #000000/2/47/9 #000000/2/48/9 #000000/2/50/9 #000000/2/51/9 #000000/2/53/9 #000000/2/54/9 #000000/2/58/9 #000000/2/59/9 #000000/2/60/9 #000000/2/61/9 #000000/2/66/9 #000000/2/67/9 #000000/2/68/9 #000000/2/69/9 #000000/2/73/9 #000000/2/74/9 #000000/2/77/9 #000000/2/78/9 #000000/2/82/9 #000000/2/83/9 #000000/2/84/9 #000000/2/87/9 #000000/2/88/9");
    private static final Rectangle ENTER_AMOUNT_IFACE_BOUNDS = new Rectangle(206, 390, 103, 22);

    private static final PixelModel LEVEL_UP_IFACE = PixelModel.fromString("#AC9D81/10 #000080/10/110/3 #6C140D/10/74/-9 #B3A589/10/455/1");
    private static final Rectangle LEVEL_UP_IFACE_BOUNDS = new Rectangle(18, 360, 490, 72);

    private static final PixelModel COLLECT_IFACE = PixelModel.fromString("#FF981F/2 #FF981F/2/1/0 #FF981F/2/2/0 #FF981F/2/14/0 #FF981F/2/15/0 #FF981F/2/18/0 #FF981F/2/19/0 #FF981F/2/67/0 #FF981F/2/68/0 #FF981F/2/69/0 #FF981F/2/70/0 #FF981F/2/71/0 #FF981F/2/-1/1 #FF981F/2/0/1 #FF981F/2/2/1 #FF981F/2/3/1 #FF981F/2/14/1 #FF981F/2/15/1 #FF981F/2/18/1 #FF981F/2/19/1 #FF981F/2/37/1 #FF981F/2/38/1 #FF981F/2/67/1 #FF981F/2/68/1 #FF981F/2/71/1 #FF981F/2/72/1 #FF981F/2/-2/2 #FF981F/2/-1/2 #FF981F/2/14/2 #FF981F/2/15/2 #FF981F/2/18/2 #FF981F/2/19/2 #FF981F/2/37/2 #FF981F/2/38/2 #FF981F/2/67/2 #FF981F/2/68/2 #FF981F/2/71/2 #FF981F/2/72/2 #FF981F/2/-2/3 #FF981F/2/-1/3 #FF981F/2/14/3 #FF981F/2/15/3 #FF981F/2/18/3 #FF981F/2/19/3 #FF981F/2/37/3 #FF981F/2/38/3 #FF981F/2/43/3 #FF981F/2/44/3 #FF981F/2/67/3 #FF981F/2/68/3 #FF981F/2/71/3 #FF981F/2/72/3 #FF981F/2/-2/4 #FF981F/2/-1/4 #FF981F/2/7/4 #FF981F/2/8/4 #FF981F/2/9/4 #FF981F/2/10/4 #FF981F/2/14/4 #FF981F/2/15/4 #FF981F/2/18/4 #FF981F/2/19/4 #FF981F/2/23/4 #FF981F/2/24/4 #FF981F/2/25/4 #FF981F/2/26/4 #FF981F/2/31/4 #FF981F/2/32/4 #FF981F/2/33/4 #FF981F/2/34/4 #FF981F/2/37/4 #FF981F/2/38/4 #FF981F/2/39/4 #FF981F/2/40/4 #FF981F/2/48/4 #FF981F/2/49/4 #FF981F/2/50/4 #FF981F/2/51/4 #FF981F/2/55/4 #FF981F/2/56/4 #FF981F/2/57/4 #FF981F/2/58/4 #FF981F/2/59/4 #FF981F/2/67/4 #FF981F/2/68/4 #FF981F/2/69/4 #FF981F/2/70/4 #FF981F/2/71/4 #FF981F/2/76/4 #FF981F/2/77/4 #FF981F/2/78/4 #FF981F/2/79/4 #FF981F/2/83/4 #FF981F/2/84/4 #FF981F/2/87/4 #FF981F/2/88/4 #FF981F/2/-2/5 #FF981F/2/-1/5 #FF981F/2/6/5 #FF981F/2/7/5 #FF981F/2/10/5 #FF981F/2/11/5 #FF981F/2/14/5 #FF981F/2/15/5 #FF981F/2/18/5 #FF981F/2/19/5 #FF981F/2/22/5 #FF981F/2/23/5 #FF981F/2/26/5 #FF981F/2/27/5 #FF981F/2/30/5 #FF981F/2/31/5 #FF981F/2/37/5 #FF981F/2/38/5 #FF981F/2/43/5 #FF981F/2/44/5 #FF981F/2/47/5 #FF981F/2/48/5 #FF981F/2/51/5 #FF981F/2/52/5 #FF981F/2/55/5 #FF981F/2/56/5 #FF981F/2/57/5 #FF981F/2/59/5 #FF981F/2/60/5 #FF981F/2/67/5 #FF981F/2/68/5 #FF981F/2/71/5 #FF981F/2/72/5 #FF981F/2/75/5 #FF981F/2/76/5 #FF981F/2/79/5 #FF981F/2/80/5 #FF981F/2/84/5 #FF981F/2/85/5 #FF981F/2/86/5 #FF981F/2/87/5 #FF981F/2/-2/6 #FF981F/2/-1/6 #FF981F/2/6/6 #FF981F/2/7/6 #FF981F/2/10/6 #FF981F/2/11/6 #FF981F/2/14/6 #FF981F/2/15/6 #FF981F/2/18/6 #FF981F/2/19/6 #FF981F/2/22/6 #FF981F/2/23/6 #FF981F/2/26/6 #FF981F/2/27/6 #FF981F/2/30/6 #FF981F/2/31/6 #FF981F/2/37/6 #FF981F/2/38/6 #FF981F/2/43/6 #FF981F/2/44/6 #FF981F/2/47/6 #FF981F/2/48/6 #FF981F/2/51/6 #FF981F/2/52/6 #FF981F/2/55/6 #FF981F/2/56/6 #FF981F/2/59/6 #FF981F/2/60/6 #FF981F/2/67/6 #FF981F/2/68/6 #FF981F/2/71/6 #FF981F/2/72/6 #FF981F/2/75/6 #FF981F/2/76/6 #FF981F/2/79/6 #FF981F/2/80/6 #FF981F/2/85/6 #FF981F/2/86/6 #FF981F/2/-2/7 #FF981F/2/-1/7 #FF981F/2/6/7 #FF981F/2/7/7 #FF981F/2/10/7 #FF981F/2/11/7 #FF981F/2/14/7 #FF981F/2/15/7 #FF981F/2/18/7 #FF981F/2/19/7 #FF981F/2/22/7 #FF981F/2/23/7 #FF981F/2/24/7 #FF981F/2/25/7 #FF981F/2/26/7 #FF981F/2/30/7 #FF981F/2/31/7 #FF981F/2/37/7 #FF981F/2/38/7 #FF981F/2/43/7 #FF981F/2/44/7 #FF981F/2/47/7 #FF981F/2/48/7 #FF981F/2/51/7 #FF981F/2/52/7 #FF981F/2/55/7 #FF981F/2/56/7 #FF981F/2/59/7 #FF981F/2/60/7 #FF981F/2/67/7 #FF981F/2/68/7 #FF981F/2/71/7 #FF981F/2/72/7 #FF981F/2/75/7 #FF981F/2/76/7 #FF981F/2/79/7 #FF981F/2/80/7 #FF981F/2/84/7 #FF981F/2/85/7 #FF981F/2/86/7 #FF981F/2/87/7 #FF981F/2/-1/8 #FF981F/2/0/8 #FF981F/2/2/8 #FF981F/2/3/8 #FF981F/2/6/8 #FF981F/2/7/8 #FF981F/2/10/8 #FF981F/2/11/8 #FF981F/2/14/8 #FF981F/2/15/8 #FF981F/2/18/8 #FF981F/2/19/8 #FF981F/2/22/8 #FF981F/2/23/8 #FF981F/2/30/8 #FF981F/2/31/8 #FF981F/2/37/8 #FF981F/2/38/8 #FF981F/2/43/8 #FF981F/2/44/8 #FF981F/2/47/8 #FF981F/2/48/8 #FF981F/2/51/8 #FF981F/2/52/8 #FF981F/2/55/8 #FF981F/2/56/8 #FF981F/2/59/8 #FF981F/2/60/8 #FF981F/2/67/8 #FF981F/2/68/8 #FF981F/2/71/8 #FF981F/2/72/8 #FF981F/2/75/8 #FF981F/2/76/8 #FF981F/2/79/8 #FF981F/2/80/8 #FF981F/2/84/8 #FF981F/2/85/8 #FF981F/2/86/8 #FF981F/2/87/8 #FF981F/2/0/9 #FF981F/2/1/9 #FF981F/2/2/9 #FF981F/2/7/9 #FF981F/2/8/9 #FF981F/2/9/9 #FF981F/2/10/9 #FF981F/2/14/9 #FF981F/2/15/9 #FF981F/2/18/9 #FF981F/2/19/9 #FF981F/2/23/9 #FF981F/2/24/9 #FF981F/2/25/9 #FF981F/2/26/9 #FF981F/2/27/9 #FF981F/2/31/9 #FF981F/2/32/9 #FF981F/2/33/9 #FF981F/2/34/9 #FF981F/2/38/9 #FF981F/2/39/9 #FF981F/2/40/9 #FF981F/2/43/9 #FF981F/2/44/9 #FF981F/2/48/9 #FF981F/2/49/9 #FF981F/2/50/9 #FF981F/2/51/9 #FF981F/2/55/9 #FF981F/2/56/9 #FF981F/2/59/9 #FF981F/2/60/9 #FF981F/2/67/9 #FF981F/2/68/9 #FF981F/2/69/9 #FF981F/2/70/9 #FF981F/2/71/9 #FF981F/2/76/9 #FF981F/2/77/9 #FF981F/2/78/9 #FF981F/2/79/9 #FF981F/2/83/9 #FF981F/2/84/9 #FF981F/2/87/9 #FF981F/2/88/9");
    private static final Rectangle COLLECT_IFACE_BOUNDS = new Rectangle(208, 56, 103, 18);
    private static final Rectangle COLLECT_IFACE_CLOSE_BOUNDS = new Rectangle(465, 58, 14, 15);

    private static final Bow BOW = Bow.YEW;
    private static final Rectangle BOW_BOUNDS = new Rectangle(213, 401, 89, 54);

    private int fletched = 0;
    private String status = "N/A";

    @Override
    public void atStart() {
        Mouse.setSpeed(1);
    }

    private boolean setAngle = false;

    private void setStatus(String status) {
        this.status = status;
        System.out.println(status);
    }

    @Override
    public int loop() {
        if (!setAngle && Math.abs(Minimap.angle() - TARGET_ANGLE) > 5) {
            setStatus("Setting angle");
            setAngle = Camera.setAngle(TARGET_ANGLE);
        } else if (collecting()) {
            setStatus("Closing collect");
            closeCollect();
        } else {
            if (Bank.viewing()) {
                boolean hasStrings = (Inventory.findSlot(BOW_STRING) != null);
                boolean hasBows = (Inventory.findSlot(BOW.model) != null);
                if (hasStrings && hasBows) {
                    setStatus("Closing bank");
                    Bank.close();
                } else {
                    if (Inventory.count() > 1 && !hasStrings) {
                        setStatus("Depositing items");
                        Rectangle slot = Inventory.findLastValidSlot();
                        if (slot != null) {
                            Mouse.move(slot);
                            if (GameMenu.selectIndex(3)) { // Deposit-14
                                Time.waitFor(2500, () -> !Inventory.hasItem(
                                        Arrays.asList(Inventory.SLOTS).indexOf(slot))
                                );
                            }
                        }
                    } else {
                        setStatus("Withdrawing items");
                        Rectangle bowString = Bank.findSlot(BOW_STRING);
                        if (bowString == null) {
                            System.out.println("You are out of bow strings.");
                            return -1;
                        }
                        Rectangle bow = Bank.findSlot(BOW.model);
                        if (bow == null) {
                            System.out.println("You are out of logs.");
                            return -1;
                        }
                        if (!hasStrings) {
                            Mouse.move(bowString);
                            if (GameMenu.selectIndex(3)) { // Withdraw-14
                                Time.waitFor(2500, () -> Inventory.findSlot(BOW_STRING) != null);
                            }
                        } else {
                            Mouse.move(bow);
                            if (GameMenu.selectIndex(3)) { // Withdraw-14
                                Time.waitFor(2500, () -> Inventory.findSlot(BOW.model) != null);
                            }
                        }
                    }
                }
            } else {
                Rectangle bowString = Inventory.findSlot(BOW_STRING);
                Rectangle log = Inventory.findSlot(BOW.model);
                if (bowString != null && log != null) {
                    setStatus("Fletching");
                    fletch(bowString, log);
                } else {
                    setStatus("Opening bank");
                    openBank();
                }
            }
        }
        return Random.nextInt(50, 100);
    }

    private boolean collecting() {
        return operator().builder()
                .bounds(COLLECT_IFACE_BOUNDS)
                .model(COLLECT_IFACE)
                .first() != null;
    }

    private boolean closeCollect() {
        Mouse.click(COLLECT_IFACE_CLOSE_BOUNDS, true);
        return Time.waitFor(2500, () -> !collecting());
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
            setStatus("Selecting Make-All");
            Mouse.move(BOW_BOUNDS);
            if (GameMenu.selectIndex(3)) { // Make-All
                if (Time.waitFor(2500, () -> !this.selecting())) {
                    setStatus("Fletching bows");
                    final AtomicReference<Rectangle> lastSlot =
                            new AtomicReference<>(null);
                    Time.waitFor(60000, () -> {
                        if (leveled()) {
                            setStatus("Leveled up");
                            return true;
                        }
                        Rectangle slot = Inventory.findSlot(BOW_STRING);
                        if (slot != lastSlot.get() && lastSlot.get() != null) {
                            fletched++;
                        }
                        lastSlot.set(slot);
                        return slot == null;
                    });
                    setStatus("Finished fletching");
                }
            }
        } else {
            setStatus("Using items");
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
    private static final String STATUS_FORMAT = "STATUS - %s";

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        MousePaint.drawMouseWaves(g, MOUSE_WAVE);
        MousePaint.drawTrail(g, MOUSE_TRAIL);
        MousePaint.drawOval(g, MOUSE_OUTER, MOUSE_INNER);
        String runtime = Time.format(runtime());
        Text.drawRuneString(g, String.format(RUNTIME_FORMAT, runtime),
                8, Viewport.height() - 30, Color.YELLOW);
        int hourlyFletched = Time.hourly(runtime(), fletched);
        Text.drawRuneString(g, String.format(FLETCHED_FORMAT, fletched, hourlyFletched),
                8, Viewport.height() - 15, Color.YELLOW);
        Text.drawRuneString(g, String.format(STATUS_FORMAT, status.toUpperCase()),
                8, Viewport.height(), Color.YELLOW);
    }

    @Override
    public void onPixelsUpdated(PixelOperator operator) {

    }

    private enum Bow {
        YEW("#7A6C0E/10 #B6A016/10/1/0 #5D5209/10/0/1 #9A8812/10/1/1 #7A6C0E/10/1/2 #C9B019/10/2/2 #938112/10/2/3 #C6AE19/10/3/3 #8D7B12/10/3/4 #C0A919/10/4/4 #89780E/10/4/5 #BBA516/10/5/5 #6B5D09/10/5/6 #B6A016/10/6/6 #B39D16/10/7/7 #907E12/10/7/8 #6B5D09/10/7/9 #A79216/10/8/10 #86750E/10/8/11 #A49012/10/9/12 #9D8A12/10/9/13 #7A6C0E/10/9/14 #9D8A12/10/10/14 #978512/10/10/15 #938112/10/10/16 #978512/10/11/16 #938112/10/11/17 #8D7B12/10/11/18 #938112/10/12/18");
        public final PixelModel model;

        Bow(String fromString) {
            this.model = PixelModel.fromString(fromString);
        }
    }
}
