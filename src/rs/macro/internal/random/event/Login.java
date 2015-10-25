package rs.macro.internal.random.event;

import rs.macro.api.access.RuneScape;
import rs.macro.api.access.input.Keyboard;
import rs.macro.api.access.input.Mouse;
import rs.macro.api.util.Renderable;
import rs.macro.api.util.Time;
import rs.macro.api.util.fx.model.PixelModel;
import rs.macro.internal.CacheData;
import rs.macro.internal.random.RandomEvent;
import rs.macro.internal.random.RandomManifest;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * @author Jacob
 * @since 10/25/2015
 */
@RandomManifest(name = "Login Solver", author = "Jacob Doiron", version = "1.0.0")
public class Login extends RandomEvent implements Renderable {

    private boolean sent;

    private Rectangle bounds;

    private static final Rectangle[] STAGE_BOUNDS = {
            new Rectangle(266, 281, 246, 21), // new user/existing user
            new Rectangle(279, 311, 215, 21), // enter user details
            new Rectangle(352, 323, 136, 26) // click here to play
    };

    private static final Rectangle[] CLICK_BOUNDS = {
            new Rectangle(395, 275, 135, 32), // existing user
            new Rectangle(274, 296, 221, 81) // click here to play
    };

    private static final PixelModel[] SCREEN_MODELS = {
            PixelModel.fromString("#FFFFFF/10 #FFFFFF/10/232/4"), // new user/existing user
            PixelModel.fromString("#FFFFFF/10 #FFFFFF/10/196/9"), // login/cancel
            PixelModel.fromString("#FFFFFF/10 #FFFFFF/10/115/8") // click here to play
    };

    private int stage() {
        for (int i = 0; i < SCREEN_MODELS.length; i++) {
            if (RuneScape.pixels().operator().builder().bounds(STAGE_BOUNDS[i]).model(SCREEN_MODELS[i]).first() != null) {
                bounds = i == 1 ? null : i == 2 ? CLICK_BOUNDS[1] : CLICK_BOUNDS[0];
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean activate() {
        return !RuneScape.playing();
    }

    @Override
    public boolean solve() {
        int stage = stage();
        String user = CacheData.user();
        String pass = CacheData.pass();
        if (user == null || pass == null) {
            CacheData.parseLogin();
            user = CacheData.user();
            pass = CacheData.pass();
        }
        if (stage == -1) {
            return true;
        }
        if (stage == 0) {
            Mouse.click(CLICK_BOUNDS[0], true);
            Time.waitFor(1500, () -> stage() == 1);
        }
        if (stage == 1) {
            if (!sent) {
                Keyboard.send(user);
                Time.sleep(150, 500);
                Keyboard.send(pass);
                sent = true;
            } else {
                Keyboard.send("");
            }
            Time.waitFor(15000, () -> stage() == 2);
        }
        if (stage == 2) {
            Mouse.click(CLICK_BOUNDS[1], true);
            return Time.waitFor(10000, RuneScape::playing);
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
